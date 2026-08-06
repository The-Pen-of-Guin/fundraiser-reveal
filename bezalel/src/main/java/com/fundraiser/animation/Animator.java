package com.fundraiser.animation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import com.fundraiser.animation.nodes.Animation;
import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.animation.nodes.CountupAnimation;
import com.fundraiser.animation.nodes.ScrambleAnimation;
import com.fundraiser.animation.nodes.SetAnimation;

public class Animator {
	private int currentAmountCents = 0;

	private float[] bgColor = {1.0f, 0.55f, 0.0f};
	private float[] textColor = {0.5f, 0.0f, 0.5f};

	private Queue<AnimationNode> animationNodes = new ArrayDeque<>();

	private AnimationEngine animationEngine = new AnimationEngine();

	public void setAnimationNodes(Queue<AnimationNode> animationNodes) {
		this.animationNodes = animationNodes;
	}

	public void run() {
		animationEngine.setBgColor(bgColor[0], bgColor[1], bgColor[2]);
		animationEngine.setTextColor(textColor[0], textColor[1], textColor[2]);

		new Thread(() -> {
			animationEngine.run();
		}).start();

		processAnimationNodes(animationNodes);
	}

	private void processAnimationNodes(Queue<AnimationNode> animationNodes) {
		for (AnimationNode node : animationNodes) {
			switch (node.animation()) {
				case SetAnimation setAnimation -> processSetAnimation(node.targetAmountCents(), setAnimation);
				case CountupAnimation countupAnimation -> processCountupAnimation(node.targetAmountCents(), countupAnimation);
				case ScrambleAnimation scrambleAnimation -> processScrambleAnimation(node.targetAmountCents(), scrambleAnimation);
				default -> System.out.println("ERROR: Animation type " + node.animation().getClass().getName() + " unknown");
			}
		}
	}

	private void processSetAnimation(int targetAmountCents, SetAnimation setAnimation) {
		waitForDelayTime(setAnimation);

		animationEngine.setText(centsToStringDollars(targetAmountCents));

		currentAmountCents = targetAmountCents;
	}

	private void processCountupAnimation(int targetAmountCents, CountupAnimation countupAnimation) {
		waitForDelayTime(countupAnimation);

		var startTime = Instant.now();
		var deltaCents = targetAmountCents - currentAmountCents;
		
		while (Duration.between(startTime, Instant.now()).toMillis() <= countupAnimation.getDurationMs()) {
			var ratio = ((double) Duration.between(startTime, Instant.now()).toMillis()) / ((double) countupAnimation.getDurationMs());
			var newAmount = (int)(currentAmountCents + deltaCents * ratio);
			animationEngine.setText(centsToStringDollars(newAmount));
		}
		
		// Set to targetAmountCents in case rounding errors didn't play nice.
		animationEngine.setText(centsToStringDollars(targetAmountCents));
		currentAmountCents = targetAmountCents;
	}

	private void processScrambleAnimation(int targetAmountCents, ScrambleAnimation scrambleAnimation) {
		waitForDelayTime(scrambleAnimation);

		var startTime = Instant.now();
		while(Duration.between(startTime, Instant.now()).toMillis() <=scrambleAnimation.getDurationMs()) {
			try {
				Thread.sleep(scrambleAnimation.getTimeBetweenNumbersMs());
			} catch (InterruptedException ex) {
			}
			var randomNumber = ThreadLocalRandom.current().nextInt(0, 1000000);

			// NOTE: Adjust formatting a little more
			animationEngine.setText(String.format("%6s", centsToStringDollars(randomNumber)).replace(' ', '0'));
		}

		animationEngine.setText(centsToStringDollars(targetAmountCents));
		currentAmountCents = targetAmountCents;
	}

	private void waitForDelayTime(Animation animation) {
		var startTime = Instant.now();

		// Kill execution time until the startDelay ends.
		while (Duration.between(startTime, Instant.now()).toMillis() <= animation.getStartDelayMs());
	}

	private String centsToStringDollars(int cents) {
		return String.format("$%,.2f", cents / 100.0);
	}
}
