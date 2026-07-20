package com.fundraiser.animation;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.time.Duration;
import java.time.Instant;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.nanovg.NanoVG.*;

public class HelloWorld {

	// The window handle
	private long window;
	private long vg;

	private String text = "$0";
	private float[] bgColor = {1.0f, 0.55f, 0.0f};
	private float[] textColor = {0.5f, 0.0f, 0.5f};

	Instant previousTime = Instant.now();
	int amount = 0;

	public void setText(String text) { this.text = text; }
	public void setBgColor(float r, float g, float b) { this.bgColor = new float[]{r, g, b}; }
	public void setTextColor(float r, float g, float b) { this.textColor = new float[]{r, g, b}; }

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
		cleanup();
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
		window = glfwCreateWindow(800, 600, "GIC Reveal Generator", NULL, NULL);
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

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
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

		// Load a system font
		int font = NanoVG.nvgCreateFont(vg, "roboto", "C:\\Users\\Isaia\\Documents\\Projects\\GIC_number_reveal\\number_reveal\\assets\\fonts\\Roboto\\static\\Roboto-Regular.ttf");
		if (font == -1) System.err.println("Font failed to load - check the path");
	}

	private void loop() {
		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			if (Duration.between(previousTime, Instant.now()).toMillis() > 100.0) {
				text = "$" + amount++;
				previousTime = Instant.now();
			}

			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetFramebufferSize(window, w, h);

			int width = w.get(0);
			int height = h.get(0);

			glViewport(0, 0, width, height);
			glClearColor(bgColor[0], bgColor[1], bgColor[2], 1f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			nvgBeginFrame(vg, width, height, 1f);

			NanoVG.nvgFontSize(vg, 48);
			NanoVG.nvgFontFace(vg, "roboto");
			NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE);

			try (MemoryStack stack = MemoryStack.stackPush()) {
				NVGColor color = NVGColor.malloc(stack);
				color.r(textColor[0]).g(textColor[1]).b(textColor[2]).a(1f);
				NanoVG.nvgFillColor(vg, color);
			}

			NanoVG.nvgText(vg, width / 2f, height / 2f, text);

			nvgEndFrame(vg);

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
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

	public static void main(String[] args) {
		new HelloWorld().run();
	}

}