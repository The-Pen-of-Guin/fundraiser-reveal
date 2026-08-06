package com.fundraiser.bridge.controllers.animation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundraiser.bridge.services.AnimationService;

@RestController
@RequestMapping("/api/v1/animation")
public class AnimationController {
	private final AnimationService animationService;

	public AnimationController(
		AnimationService animationService
	) {
		this.animationService = animationService;
	}

	@GetMapping("/play")
	public ResponseEntity<String> play() {
		animationService.playAnimation();
		return ResponseEntity.ok("Success!");
	}

	@PostMapping("/save")
	public ResponseEntity<String> save(
		@RequestBody SaveAnimationRequest request
	) {
		animationService.saveAnimation();
		return ResponseEntity.ok("Success!");
	}
}
