package com.fundraiser.bridge.util;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class GlfwDispatcher {
	public static final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();

    	public static <T> T runAndWait(Supplier<T> task) {
    	    var future = new CompletableFuture<T>();
    	    queue.add(() -> {
    	        try {
    	            future.complete(task.get());
    	        } catch (Exception e) {
    	            future.completeExceptionally(e);
    	        }
    	    });
    	    return future.join(); // blocks the calling thread until main thread finishes
    	}
}
