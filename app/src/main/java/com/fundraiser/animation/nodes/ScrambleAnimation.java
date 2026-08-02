package com.fundraiser.animation.nodes;

public class ScrambleAnimation extends Animation {
	private final static int TIME_BETWEEN_NUMBERS_MS = 35;

	public ScrambleAnimation(int startDelayMs, int durationMs) {
		super(startDelayMs, durationMs);
	}

	public int getTimeBetweenNumbersMs() {
		return TIME_BETWEEN_NUMBERS_MS;
	}
}
