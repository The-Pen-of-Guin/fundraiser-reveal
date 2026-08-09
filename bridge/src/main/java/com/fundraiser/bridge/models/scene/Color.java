package com.fundraiser.bridge.models.scene;

public record Color(short r, short g, short b) {
	public Color {
		colorChannelCheck(r, "r");
		colorChannelCheck(g, "g");
		colorChannelCheck(b, "b");
	}

	private void colorChannelCheck(short channel, String channelName) {
		if (channel > 255) {
			throw new IllegalArgumentException(String.format("Invalid argument for Color: %s cannot be greater than 255.", channelName));
		} else if (channel < 0) {
			throw new IllegalArgumentException(String.format("Invalid argument for Color: %s cannot be negative.", channelName));
		}
	}
}
