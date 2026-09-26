package com.fundraiser.bridge.util;

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
		animator.run(Path.of("./output.mp4"), 60, durationSeconds);
	}

	public static void saveSettings() {
		animator.saveSettings();
	}

	public static Settings loadSettings() {
		return GlfwDispatcher.runAndWait(() -> animator.loadSettings());
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
}
