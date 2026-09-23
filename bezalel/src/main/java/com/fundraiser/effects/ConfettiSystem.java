package com.fundraiser.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.lwjgl.BufferUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.lwjgl.opengl.GL30.*;

public class ConfettiSystem {
	private GifAnimation gif;

	public void init() {
		gif = new GifAnimation("conffeti.gif");
	}

	public void spawn(float x, float y, int count) {
	}

	public void update(float x, float y, float xScale, float yScale, int screenWidth, int screenHeight) {
		gif.update();
		drawOverlayQuad(gif.getCurrentTextureId(), x - (gif.getWidth() * xScale) / 2f, y - (gif.getHeight() * yScale) / 2f, gif.getWidth() * xScale, gif.getHeight() * yScale, screenWidth, screenHeight);
	}

	private static void drawOverlayQuad(int textureId, float x, float y, float w, float h,
	                                    int screenWidth, int screenHeight) {
	    glDisable(GL_DEPTH_TEST);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
	
	    glMatrixMode(GL_PROJECTION);
	    glPushMatrix();
	    glLoadIdentity();
	    glOrtho(0, screenWidth, screenHeight, 0, -1, 1); // top-left origin, screen-space
	
	    glMatrixMode(GL_MODELVIEW);
	    glPushMatrix();
	    glLoadIdentity();
	
	    glBindTexture(GL_TEXTURE_2D, textureId);
	    glEnable(GL_TEXTURE_2D);
	
	    glBegin(GL_QUADS);
	        glTexCoord2f(0, 0); glVertex2f(x,     y);
	        glTexCoord2f(1, 0); glVertex2f(x + w, y);
	        glTexCoord2f(1, 1); glVertex2f(x + w, y + h);
	        glTexCoord2f(0, 1); glVertex2f(x,     y + h);
	    glEnd();
	
	    glMatrixMode(GL_PROJECTION);
	    glPopMatrix();
	    glMatrixMode(GL_MODELVIEW);
	    glPopMatrix();
	
	    glEnable(GL_DEPTH_TEST); // restore 3D state for next frame's scene
	}


