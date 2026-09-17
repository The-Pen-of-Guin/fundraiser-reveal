package com.fundraiser.animation;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.fundraiser.utils.FFmpegEncoder;

import java.io.IOException;
import java.nio.*;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.nanovg.NanoVG.*;

public class AnimationEngine {
	// The window handle
	private long window;
	private long vg;

	private AtomicReference<String> text = new AtomicReference<>("$0");
	private float[] bgColor = {0.0f, 0.0f, 0.0f};
	private float[] textColor = {1.0f, 1.0f, 1.0f};
	private String font = "Roboto";
	private int fontSize = 150;

	public void setText(String text) { this.text.set(text); }
	public String getText() { return this.text.get(); }
	public void setBgColor(float r, float g, float b) { this.bgColor = new float[]{r, g, b}; }
	public void setTextColor(float r, float g, float b) { this.textColor = new float[]{r, g, b}; }
	public void setFont(String font) { this.font = font; }

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
		cleanup();
	}

	public void run(Path outputFile, int fps, int durationSeconds) {
		System.out.println("Duration: " + durationSeconds + " seconds");
		init();
		try (var encoder = new FFmpegEncoder(outputFile, 1920, 1080, fps)) {
			loop(encoder, fps, durationSeconds);
		} catch (IOException e) {
			throw new RuntimeException("Encoding Failed: ", e);
		} finally {
			cleanup();
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1920, 1080, "GIC Reveal Generator", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window, Wayland doesn't allow setting window position
			if (glfwGetPlatform() != GLFW_PLATFORM_WAYLAND) {
				glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
			}
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
		if (vg == 0) throw new RuntimeException("Failed to create NanoVG context");

		ByteBuffer fontData = getFontPath(font);

		// Load a system font
		int loadedFont = NanoVG.nvgCreateFontMem(vg, "font", fontData, false);
		if (loadedFont == -1) System.err.println("Font failed to load - check the path");
	}

	private void loop() {
		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetFramebufferSize(window, w, h);

			int width = w.get(0);
			int height = h.get(0);

			glViewport(0, 0, width, height);
			glClearColor(bgColor[0], bgColor[1], bgColor[2], 1f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			nvgBeginFrame(vg, width, height, 1f);

			NanoVG.nvgFontSize(vg, fontSize);
			NanoVG.nvgFontFace(vg, "font");
			NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE);

			try (MemoryStack stack = MemoryStack.stackPush()) {
				NVGColor color = NVGColor.malloc(stack);
				color.r(textColor[0]).g(textColor[1]).b(textColor[2]).a(1f);
				NanoVG.nvgFillColor(vg, color);
			}

			NanoVG.nvgText(vg, width / 2f, height / 2f, getText());

			nvgEndFrame(vg);

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void loop(FFmpegEncoder encoder, int fps, int durationSeconds) throws IOException {
        	glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        	int totalFrames = fps * durationSeconds;
        	ByteBuffer pixelBuffer = null;

        	for (int frame = 0; frame < totalFrames && !glfwWindowShouldClose(window); frame++) {
        	        IntBuffer w = BufferUtils.createIntBuffer(1);
        	        IntBuffer h = BufferUtils.createIntBuffer(1);
        	        glfwGetFramebufferSize(window, w, h);
        	        int width = w.get(0), height = h.get(0);

        	        if (pixelBuffer == null) pixelBuffer = BufferUtils.createByteBuffer(width * height * 4);

        	        glViewport(0, 0, width, height);
        	        glClearColor(bgColor[0], bgColor[1], bgColor[2], 1f);
        	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        	        nvgBeginFrame(vg, width, height, 1f);
        	        NanoVG.nvgFontSize(vg, fontSize);
        	        NanoVG.nvgFontFace(vg, "font");
        	        NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE);
        	        try (MemoryStack stack = MemoryStack.stackPush()) {
        	                NVGColor color = NVGColor.malloc(stack);
        	                color.r(textColor[0]).g(textColor[1]).b(textColor[2]).a(1f);
        	                NanoVG.nvgFillColor(vg, color);
        	        }
        	        NanoVG.nvgText(vg, width / 2f, height / 2f, getText());
        	        nvgEndFrame(vg);

        	        pixelBuffer.clear();
        	        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
        	        encoder.writeFrame(pixelBuffer);

        	        glfwSwapBuffers(window);
        	        glfwPollEvents();
        	}
	}

	private void cleanup() {
		nvgDelete(vg);

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private ByteBuffer getFontPath(String font) {
		String path = switch (font) {
			case "Roboto" -> "/fonts/Roboto-Regular.ttf";
			default -> throw new IllegalArgumentException(String.format("Font type %s not supported!"));
		};

		byte[] bytes;
		try {
			bytes = getClass().getResourceAsStream(path).readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException("Failed to read font resource: ", e);
		}

		ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		
		return buffer;
	}
}
