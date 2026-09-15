package com.fundraiser.bridge.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fundraiser.bridge.controllers.scene.SetBackgroundColorRequest;
import com.fundraiser.bridge.controllers.scene.SetTextColorRequest;
import com.fundraiser.bridge.controllers.scene.SetTextFontRequest;
import com.fundraiser.bridge.models.scene.Color;

@Service
public class SceneServiceImpl implements SceneService {
	private Color backgroundColor = new Color((short)0, (short)0, (short)0);
	private Color textColor = new Color((short)255, (short)255, (short)255);
	private String fontName = "Roboto";

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
		fontName = request.font();
	}

	@Override
	public Optional<String> getTextFont() {
		return Optional.ofNullable(fontName);
	}
}
