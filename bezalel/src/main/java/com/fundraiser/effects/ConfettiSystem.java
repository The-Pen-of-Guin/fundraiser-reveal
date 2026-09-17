package com.fundraiser.effects;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.*;

import com.fundraiser.effects.particles.Particle;
import com.fundraiser.utils.ShaderUtils;

public class ConfettiSystem {
	static final int MAX_PARTICLES = 500;
	List<Particle> particles = new ArrayList<>();
	int vao, quadVBO, instanceVBO, shaderProgram;
	FloatBuffer instanceData = BufferUtils.createFloatBuffer(MAX_PARTICLES * 7);

	public void init() {
		// Quad mesh (2 trianfles, unit size centered at origin)
		float[] quad = {
			-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
		};

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		quadVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
		glBufferData(GL_ARRAY_BUFFER, quad, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);

		// Instance buffer: pos(2) + rot(1) + color(4) = 7 floats per particle
		instanceVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glBufferData(GL_ARRAY_BUFFER, (long) MAX_PARTICLES * 7 * 4, GL_DYNAMIC_DRAW);

		int stride = 7 * 4;
		glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 0);   // x,y,rot
		glEnableVertexAttribArray(1);
		glVertexAttribDivisor(1, 1);

		glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 3 * 4); // rgba
		glEnableVertexAttribArray(2);
		glVertexAttribDivisor(2, 1);

		InputStream vertStream = getClass().getClassLoader().getResourceAsStream("shaders/confetti/confetti.vert");
		InputStream fragStream = getClass().getClassLoader().getResourceAsStream("shaders/confetti/confetti.frag");

		shaderProgram = ShaderUtils.load(vertStream, fragStream);
	}

    	public void spawn(float x, float y, int count) {
		var rnd = new Random();
		for (int i = 0; i < count && particles.size() < MAX_PARTICLES; i++) {
			var p = new Particle();
		  	p.setPosition(x, y);
		  	float angle = (float)(rnd.nextDouble() * Math.PI - Math.PI/2); // upward cone
		  	float speed = 200 + rnd.nextFloat() * 300;
		  	p.setVelocity((float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed);
		  	p.setRotation(rnd.nextFloat() * 360);
		  	p.setAngularVelocity((rnd.nextFloat() - 0.5f) * 720);
		  	p.setR(rnd.nextFloat()); p.setG(rnd.nextFloat()); p.setB(rnd.nextFloat());
			var life = 10f + rnd.nextFloat();
		  	p.setMaxLife(life);
			p.setLife(life);
		  	particles.add(p);
		}
    	}

    	public void update(float dt) {
    	    Iterator<Particle> it = particles.iterator();
    	    while (it.hasNext()) {
    	        Particle p = it.next();
    	        p.setVelocity(p.getVelocity().x, p.getVelocity().y - 500f * dt);      // gravity
    	        p.setVelocity(p.getVelocity().mul(0.99f));          // drag
    	        p.setPosition(p.getPosition().add(p.getVelocity().x * dt, p.getVelocity().y * dt));
    	        p.setRotation(p.getRotation() + p.getAngularVelocity() * dt);
    	        p.setLife(p.getLife() - dt);
    	        p.setA(Math.max(0, p.getLife() / p.getMaxLife())); // fade out
    	        if (p.getLife() <= 0) it.remove();
    	    }
    	}

    	public void render(Matrix4f projection) {
    	    instanceData.clear();
    	    for (Particle p : particles) {
    	        instanceData.put(p.getPosition().x).put(p.getPosition().y).put((float)Math.toRadians(p.getRotation()));
    	        instanceData.put(p.getR()).put(p.getG()).put(p.getB()).put(p.getA());
    	    }
    	    instanceData.flip();

    	    glUseProgram(shaderProgram);

	    int projLoc = glGetUniformLocation(shaderProgram, "projection");
	    var matBuffer = new float[16];
	    projection.get(matBuffer);
	    glUniformMatrix4fv(projLoc, false, matBuffer);

    	    glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
    	    glBufferSubData(GL_ARRAY_BUFFER, 0, instanceData);

    	    glEnable(GL_BLEND);
    	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    	    glBindVertexArray(vao);
    	    glDrawArraysInstanced(GL_TRIANGLES, 0, 6, particles.size());
    	}
}
