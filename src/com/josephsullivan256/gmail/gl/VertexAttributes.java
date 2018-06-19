package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.List;

public class VertexAttributes {
	
	private List<VertexAttribute> attributes;
	
	public VertexAttributes() {
		attributes = new ArrayList<VertexAttribute>();
	}
	
	public VertexAttributes with(int count, int type) {
		attributes.add(new VertexAttribute(count,type));
		return this;
	}
	
	public void apply() {
		int stride = 0;
		for(VertexAttribute attrib: attributes) {
			stride+=getSize(attrib.type)*attrib.count;
		}
		
		int offset = 0;
		int location = 0;
		for(VertexAttribute attrib: attributes) {
			glVertexAttribPointer(location, attrib.count, attrib.type, false, stride, offset);
			glEnableVertexAttribArray(location);
			location++;
			offset+=getSize(attrib.type)*attrib.count;
		}
	}
	
	private static int getSize(int type) {
		if(type == GL_FLOAT) {
			return 4;
		} else if (type == GL_INT) {
			return 4;
		} else if (type == GL_BYTE) {
			return 1;
		}
		return 0;
	}
	
	private static class VertexAttribute{
		public final int count, type;
		public VertexAttribute(int count, int type) {
			this.count = count;
			this.type = type;
		}
	}
}
