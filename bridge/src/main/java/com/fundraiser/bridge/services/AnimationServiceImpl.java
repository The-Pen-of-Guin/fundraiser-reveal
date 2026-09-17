package com.fundraiser.bridge.services;

import java.nio.file.Path;
import java.util.ArrayDeque;

import org.springframework.stereotype.Service;

import com.fundraiser.animation.Animator;
import com.fundraiser.animation.nodes.AnimationNode;

@Service
public class AnimationServiceImpl implements AnimationService {
	private final Animator animator = new Animator();

	private final NodeService nodeService;
	private final SceneService sceneService;

	public AnimationServiceImpl(NodeService nodeService, SceneService sceneService) {
		this.nodeService = nodeService;
		this.sceneService = sceneService;
	}

	@Override
	public void playAnimation() {
		setupAnimation();

		new Thread(() -> {
			animator.run();
		}).start();
	}

	@Override
	public void saveAnimation() {
		setupAnimation();

		int durationMs = 0;
		for (AnimationNode node : nodeService.getNodes()) {
			durationMs = durationMs + node.animation().getDurationMs() + node.animation().getStartDelayMs();
		}

		final int durationSeconds = durationMs/1000;
		new Thread(() -> {
			animator.run(Path.of("./output.mp4"), 60, durationSeconds);
		}).start();
	}

	@Override
	public void clearAnimation() {
		nodeService.clearNodes();
	}

	private void setupAnimation() {
		var nodes = nodeService.getNodes();
		animator.setAnimationNodes(new ArrayDeque<>(nodes));
		
		var backgroundColor = sceneService.getBackgroundColor().orElseThrow(() -> new RuntimeException("Background color has not been set."));
		animator.setBackgroundColor(backgroundColor.r()/255f, backgroundColor.g()/255f, backgroundColor.b()/255f);

		var textColor = sceneService.getTextColor().orElseThrow(() -> new RuntimeException("Text color has not been set."));
		animator.setTextColor(textColor.r()/255f, textColor.g()/255, textColor.b()/255f);

		var textFont = sceneService.getTextFont().orElseThrow(() -> new RuntimeException("Text font has not been set."));
		animator.setTextFont(textFont);
	}
}
