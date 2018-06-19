package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexArrayObject {
	
	private int vao;
	
	public VertexArrayObject() {
		vao = glGenVertexArrays();
	}
	
	public void initialize(BufferObject vbo, BufferObject ebo, VertexAttributes va) {
		bind();
		
		vbo.bind();
		ebo.bind();
		
		va.apply();
		
		unbind();
		vbo.unbind();
		ebo.unbind();
	}
	
	public void bind() {
		glBindVertexArray(vao);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	public void draw(int count) {
		//glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
	}
}
