package com.fundraiser.utils.settings;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundraiser.utils.settings.models.Settings;

public class SettingsSaver {
	private static ObjectMapper objectMapper;

	public static void save(Settings settings) {
		try {
			objectMapper.writerWithDefaultPrettyPrinter()
				.writeValue(new File("bezalel-settings.json"), settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
