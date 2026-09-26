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
				.writeValue(getSettingsFile(), settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Settings load() {
		try {
			var settings = objectMapper.readValue(getSettingsFile(), Settings.class);
			return settings;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load from settings file: ", e);
		}
	}

	private static File getSettingsFile() {
	    String os = System.getProperty("os.name").toLowerCase();
	    String userHome = System.getProperty("user.home");
	    File dir;
	
	    if (os.contains("mac")) {
	        dir = new File(userHome, "Library/Application Support/MyBridgeApp");
	    } else if (os.contains("win")) {
	        dir = new File(System.getenv("APPDATA"), "MyBridgeApp");
	    } else {
	        dir = new File(userHome, ".config/MyBridgeApp"); // Linux convention
	    }
	
	    dir.mkdirs();
	    return new File(dir, "settings.json");
	}
}
