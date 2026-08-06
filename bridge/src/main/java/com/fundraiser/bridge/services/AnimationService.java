package com.fundraiser.bridge.services;

import com.fundraiser.bridge.controllers.node.AddNodeRequest;

public interface AnimationService {
	void appendNode(AddNodeRequest request);

	void addNode(Integer position, AddNodeRequest request);	
}
