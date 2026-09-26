package com.fundraiser.bridge.util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;

import com.fundraiser.animation.Animator;
import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.bridge.models.scene.Color;
import com.fundraiser.utils.settings.models.Settings;

public class AnimatorUtil {
	private static final Animator animator = new Animator();

	public static void playAnimation() {
		animator.run();
	}

	public static void saveAnimation(List<AnimationNode> nodes) {
		int durationMs = 0;
		for (AnimationNode node : nodes) {
			durationMs = durationMs + node.animation().getDurationMs() + node.animation().getStartDelayMs();
		}

		final int durationSeconds = durationMs/1000;
		animator.run(getVideoOutputFile().toPath(), 60, durationSeconds);
	}

	public static void saveSettings() {
		animator.saveSettings();
	}

	public static Settings loadSettings() {
		return animator.loadSettings();
	}

	public static void setupAnimation(
		List<AnimationNode> nodes,
		Color backgroundColor,
		Color textColor,
		String textFont,
		int fontSize
	) {
		animator.setAnimationNodes(new ArrayDeque<>(nodes));
		
		animator.setBackgroundColor(backgroundColor.r()/255f, backgroundColor.g()/255f, backgroundColor.b()/255f);

		animator.setTextColor(textColor.r()/255f, textColor.g()/255f, textColor.b()/255f);

		animator.setTextFont(textFont);

		animator.setTextFontSize(fontSize);
	}

	private static File getVideoOutputFile() {
	    String os = System.getProperty("os.name").toLowerCase();
	    String userHome = System.getProperty("user.home");
	    File dir;
	
	    if (os.contains("mac")) {
	        dir = new File(userHome, "Movies/MyBridgeApp");
	    } else if (os.contains("win")) {
	        dir = new File(System.getenv("USERPROFILE"), "Videos\\MyBridgeApp");
	    } else {
	        dir = new File(userHome, "Videos/MyBridgeApp"); // Linux (XDG convention)
	    }
	
	    dir.mkdirs();
	    return new File(dir, "gic-reveal.mp4");
	}
}
