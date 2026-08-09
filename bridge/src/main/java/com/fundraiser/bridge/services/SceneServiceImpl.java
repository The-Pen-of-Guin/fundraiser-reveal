package com.fundraiser.bridge.services;

import java.nio.file.Path;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fundraiser.bridge.controllers.scene.SetBackgroundColorRequest;
import com.fundraiser.bridge.controllers.scene.SetTextColorRequest;
import com.fundraiser.bridge.controllers.scene.SetTextFontRequest;
import com.fundraiser.bridge.models.scene.Color;

@Service
public class SceneServiceImpl implements SceneService {
	private Color backgroundColor;
	private Color textColor;
	private Path fontPath;

	@Override
	public void setBackgroundColor(SetBackgroundColorRequest request) {
		backgroundColor = new Color(request.r(), request.g(), request.b());
	}

	@Override
	public Optional<Color> getBackgroundColor() {
		return Optional.ofNullable(backgroundColor);
	}

	@Override
	public void setTextColor(SetTextColorRequest request) {
		textColor = new Color(request.r(), request.g(), request.b());
	}

	@Override
	public Optional<Color> getTextColor() {
		return Optional.ofNullable(textColor);
	}

	@Override
	public void setTextFont(SetTextFontRequest request) {
		fontPath = Path.of(request.fontPath());
	}

	@Override
	public Optional<Path> getTextFont() {
		return Optional.ofNullable(fontPath);
	}
}
