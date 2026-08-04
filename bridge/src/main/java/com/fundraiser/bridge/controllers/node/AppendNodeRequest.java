package com.fundraiser.bridge.controllers.node;

public record AppendNodeRequest(
	Integer targetAmountCents,
	AnimationRequest animation	
){}