	private int getFrameDelay(ImageReader reader, int index) throws IOException {
		IIOMetadata metadata = reader.getImageMetadata(index);
		String metaFormat = metadata.getNativeMetadataFormatName();
		Node root = metadata.getAsTree(metaFormat);

		int delay = 100; // fallback default (ms), in case GIF omits it
		
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeName().equals("GraphicControlExtension")) {
				String delayAttr = ((IIOMetadataNode) node).getAttribute("delayTime");
				// GIF stores delay in hundredths of a second - convert to ms
				delay = Integer.parseInt(delayAttr) * 10;
				break;
			}
		}

		// Some GIFs specify 0ms delay - browsers/viewers treat that as ~100ms
		if (delay <= 0) {
			delay = 100;
		}
		return delay;
	}

	private record LoadedGifInfo(int[] textureIds, int[] delays, float width, float height) {}

	private LoadedGifInfo readGif(String filePath) {
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		try {
			reader.setInput(ImageIO.createImageInputStream(new File(filePath)));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read GIF.");
		}

		int frames;
		try {
			frames = reader.getNumImages(true);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read GIF frames size");
		}

		var textureIds = new int[frames];
		var delays = new int[frames];
		int width = 0;
		int height = 0;
		try {
			int logicalWidth = reader.read(0).getWidth();
			int logicalHeight = reader.read(0).getHeight();
			width = logicalWidth;
			height = logicalHeight;

			BufferedImage canvas = new BufferedImage(logicalWidth, logicalHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = canvas.createGraphics();
			g.setBackground(new Color(0, 0, 0, 0));

			for (int i = 0; i < frames; i++) {
				BufferedImage raw = reader.read(i);
				IIOMetadata metadata = reader.getImageMetadata(i);
				Node root = metadata.getAsTree(metadata.getNativeMetadataFormatName());

				int x = 0, y = 0;
				String disposalMethod = "none";

				NodeList children = root.getChildNodes();
				for (int j = 0; j < children.getLength(); j++) {
					Node node = children.item(j);
					if (node.getNodeName().equals("ImageDescriptor")) {
						x = Integer.parseInt(((IIOMetadataNode) node).getAttribute("imageLeftPosition"));
						y = Integer.parseInt(((IIOMetadataNode) node).getAttribute("imageTopPosition"));
					}
					if (node.getNodeName().equals("GraphicControlExtension")) {
						disposalMethod = ((IIOMetadataNode) node).getAttribute("disposalMethod");
					}
				}

				BufferedImage previous = disposalMethod.equals("restoreToPrevious") ? deepCopy(canvas) : null;

				g.drawImage(raw, x, y, null);
				BufferedImage frameSnapshot = deepCopy(canvas);

				if (disposalMethod.equals("restoreToBackgroundColor")) {
					g.clearRect(x, y, raw.getWidth(), raw.getHeight());
				} else if (disposalMethod.equals("restoreToPrevious") && previous != null) {
					canvas = previous;
					g = canvas.createGraphics();
					g.setBackground(new Color(0, 0, 0, 0));
				}

				ByteBuffer buffer = convertBufferedImageToByteBuffer(frameSnapshot);
				textureIds[i] = uploadTexture(buffer, frameSnapshot.getWidth(), frameSnapshot.getHeight());
				delays[i] = getFrameDelay(reader, i);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read GIF frames.");
		}

		return new LoadedGifInfo(textureIds, delays, width, height);
	}

	private BufferedImage deepCopy(BufferedImage src) {
		var copy = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		Graphics2D g = copy.createGraphics();
		g.drawImage(src, 0, 0, null);
		g.dispose();
		return copy;
	}

	private ByteBuffer convertBufferedImageToByteBuffer(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		// Pull pixels into an int array first - faster tahn pixel-by-pixel getRGB calls
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >>  8) & 0xFF;
				int b =  pixel &        0xFF;
				int a = (pixel >> 24) & 0xFF;

				r = r * a / 255;
				g = g * a / 255;
				b = b * a / 255;

				buffer.put((byte) r);
				buffer.put((byte) g);
				buffer.put((byte) b);
				buffer.put((byte) a);
			}
		}

		buffer.flip();
		return buffer;
	}

	private int uploadTexture(ByteBuffer buffer, int width, int height) {
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);

		// Filtering - GL_NEAREST keeps GIF pixels crisp, GL_LINEAR smooths them
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// Clamp so edges don't wrap/bleed
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		return textureId;
	}

	private class GifAnimation {
		public int[] textureIds;
		public int[] delays;

		private int currentFrame = 0;
		private long lastFrameTime = 0;

		private float width;
		private float height;

		public GifAnimation(String path) {
			LoadedGifInfo info = readGif(path);

			this.textureIds = info.textureIds();
			this.delays = info.delays();
			this.width = info.width();
			this.height = info.height();
		}

		public void update() {
			long now = System.currentTimeMillis();
			if (lastFrameTime == 0) lastFrameTime = now; // first call, init clock

			if (now - lastFrameTime >= delays[currentFrame]) {
				lastFrameTime = now;
				currentFrame = (currentFrame + 1) % textureIds.length; // loop back to 0
			}
		}

		public int getCurrentTextureId() {
			return textureIds[currentFrame];
		}

		public float getWidth() {
			return width;
		}

		public float getHeight() {
			return height;
		}
	}
}

