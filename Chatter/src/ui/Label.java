package ui;
public interface Label {
	public void render();
	public void cleanup();
	public boolean isTextured();
	//method for scripting
	public void update();
}
