import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
	public static void main(String[] args) {
		//get screen res
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		
		//create window
		Window window = new Window(size.width, size.height);
		
		//init window
		window.init();
		
		//basic update loop
		while(window.shouldClose() == false) {
			window.update();
		}
	}
}
