package opengl;

import java.awt.Dimension;
import java.awt.Toolkit;

import ui.*;

public class Main {
	public static Window window;
	public static void main(String[] args) {
		// get screen res
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

		// create window
		window = new Window(size.width, size.height);

		// init window
		window.init();

		// create a background label and add it to the window
		Background bg = new StrobeBackground(0.01f, 0.01f, 0.01f) {
			int rMul = 1;
			int gMul = 1;
			int bMul = 1;
			@Override
			public void update() {
				float r = getR() + rMul * (float) Math.random() * 0.02f;
				float g = getG() + gMul * (float) Math.random() * 0.02f;
				float b = getB() + bMul * (float) Math.random() * 0.02f;
				if(r > 0.98f || r < 0.02f) rMul *= -1;
				if(g > 0.98f || g < 0.02f) gMul *= -1;
				if(b > 0.98f || b < 0.02f) bMul *= -1;
				updateColor(r,g,b);
			}
		};
		window.addLabel(bg);
		
		//then start the loop
		window.start();
	}
}
