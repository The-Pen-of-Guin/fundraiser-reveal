package com.fundraiser.bridge.controllers.node;

public record AddNodeRequest(
	Integer targetAmountCents,
	AnimationRequest animation	
){}
