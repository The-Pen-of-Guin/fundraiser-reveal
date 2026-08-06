package com.fundraiser.bridge.controllers.animation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/api/v1/animation")
public class AnimationController {
	@GetMapping("/play")
	public EntityResponse<String> play() {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}

	@PostMapping("/save")
	public EntityResponse<String> save(
		@RequestBody SaveAnimationRequest request
	) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
}
