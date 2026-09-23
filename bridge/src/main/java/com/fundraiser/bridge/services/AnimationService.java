package com.fundraiser.bridge.services;

import com.fundraiser.utils.settings.models.Settings;

public interface AnimationService {
	void playAnimation();

	void saveAnimation();	

	void clearAnimation();

	void saveSettings();

	Settings loadSettings();
}
