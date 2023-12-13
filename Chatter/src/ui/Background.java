package ui;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

//a basic label that has a solid color, covers the screen, and is a background
public class Background implements Label{
	
	//handles to vao (holds vbo, ebo) and vbo (holds vertices) & ebo (holds indices)
	protected int vao;
	protected int vbo;
	protected int ebo;
	
	//handle to mesh
	protected float[] mesh;
	protected int[] indices;
	
	//color
	protected float r;
	protected float g;
	protected float b;
	
	//number to represent how many floats per vertex
	public static final int FLOATS_PER_VERTEX = 5;
	
	public Background(float r, float g, float b) {
		
		//create the mesh
		float[] v = {
			-1.0f, -1.0f, r, g, b,
			 1.0f, -1.0f, r, g, b,
			-1.0f,  1.0f, r, g, b,
			 1.0f,  1.0f, r, g, b
		};
		mesh = v;
		
		int[] i = {
				0, 1, 2,
				1, 2, 3
		};
		indices = i;
		
		//load into buffers
		FloatBuffer vertices = MemoryUtil.memAllocFloat(v.length);
		vertices.put(v);
		vertices.flip();
		
		IntBuffer indices = MemoryUtil.memAllocInt(i.length);
		indices.put(i);
		indices.flip();
		
		//create vao and bind
		vao = GL30.glGenVertexArrays();
		
		GL30.glBindVertexArray(vao);
		
		//create vbo and bind and buffer the data
		vbo = GL30.glGenBuffers();
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
		GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertices, GL20.GL_STATIC_DRAW);
		
		//create ebo and bind and buffer the data
		ebo = GL30.glGenBuffers();
		GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, indices, GL20.GL_STATIC_DRAW);
		
		//layout the buffer
		GL20.glVertexAttribPointer(0, 2, GL20.GL_FLOAT, false, (2+3) * Float.BYTES, 0);
		GL20.glVertexAttribPointer(1, 3, GL20.GL_FLOAT, true, (2+3) * Float.BYTES, (2) * Float.BYTES);
		
		//unbind and free
		GL30.glBindVertexArray(0);
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
		GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
		MemoryUtil.memFree(vertices);
		MemoryUtil.memFree(indices);
	}

	@Override
	public void render() {
		//bind to the vao that contains the vbo and ebo
		GL30.glBindVertexArray(vao);
		
		//enable the formatting
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		
		//bind to the ebo (to draw the indices)
		GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, ebo);
		
		//then draw the stuff
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
		
		//unbind
		GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	@Override
	public void cleanup() {
		//delete
		GL20.glDeleteBuffers(vbo);
		GL20.glDeleteBuffers(ebo);
		GL30.glDeleteVertexArrays(vao);
	}
	
	//this is a solid color background, is not textured
	@Override
	public boolean isTextured() {
		return false;
	}
	
	public void reload() {

		//load into buffers
		FloatBuffer vertices = MemoryUtil.memAllocFloat(mesh.length);
		vertices.put(mesh);
		vertices.flip();
		
		IntBuffer indices = MemoryUtil.memAllocInt(this.indices.length);
		indices.put(this.indices);
		indices.flip();
		
		//create vao and bind
		vao = GL30.glGenVertexArrays();
		
		GL30.glBindVertexArray(vao);
		
		//create vbo and bind and buffer the data
		vbo = GL30.glGenBuffers();
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
		GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertices, GL20.GL_STATIC_DRAW);
		
		//create ebo and bind and buffer the data
		ebo = GL30.glGenBuffers();
		GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, indices, GL20.GL_STATIC_DRAW);
		
		//layout the buffer
		GL20.glVertexAttribPointer(0, 2, GL20.GL_FLOAT, false, (2+3) * Float.BYTES, 0);
		GL20.glVertexAttribPointer(1, 3, GL20.GL_FLOAT, true, (2+3) * Float.BYTES, (2) * Float.BYTES);
		
		//unbind and free
		GL30.glBindVertexArray(0);
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
		GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
		MemoryUtil.memFree(vertices);
		MemoryUtil.memFree(indices);
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return g;
	}
	
	public float getB() {
		return b;
	}
	
	//purposely leave empty for anonymous classes to override n implement
	public void update() {
		
	}
}
