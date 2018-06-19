package com.josephsullivan256.gmail.particles;

import com.josephsullivan256.gmail.math.linalg.Vec3;

public class Particle {
	private Vec3 pos;
	private Vec3 vel;
	private float mass;
	
	private Vec3 accel;
	
	public Particle(Vec3 pos, Vec3 vel, float mass) {
		this.pos = pos;
		this.vel = vel;
		this.mass = mass;
		this.accel = Vec3.zero;
	}
	
	public Particle(Vec3 pos, float mass) {
		this(pos, Vec3.zero, mass);
	}
	
	public void applyForce(Vec3 f) {
		accel = accel.plus(f.scaledBy(1f/mass));
	}
	
	public void integrate(float t) {
		//vel = vel.scaledBy(0.99f);
		Vec3 oldVel = vel;
		vel = vel.plus(accel.scaledBy(t));
		pos = pos.plus(vel.plus(oldVel).scaledBy(t/2f));
		accel = Vec3.zero;
	}
	
	public Vec3 getPos() {
		return pos;
	}
	
	public Vec3 getVel() {
		return vel;
	}
	
	public float getMass() {
		return mass;
	}
}
