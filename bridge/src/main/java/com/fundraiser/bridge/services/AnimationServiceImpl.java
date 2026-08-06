package com.fundraiser.bridge.services;

import java.util.ArrayDeque;

import org.springframework.stereotype.Service;

import com.fundraiser.animation.Animator;

@Service
public class AnimationServiceImpl implements AnimationService {
	private final Animator animator = new Animator();

	private final NodeService nodeService;

	public AnimationServiceImpl(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	@Override
	public void playAnimation() {
		var nodes = nodeService.getNodes();
		animator.setAnimationNodes(new ArrayDeque<>(nodes));

		new Thread(() -> {
			animator.run();
		}).start();
	}

	@Override
	public void saveAnimation() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'saveAnimation'");
	}
}
