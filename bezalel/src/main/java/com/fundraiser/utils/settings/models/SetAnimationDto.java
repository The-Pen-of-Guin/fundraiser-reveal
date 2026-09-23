package com.fundraiser.utils.settings.models;

public class SetAnimationDto extends AnimationDto {
	public SetAnimationDto() {}

	public SetAnimationDto(int startDelayMs) {
		super("Set", startDelayMs, 0);
	}
}
