package com.fundraiser.bridge.services;

import org.springframework.stereotype.Service;

import com.fundraiser.bridge.util.AnimatorUtil;
import com.fundraiser.bridge.util.GlfwDispatcher;
import com.fundraiser.utils.settings.models.Settings;

@Service
public class AnimationServiceImpl implements AnimationService {
	private final NodeService nodeService;
	private final SceneService sceneService;

	public AnimationServiceImpl(NodeService nodeService, SceneService sceneService) {
		this.nodeService = nodeService;
		this.sceneService = sceneService;
	}

	@Override
	public void playAnimation() {
		GlfwDispatcher.queue.add(() -> {
			setupAnimation();
			AnimatorUtil.playAnimation();
		});
	}

	@Override
	public void saveAnimation() {
		GlfwDispatcher.queue.add(() -> {
			setupAnimation();
			AnimatorUtil.saveAnimation(nodeService.getNodes());
		});
	}

	@Override
	public void clearAnimation() {
		nodeService.clearNodes();
	}

	@Override
	public void saveSettings() {
		GlfwDispatcher.queue.add(() -> {
			setupAnimation();
			AnimatorUtil.saveSettings();
		});
	}

	@Override
	public Settings loadSettings() {
		return GlfwDispatcher.runAndWait(() -> AnimatorUtil.loadSettings());
	}

	private void setupAnimation() {
		var nodes = nodeService.getNodes();
		
		var backgroundColor = sceneService.getBackgroundColor().orElseThrow(() -> new RuntimeException("Background color has not been set."));

		var textColor = sceneService.getTextColor().orElseThrow(() -> new RuntimeException("Text color has not been set."));

		var textFont = sceneService.getTextFont().orElseThrow(() -> new RuntimeException("Text font has not been set."));

		var fontSize = sceneService.getTextFontSize().orElseThrow(() -> new RuntimeException("Font size has not been set."));

		AnimatorUtil.setupAnimation(nodes, backgroundColor, textColor, textFont, fontSize);
	}
}
