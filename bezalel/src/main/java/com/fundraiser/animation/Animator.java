package com.fundraiser.animation;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import com.fundraiser.animation.nodes.Animation;
import com.fundraiser.animation.nodes.AnimationNode;
import com.fundraiser.animation.nodes.CountupAnimation;
import com.fundraiser.animation.nodes.ScrambleAnimation;
import com.fundraiser.animation.nodes.SetAnimation;
import com.fundraiser.datamapping.AnimationNodeMapper;
import com.fundraiser.utils.settings.SettingsSaver;
import com.fundraiser.utils.settings.models.ColorSettings;
import com.fundraiser.utils.settings.models.NodeSettings;
import com.fundraiser.utils.settings.models.Settings;
import com.fundraiser.utils.settings.models.TextSettings;

public class Animator {
	private int currentAmountCents = 0;

	private float[] bgColor = {0.0f, 0.0f, 0.0f};
	private float[] textColor = {1.0f, 1.0f, 1.0f};
	private String font = "Roboto";
	private int fontSize = 200;

	private Queue<AnimationNode> animationNodes = new ArrayDeque<>();

	private AnimationEngine animationEngine = new AnimationEngine();

	public void run() {
		run(null, 0, 0);
	}

	public void run(Path outputFile, int fps, int durationSeconds) {
		animationEngine.setBgColor(bgColor[0], bgColor[1], bgColor[2]);
		animationEngine.setTextColor(textColor[0], textColor[1], textColor[2]);
		animationEngine.setFont(font);
		animationEngine.setFontSize(fontSize);

		// Reset amount when starting. This resets previous runs.
		currentAmountCents = 0;
		animationEngine.setText(centsToStringDollars(currentAmountCents));

		new Thread(() -> {
			if (outputFile == null)
				animationEngine.run();
			else
				animationEngine.run(outputFile, fps, durationSeconds);	
		}).start();

		processAnimationNodes(animationNodes);

		var startTime = Instant.now();

		while (Duration.between(startTime, Instant.now()).toMillis() <= 3000);

		animationEngine.setUseConfetti(true);

		while (Duration.between(startTime, Instant.now()).toMillis() <= 25000);

		animationEngine.setUseConfetti(false);
	}

	public void saveSettings() {
		var colorSettings = new ColorSettings(bgColor, textColor);
		var textSettings = new TextSettings(font, fontSize);
		var nodeSettings = new NodeSettings(animationNodes.stream()
			.map(node -> AnimationNodeMapper.INSTANCE.nodeToDto(node)).toList());
		var settings = new Settings(colorSettings, textSettings, nodeSettings, null);
		SettingsSaver.save(settings);
	}

	public Settings loadSettings() {
		Settings settings = SettingsSaver.load();

		var bgColor = settings.colorSettings().backgroundColor();
		setBackgroundColor(bgColor[0], bgColor[1], bgColor[2]);

		var textColor = settings.colorSettings().textColor();
		setTextColor(textColor[0], textColor[1], textColor[2]);

		var font = settings.textSettings().font();
		setTextFont(font);

		var fontSize = settings.textSettings().fontSize();
		setTextFontSize(fontSize);

		List<AnimationNode> nodes = settings.nodeSettings().nodes().stream().map(dto -> AnimationNodeMapper.INSTANCE.dtoToNode(dto)).toList();
		setAnimationNodes(new ArrayDeque<>(nodes));

		return settings;
	}

	public void setAnimationNodes(Queue<AnimationNode> animationNodes) {
		this.animationNodes = animationNodes;
	}

	public void setBackgroundColor(float r, float g, float b) {
		try {
			colorCheck(r, g, b);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Failed to set background color: " + e);
		}

		bgColor = new float[] {r, g, b};
	}

	public void setTextColor(float r, float g, float b) {
		try {
			colorCheck(r, g, b);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Failed to set text color: " + e);
		}

		textColor = new float[] {r, g, b};
	}

	public void setTextFont(String font) {
		this.font = font;
	}

	public void setTextFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	private void colorCheck(float r, float g, float b) {
		if (r < 0 | r > 1.0)
			throw new IllegalArgumentException("r must be in range 0.0 to 1.0 inclusive");
		if (g < 0 | g > 1.0)
			throw new IllegalArgumentException("r must be in range 0.0 to 1.0 inclusive");
		if (b < 0 | b > 1.0)
			throw new IllegalArgumentException("r must be in range 0.0 to 1.0 inclusive");
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
			var randomNumber = ThreadLocalRandom.current().nextInt((int) Math.pow(10, (int) Math.log10(targetAmountCents)), (int) Math.pow(10, (int) Math.log10(targetAmountCents) + 1) - 1);

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
