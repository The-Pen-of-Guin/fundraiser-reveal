package com.fundraiser.datamapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

import com.fundraiser.utils.settings.models.AnimationDto;
import com.fundraiser.utils.settings.models.AnimationNodeDto;
import com.fundraiser.animation.nodes.Animation;
import com.fundraiser.animation.nodes.AnimationNode;

@Mapper
public interface AnimationNodeMapper {
	public static final AnimationNodeMapper INSTANCE = Mappers.getMapper(AnimationNodeMapper.class);

	AnimationNodeDto nodeToDto(AnimationNode node);
	
	AnimationDto toDto(Animation animation);
}
