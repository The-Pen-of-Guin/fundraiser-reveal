package com.fundraiser.bridge.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fundraiser.bridge.controllers.node.AppendNodeRequest;

@RestController
@RequestMapping("/api/v1/node")
public class NodeController {
	@PostMapping("/append")
	public ResponseEntity<String> append(
		@RequestBody AppendNodeRequest request
	) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}

	@PostMapping("/add/{position}")
	public ResponseEntity<String> add(
		@PathVariable Integer position,
		@RequestBody AppendNodeRequest request
	) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}

	@DeleteMapping("/{position}")
	public ResponseEntity<String> delete(
		@PathVariable Integer position
	) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
}