// Old Confetti Code
	// public void init() {
	// 	// Quad mesh (2 trianfles, unit size centered at origin)
	// 	float[] quad = {
	// 		-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
	// 		-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
	// 	};
	//
	// 	vao = glGenVertexArrays();
	// 	glBindVertexArray(vao);
	//
	// 	quadVBO = glGenBuffers();
	// 	glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
	// 	glBufferData(GL_ARRAY_BUFFER, quad, GL_STATIC_DRAW);
	// 	glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
	// 	glEnableVertexAttribArray(0);
	//
	// 	// Instance buffer: pos(2) + rot(1) + color(4) + size(1) = 8 floats per particle
	// 	instanceVBO = glGenBuffers();
	// 	glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
	// 	glBufferData(GL_ARRAY_BUFFER, (long) MAX_PARTICLES * 8 * 4, GL_DYNAMIC_DRAW);
	//
	// 	int stride = 8 * 4;
	// 	glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 0);   // x,y,rot
	// 	glEnableVertexAttribArray(1);
	// 	glVertexAttribDivisor(1, 1);
	//
	// 	glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 3 * 4); // rgba
	// 	glEnableVertexAttribArray(2);
	// 	glVertexAttribDivisor(2, 1);
	//
	// 	glVertexAttribPointer(3, 1, GL_FLOAT, false, stride, 7 * 4);
	// 	glEnableVertexAttribArray(3);
	// 	glVertexAttribDivisor(3, 1);
	//
	// 	InputStream vertStream = getClass().getClassLoader().getResourceAsStream("shaders/confetti/confetti.vert");
	// 	InputStream fragStream = getClass().getClassLoader().getResourceAsStream("shaders/confetti/confetti.frag");
	//
	// 	shaderProgram = ShaderUtils.load(vertStream, fragStream);
	// }
	//
	//    	public void spawn(float x, float y, int count) {
	// 	var rnd = new Random();
	// 	for (int i = 0; i < count && particles.size() < MAX_PARTICLES; i++) {
	// 		var p = new Particle();
	// 		p.setSize(20f + rnd.nextFloat() * 20f);
	// 	  	p.setPosition(x, y);
	// 	  	float angle = (float)(rnd.nextDouble() * -(1f/6f)*Math.PI - (Math.PI/2f - 0.25));
	// 	  	float speed = 2000 + rnd.nextFloat() * 500;
	// 	  	p.setVelocity((float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed);
	// 	  	p.setRotation(rnd.nextFloat() * 360);
	// 	  	p.setAngularVelocity((rnd.nextFloat() - 0.5f) * 720);
	// 	  	p.setR(rnd.nextFloat()); p.setG(rnd.nextFloat()); p.setB(rnd.nextFloat());
	// 		var life = BASE_LIFE + rnd.nextFloat();
	// 	  	p.setMaxLife(life);
	// 		p.setLife(life);
	// 	  	particles.add(p);
	// 	}
	//    	}
	//
	//    	public void update(float dt) {
	//    	    Iterator<Particle> it = particles.iterator();
	//    	    while (it.hasNext()) {
	//    	        Particle p = it.next();
	//    	        p.setVelocity(p.getVelocity().x, p.getVelocity().y + GRAVITY * dt);      // gravity
	//    	        p.setVelocity(p.getVelocity().mul(0.99f));          // drag
	//    	        p.setPosition(p.getPosition().add(p.getVelocity().x * dt, p.getVelocity().y * dt));
	//    	        p.setRotation(p.getRotation() + p.getAngularVelocity() * dt);
	//    	        p.setLife(p.getLife() - dt);
	//    	        // p.setA(Math.max(0, p.getLife() / p.getMaxLife())); // fade out
	//    	        if (p.getLife() <= 0) it.remove();
	// 	System.out.println("x: " + p.getPosition().x + ", y: " + p.getPosition().y);
	//    	    }
	//    	}
	//
	//    	public void render(Matrix4f projection) {
	//    	    instanceData.clear();
	//    	    for (Particle p : particles) {
	//    	        instanceData.put(p.getPosition().x).put(p.getPosition().y).put((float)Math.toRadians(p.getRotation()));
	//    	        instanceData.put(p.getR()).put(p.getG()).put(p.getB()).put(p.getA()).put(p.getSize());
	//    	    }
	//    	    instanceData.flip();
	//
	//    	    glUseProgram(shaderProgram);
	//
	//     int projLoc = glGetUniformLocation(shaderProgram, "projection");
	//     var matBuffer = new float[16];
	//     projection.get(matBuffer);
	//     glUniformMatrix4fv(projLoc, false, matBuffer);
	//
	//    	    glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
	//    	    glBufferSubData(GL_ARRAY_BUFFER, 0, instanceData);
	//
	//    	    glEnable(GL_BLEND);
	//    	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	//
	//    	    glBindVertexArray(vao);
	//    	    glDrawArraysInstanced(GL_TRIANGLES, 0, 6, particles.size());
	//    	}
