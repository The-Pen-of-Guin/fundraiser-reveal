package com.fundraiser.effects.particles;

import org.joml.Vector2f;

public class Particle {
	Vector2f position = new Vector2f();
	Vector2f velocity = new Vector2f();
	float rotation, angularVelocity;
	float r, g, b, a = 1f;
	float life, maxLife;
	float size;

	public Vector2f getPosition() {
		return position;
	}
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2f getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	public void setVelocity(float x, float y) {
		this.velocity.set(x, y);
	}

	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getAngularVelocity() {
		return angularVelocity;
	}
	public void setAngularVelocity(float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}

	public float getG() {
		return g;
	}
	public void setG(float g) {
		this.g = g;
	}

	public float getB() {
		return b;
	}
	public void setB(float b) {
		this.b = b;
	}

	public float getA() {
		return a;
	}
	public void setA(float a) {
		this.a = a;
	}

	public float getLife() {
		return life;
	}
	public void setLife(float life) {
		this.life = life;
	}

	public float getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(float maxLife) {
		this.maxLife = maxLife;
	}

	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
}
