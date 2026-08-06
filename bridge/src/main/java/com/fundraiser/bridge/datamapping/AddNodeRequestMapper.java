package com.fundraiser.bridge.datamapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.bridge.controllers.node.AddNodeRequest;

@Mapper
public interface AddNodeRequestMapper {
	public static final AddNodeRequestMapper INSTANCE = Mappers.getMapper(AddNodeRequestMapper.class);

	AnimationNode addRequestToAnimationNode(AddNodeRequest request);
}
