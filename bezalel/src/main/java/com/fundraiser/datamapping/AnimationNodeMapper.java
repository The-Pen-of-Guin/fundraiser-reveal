package com.fundraiser.datamapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

import com.fundraiser.utils.settings.models.AnimationDto;
import com.fundraiser.utils.settings.models.AnimationNodeDto;
import com.fundraiser.utils.settings.models.CountupAnimationDto;
import com.fundraiser.utils.settings.models.ScrambleAnimationDto;
import com.fundraiser.utils.settings.models.SetAnimationDto;
import com.fundraiser.animation.nodes.Animation;
import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.animation.nodes.CountupAnimation;
import com.fundraiser.animation.nodes.ScrambleAnimation;
import com.fundraiser.animation.nodes.SetAnimation;

@Mapper
public interface AnimationNodeMapper {
	public static final AnimationNodeMapper INSTANCE = Mappers.getMapper(AnimationNodeMapper.class);

	AnimationNodeDto nodeToDto(AnimationNode node);
	
	default AnimationDto toDto(Animation animation) {
		return switch (animation) {
			case SetAnimation set -> toDto(set);
			case ScrambleAnimation scramble -> toDto(scramble);
			case CountupAnimation countup -> toDto(countup);
			default -> throw new IllegalArgumentException("Animations of type: " + animation.getAnimationType() + " are not supported");
		};
	}

	SetAnimationDto toDto(SetAnimation animation);
	ScrambleAnimationDto toDto(ScrambleAnimation animation);
	CountupAnimationDto toDto(CountupAnimation animation);


	AnimationNode dtoToNode(AnimationNodeDto dto);
	
	default Animation toAnimation(AnimationDto dto) {
		return switch (dto.getAnimationType()) {
			case "Set" -> toAnimation(new SetAnimationDto(dto.getStartDelayMs()));
			case "Scramble" -> toAnimation(new ScrambleAnimationDto(dto.getStartDelayMs(), dto.getDurationMs()));
			case "Countup" -> toAnimation(new CountupAnimationDto(dto.getStartDelayMs(), dto.getDurationMs()));
			default -> throw new IllegalArgumentException("Animations of type: " + dto.getAnimationType() + " are not supported");
		};
	}

	SetAnimation toAnimation(SetAnimationDto dto);
	ScrambleAnimation toAnimation(ScrambleAnimationDto dto);
	CountupAnimation toAnimation(CountupAnimationDto dto);
}
