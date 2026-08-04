package com.fundraiser.animation.nodes;

public class Animation {
	private int startDelayMs;
	private int durationMs;

	public Animation(int startDelayMs, int durationMs) {
		this.startDelayMs = startDelayMs;
		this.durationMs = durationMs;
	}

	public int getStartDelayMs() {
		return startDelayMs;
	}

	public int getDurationMs() {
		return durationMs;
	}
}
