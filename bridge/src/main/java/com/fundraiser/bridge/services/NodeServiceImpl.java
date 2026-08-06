package com.fundraiser.bridge.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.bridge.controllers.node.AddNodeRequest;
import com.fundraiser.bridge.datamapping.AddNodeRequestMapper;

@Service
public class NodeServiceImpl implements NodeService {
	private List<AnimationNode> nodes = new ArrayList<>();

	@Override
	public void appendNode(AddNodeRequest request) {
		var node = AddNodeRequestMapper.INSTANCE.addRequestToAnimationNode(request);
		nodes.add(node);
	}

	@Override
	public void addNode(Integer position, AddNodeRequest request) {
		var node = AddNodeRequestMapper.INSTANCE.addRequestToAnimationNode(request);
		nodes.add(position, node);
	}

	@Override
	public void deleteNode(Integer position) {
		if (position == null) {
			throw new IllegalArgumentException("Position cannot be null.");
		}

		nodes.remove((int)position);
	}

	@Override
	public List<AnimationNode> getNodes() {
		return nodes;
	}
}
