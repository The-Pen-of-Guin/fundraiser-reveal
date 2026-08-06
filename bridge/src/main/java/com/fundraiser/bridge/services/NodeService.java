package com.fundraiser.bridge.services;

import com.fundraiser.bridge.controllers.node.AddNodeRequest;

public interface NodeService {
	void appendNode(AddNodeRequest request);

	void addNode(Integer position, AddNodeRequest request);

	void deleteNode(Integer position);
}
