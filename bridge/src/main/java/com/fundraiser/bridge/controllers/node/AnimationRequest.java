package com.fundraiser.bridge.controllers.node;

import java.util.List;

public record AnimationRequest(
	String animationType,
	Integer startDelayMs,
	Integer durationMs,
	List<String> additionalProperties
){}
