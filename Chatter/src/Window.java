import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
	//handle to GLFW window
	private long window;
	//width and height of window
	private int width;
	private int height;
	
	public Window(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void init() {
		//init glfw
		if(GLFW.glfwInit() == false) {
			System.err.println("GLFW failed to initialize. Try again");
			System.exit(0);
		}
		
		//set the window hints
		GLFW.glfwDefaultWindowHints();
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
		
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		
		window = GLFW.glfwCreateWindow(width/2, height/2, "Chatter, the best chat app for friends!", MemoryUtil.NULL, MemoryUtil.NULL);
		
		//center on screen
		GLFW.glfwSetWindowPos(window, width/4, height/4);
		
		//check if failed
		if(window == MemoryUtil.NULL) {
			System.err.println("Window failed to create. Please try again");
			System.exit(0);
		}
		
		//make exit callback
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				//close window
				GLFW.glfwSetWindowShouldClose(window, true);
			}
		});
		
		
		//make context current
		GLFW.glfwMakeContextCurrent(window);
		
		//init opengl
		GL.createCapabilities();
		
		//set bg color
		GL11.glClearColor(211.0f/255.0f,243.0f/255.0f,1.0f, 1.0f);
		
		//enable visibility
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_TRUE);
		GLFW.glfwShowWindow(window);
		
		//enable vsync
		GLFW.glfwSwapInterval(1);
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
}
