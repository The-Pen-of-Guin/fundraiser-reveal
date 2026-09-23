package com.fundraiser.bridge.services;

import java.util.Optional;

import com.fundraiser.bridge.controllers.scene.SetBackgroundColorRequest;
import com.fundraiser.bridge.controllers.scene.SetTextColorRequest;
import com.fundraiser.bridge.controllers.scene.SetTextFontRequest;
import com.fundraiser.bridge.controllers.scene.SetTextFontSizeRequest;
import com.fundraiser.bridge.models.scene.Color;

public interface SceneService {
	void setBackgroundColor(SetBackgroundColorRequest request);

	void setTextColor(SetTextColorRequest request);

	void setTextFont(SetTextFontRequest request);

	void setTextFontSize(SetTextFontSizeRequest request);

	Optional<Color> getBackgroundColor();

	Optional<Color> getTextColor();

	Optional<String> getTextFont();

	Optional<Integer> getTextFontSize();
}
