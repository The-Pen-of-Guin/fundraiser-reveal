package com.fundraiser.utils.settings.models;

public class AnimationDto {
	private String animationType;
	private int startDelayMs;
	private int durationMs;

	public AnimationDto() {}

	public AnimationDto(String animationType, int startDelayMs, int durationMs) {
		this.animationType = animationType;
		this.startDelayMs = startDelayMs;
		this.durationMs = durationMs;
	}

	public int getStartDelayMs() {
		return startDelayMs;
	}

	public void setStartDelayMs(int startDelayMs) {
		this.startDelayMs = startDelayMs;
	}

	public int getDurationMs() {
		return durationMs;
	}

	public void setDurationMs(int durationMs) {
		this.durationMs = durationMs;
	}

	public String getAnimationType() {
		return animationType;
	}

	public void setAnimationType(String animationType) {
		this.animationType = animationType;
	}
}
