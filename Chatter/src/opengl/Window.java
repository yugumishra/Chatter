package opengl;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import ui.Label;

public class Window {
	
	//this section is for shader uniform names
	public static final String TEXTURED = "textured";
	
	// handle to GLFW window
	private long window;
	// width and height of window
	private int width;
	private int height;
	
	//uniform mapping
	private HashMap<String, Integer> uniforms;
	
	//program handle
	private int program;

	// list of labels that is the UI
	private ArrayList<Label> ui;

	public Window(int width, int height) {
		this.width = width;
		this.height = height;
		ui = new ArrayList<Label>();
		uniforms = new HashMap<String, Integer>();
	}

	public void init() {
		// init glfw
		if (GLFW.glfwInit() == false) {
			System.err.println("GLFW failed to initialize. Try again");
			System.exit(0);
		}

		// set the window hints
		GLFW.glfwDefaultWindowHints();

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);

		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		window = GLFW.glfwCreateWindow(width / 2, height / 2, "Chatter, the best chat app for friends!",
				MemoryUtil.NULL, MemoryUtil.NULL);

		// center on screen
		GLFW.glfwSetWindowPos(window, width / 4, height / 4);

		// check if failed
		if (window == MemoryUtil.NULL) {
			System.err.println("Window failed to create. Please try again");
			System.exit(0);
		}

		// make exit callback
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				// close window
				GLFW.glfwSetWindowShouldClose(window, true);
			}
		});
		
		// make window changed callback
		//so the opengl viewports maps to the window perfectly
		GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			GL11.glViewport(0, 0, width, height);
			this.width = width;
			this.height = height;
		});

		// make context current
		GLFW.glfwMakeContextCurrent(window);

		// init opengl
		GL.createCapabilities();

		// enable visibility
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_TRUE);
		GLFW.glfwShowWindow(window);

		// enable vsync
		GLFW.glfwSwapInterval(1);

		// set up rendering
		int vid = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		String vertexShader = 
				"#version 330 core\n" 
				+ "\n" 
				+ "layout (location = 0) in vec2 pos;\n"
				+ "layout (location = 1) in vec3 Var;\n"
				+ "\n"
				+ "out vec3 var;\n"
				+ "\n"
				+ "void main() {\n" 
				+ "gl_Position = vec4(pos, 1.0, 1.0);\n" 
				+ "var = Var;\n"
				+ "}\n";
		GL20.glShaderSource(vid, vertexShader);

		GL20.glCompileShader(vid);

		if (GL20.glGetShaderi(vid, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
			// uh oh
			System.err.println("Vertex shader failed to compile.");
			System.err.println(GL20.glGetShaderInfoLog(vid));
			GL20.glDeleteShader(vid);
			cleanup();
			System.exit(0);
		}

		int fid = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		String fragmentShader = "#version 330 core\n" 
				+ "\n"
				+ "in vec3 var;\n"
				+ "out vec4 color;\n" 
				+ "\n"
				+ "void main() {\n"
				+ "color = vec4(var.xyz, 1.0);"
				+ "}\n";

		GL20.glShaderSource(fid, fragmentShader);

		GL20.glCompileShader(fid);

		if (GL20.glGetShaderi(fid, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
			// uh oh 2
			System.err.println("Fragment shader failed to compile");
			System.err.println(GL20.glGetShaderInfoLog(fid));
			GL20.glDeleteShader(fid);
			cleanup();
			System.exit(0);
		}

		// link them together
		int program = GL20.glCreateProgram();

		GL20.glAttachShader(program, vid);
		GL20.glAttachShader(program, fid);

		GL20.glLinkProgram(program);

		if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
			// uh oh 3
			System.err.println("Program failed to link");
			System.err.println(GL20.glGetProgramInfoLog(program));

			GL20.glDeleteShader(vid);
			GL20.glDeleteShader(fid);
			GL20.glDeleteProgram(program);

			cleanup();
			System.exit(0);
		}

		// discard the shaders, the executable has already been created
		GL20.glDeleteShader(vid);
		GL20.glDeleteShader(fid);

		// use this program
		GL20.glUseProgram(program);

		// now that rendering has been setup, we can render stuff
	}
	
	public void addUniform(String name) {
		int location = GL20.glGetUniformLocation(program, name);
		System.out.println(location);
		if(location < 0) {
			System.err.println("Uniform " + name + " not found");
			return;
		}
		uniforms.put(name, location);
	}
	
	public int getUniform(String name) {
		return uniforms.get(name);
	}
	
	//this method starts the render loop
	public void start() {
		// this is the basic loop
		while (this.shouldClose() == false) {
			update();

			// render & update each label
			for (Label l : ui) {
				l.render();
				l.update();
			}
		}

		// cleanup the labels
		for (Label l : ui) {
			l.cleanup();
		}

		// cleanup the window
		cleanup();
	}

	public void update() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}

	public void cleanup() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public void addLabel(Label l) {
		ui.add(l);
	}
}
