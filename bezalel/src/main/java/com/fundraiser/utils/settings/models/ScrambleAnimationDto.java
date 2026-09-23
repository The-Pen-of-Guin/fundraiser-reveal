package com.fundraiser.utils.settings.models;

public class ScrambleAnimationDto extends AnimationDto{
	public ScrambleAnimationDto() {}

	public ScrambleAnimationDto(int startDelayMs, int durationMs) {
		super("Scramble", startDelayMs, durationMs);
	}
}
