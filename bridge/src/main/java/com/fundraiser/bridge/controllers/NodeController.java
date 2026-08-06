package com.fundraiser.bridge.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundraiser.bridge.controllers.node.AddNodeRequest;
import com.fundraiser.bridge.services.NodeService;

@RestController
@RequestMapping("/api/v1/node")
public class NodeController {
	private final NodeService nodeService;

	public NodeController(
		NodeService nodeService
	) {
		this.nodeService = nodeService;
	}

	@PostMapping("/append")
	public ResponseEntity<String> append(
		@RequestBody AddNodeRequest request
	) {
		nodeService.appendNode(request);
		return ResponseEntity.ok("Success!");
	}

	@PostMapping("/add/{position}")
	public ResponseEntity<String> add(
		@PathVariable Integer position,
		@RequestBody AddNodeRequest request
	) {
		nodeService.addNode(position, request);
		return ResponseEntity.ok("Success!");
	}

	@DeleteMapping("/{position}")
	public ResponseEntity<String> delete(
		@PathVariable Integer position
	) {
		nodeService.deleteNode(position);
		return ResponseEntity.ok("Success!");
	}
}
