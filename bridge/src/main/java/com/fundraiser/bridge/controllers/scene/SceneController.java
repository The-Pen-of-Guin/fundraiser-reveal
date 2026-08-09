package com.fundraiser.bridge.controllers.scene;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundraiser.bridge.services.SceneService;

@RestController
@RequestMapping("/api/v1/scene")
public class SceneController {
	private final SceneService sceneService;

	public SceneController(SceneService sceneService) {
		this.sceneService = sceneService;
	}

	@PostMapping("/background/color")
	public ResponseEntity<String> setBackgroundColor(@RequestBody SetBackgroundColorRequest request) {
		sceneService.setBackgroundColor(request);
		return ResponseEntity.ok("Success!");
	}

	@PostMapping("/text/color")
	public ResponseEntity<String> setTextColor(@RequestBody SetTextColorRequest request) {
		sceneService.setTextColor(request);
		return ResponseEntity.ok("Success!");
	}

	@PostMapping("/text/font")
	public ResponseEntity<String> setTextFont(@RequestBody SetTextFontRequest request) {
		sceneService.setTextFont(request);
		return ResponseEntity.ok("Success!");
	}
}
