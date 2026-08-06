package com.fundraiser.bridge.datamapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.fundraiser.animation.nodes.Animation;
import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.animation.nodes.CountupAnimation;
import com.fundraiser.animation.nodes.ScrambleAnimation;
import com.fundraiser.animation.nodes.SetAnimation;
import com.fundraiser.bridge.controllers.node.AddNodeRequest;

@Mapper
public interface AddNodeRequestMapper {
	public static final AddNodeRequestMapper INSTANCE = Mappers.getMapper(AddNodeRequestMapper.class);

	default AnimationNode addRequestToAnimationNode(AddNodeRequest request) {
		Animation animation;
		switch (request.animation().animationType()) {
			case "Set" -> animation = new SetAnimation(request.animation().startDelayMs()); 
			case "Countup" -> animation = new CountupAnimation(request.animation().startDelayMs(), request.animation().durationMs()); 
			case "Scramble" -> animation = new ScrambleAnimation(request.animation().startDelayMs(), request.animation().durationMs()); 
			default -> throw new IllegalArgumentException(request.animation().animationType() + " is not an accepted animation type.");
		}

		return new AnimationNode(request.targetAmountCents(), animation);
	};
}
