package ui;

public class StrobeBackground extends Background{
	
	public StrobeBackground(float r, float g, float b) {
		super(r, g, b);
	}
	
	//only difference between this background and a regular background is that this one can update its colors
	public void updateColor(float r, float g, float b) {
		float[] mesh = super.mesh;
		for(int i = 0; i< 4; i++) {
			mesh[i*5 + 2] = r;
			mesh[i*5 + 3] = g;
			mesh[i*5 + 4] = b;
		}
		super.reload();
		
		super.r = r;
		super.g = g;
		super.b = b;
	}
	
}
