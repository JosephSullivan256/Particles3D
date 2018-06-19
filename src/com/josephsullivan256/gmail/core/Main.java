package com.josephsullivan256.gmail.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.josephsullivan256.gmail.gl.BufferObject;
import com.josephsullivan256.gmail.gl.Shader;
import com.josephsullivan256.gmail.gl.Uniform;
import com.josephsullivan256.gmail.gl.UniformPasser;
import com.josephsullivan256.gmail.gl.Utils;
import com.josephsullivan256.gmail.gl.VertexArrayObject;
import com.josephsullivan256.gmail.gl.VertexAttributes;
import com.josephsullivan256.gmail.gl.Window;
import com.josephsullivan256.gmail.gl.camera.Camera;
import com.josephsullivan256.gmail.gl.camera.CameraCallback;
import com.josephsullivan256.gmail.gl.input.CollectionCallback;
import com.josephsullivan256.gmail.math.linalg.Matrix;
import com.josephsullivan256.gmail.math.linalg.Vec2;
import com.josephsullivan256.gmail.math.linalg.Vec3;
import com.josephsullivan256.gmail.math.linalg.Vec4;
import com.josephsullivan256.gmail.particles.Particle;
import com.josephsullivan256.gmail.particles.ParticleInteraction;

public class Main {
	
	public Main() {
		System.out.println("LWJGL " + Utils.getVersion());
		
		int width = 800;
		int height = 800;
		
		Utils.initGLFW();
		Window window = new Window("hello world",width,height);
		CollectionCallback callback = new CollectionCallback();
		window.setCallback(callback);
		
		Camera camera = new Camera(new Vec3(0,0,-10));
		CameraCallback cameraCallback = new CameraCallback(camera);
		callback.addCallback(cameraCallback);
		
		Utils.initGL();
		
		Shader shader = null;
		//shader = new Shader(Shaders.vs,Shaders.fs);
		try {
			shader = new Shader(Utils.readFile("shaders/shader.vsh"),Utils.readFile("shaders/shader.fsh"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		VertexArrayObject vao = new VertexArrayObject();
		vao.initialize(
				BufferObject.vbo().bind().bufferData(new float[] {
						-1f,-1f,
						1f,-1f,
						-1f,1f,
						1f,1f,
				}, GL15.GL_STATIC_DRAW), 
				BufferObject.ebo().bind().bufferData(new int[] {
						0,1,2,1,2,3
				}, GL15.GL_STATIC_DRAW),
				new VertexAttributes().with(2, GL11.GL_FLOAT));
		
		List<Particle> particles = initParticles(100, new Vec3(0,0,0), new Vec3(10,10,10));
		particles.addAll(
				initParticles(100,new Vec3(20,0,0), new Vec3(10,10,10))
				);
		ParticleInteraction p1 = new ParticleInteraction(1f, 2);
		ParticleInteraction p2 = new ParticleInteraction(-0.0001f, 4);
		float[] positions = new float[particles.size()*3];
		particlesToPositions(particles,positions);
		
		//Matrix netRotate = Matrix.m44i;
		//Matrix rotate = Matrix.ry44(0.005f);
		
		Uniform<float[]> positionsUniform = new Uniform<float[]>("positions",UniformPasser.uniformFloatsV3);
		Uniform<Matrix> transformUniform = new Uniform<Matrix>("transform",UniformPasser.uniformMatrix4);
		
		GL11.glViewport(0, 0, width, height);
		
		GL11.glClearColor(1f, 1f, 1f, 0f);
		while(!window.shouldClose()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			vao.bind();
			shader.use();
			positionsUniform.uniform(positions, shader);
			transformUniform.uniform(camera.getTransform(), shader);
			vao.draw(6);
			vao.unbind();
			
			apply(particles,p1);
			apply(particles,p2);
			update(particles,0.01f);
			particlesToPositions(particles,positions);
			//netRotate = rotate.times(netRotate);
			camera.move(cameraCallback.getMovement().scaledBy(0.1f));
			camera.rotate(cameraCallback.getRotation().scaledBy(0.01f));
			
			window.swapBuffers();
			Utils.pollGLFWEvents();
			
			/*try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		
		window.destroy();
		Utils.terminateGLFW();
	}
	
	public List<Particle> initParticles(int count, Vec3 center, Vec3 dim) {
		List<Particle> temp = new ArrayList<Particle>();
		
		/*for(float x = -0.9f; x < 0.9f; x+=0.1f) {
			for(float y = -0.9f; y < 0.9f; y+=0.1f) {
				temp.add(new Particle(new Vec2(x,y),0.1f));
			}
		}*/
		
		for(int i = 0; i < count; i++) {
			float x = (((float) Math.random())*2f-1f)*dim.x/2f;
			float y = (((float) Math.random())*2f-1f)*dim.y/2f;
			float z = (((float) Math.random())*2f-1f)*dim.z/2f;
			float m = (float)Math.random()*1f +0.1f;
			temp.add(new Particle(new Vec3(x,y,z).plus(center),m));
		}
		
		return temp;
	}
	
	public float[] initColors(int count){
		float[] colors = new float[count*3];
		
		for(int i = 0; i < count; i++){
			colors[3*i+0] = (float) Math.random();
			colors[3*i+1] = (float) Math.random();
			colors[3*i+2] = (float) Math.random();
			
		}
		
		return colors;
	}
	
	public void particlesToPositions(List<Particle> particles, float[] positions) {
		int i = 0;
		for(Particle particle: particles) {
			positions[3*i] = particle.getPos().x;
			positions[3*i+1] = particle.getPos().y;
			positions[3*i+2] = particle.getPos().z;
			i++;
		}
	}
	
	public void apply(List<Particle> particles, ParticleInteraction p) {
		for(int i = 0; i < particles.size(); i++) {
			for(int j = 0; j < i; j++) {
				p.apply(particles.get(i), particles.get(j));
			}
		}
	}
	
	public void update(List<Particle> particles, float t) {
		for(Particle particle: particles) {
			//particle.applyForce(particle.getVel().scaledBy(-0.1f));
			//particle.applyForce(Vec3.j.scaledBy(-1f));
			particle.integrate(t);
		}
	}
	
	public Vec3 getCOM(List<Particle> particles){
		Vec3 com = Vec3.zero;
		for(Particle particle: particles){
			com = com.plus(particle.getPos());
		}
		return com.scaledBy(1f/((float)particles.size()));
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
