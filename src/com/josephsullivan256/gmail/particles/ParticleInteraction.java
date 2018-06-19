package com.josephsullivan256.gmail.particles;

import com.josephsullivan256.gmail.math.linalg.Vec3;

public class ParticleInteraction {
	
	private float k, pow;
	
	public ParticleInteraction(float k, float pow) {
		this.k = k;
		this.pow = pow;
	}
	
	public void apply(Particle p1, Particle p2) {
		Vec3 dist = p1.getPos().minus(p2.getPos());
		float mag = dist.magnitude();
		dist = dist.scaledBy(1f/mag);
		
		mag = Math.max(0.05f,mag);
		
		float force = -k*p1.getMass()*p2.getMass()/((float) Math.pow(mag, pow));
		Vec3 fv = dist.scaledBy(force);
		
		p1.applyForce(fv);
		p2.applyForce(fv.scaledBy(-1f));
	}
}
