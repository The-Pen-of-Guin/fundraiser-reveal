package com.fundraiser.utils.settings;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundraiser.utils.settings.models.Settings;

public class SettingsSaver {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static void save(Settings settings) {
		try {
			objectMapper.writerWithDefaultPrettyPrinter()
				.writeValue(new File("bezalel-settings.json"), settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Settings load() {
		try {
			var settings = objectMapper.readValue(new File("bezalel-settings.json"), Settings.class);
			return settings;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load from settings file: ", e);
		}
	}
}
