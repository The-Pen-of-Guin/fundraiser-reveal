package com.fundraiser.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fundraiser.bridge.util.GlfwDispatcher;

@SpringBootApplication
public class BridgeApplication {
	public static void main(String[] args) {
		new Thread(() -> SpringApplication.run(BridgeApplication.class, args)).start();

		while (true) {
			Runnable task;
			while ((task = GlfwDispatcher.queue.poll()) != null) {
				task.run();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Sleep interuppted!");
			}
		}
	}
}
