package com.fundraiser.utils.settings.models;

public class CountupAnimationDto extends AnimationDto {
	public CountupAnimationDto() {}

	public CountupAnimationDto(int startDelayMs, int durationMs) {
		super("Countup", startDelayMs, durationMs);
	}
}
