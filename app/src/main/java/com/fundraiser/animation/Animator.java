package com.fundraiser.animation;

public class Animator {
	private int currentAmountCents = 0;

	private float[] bgColor = {1.0f, 0.55f, 0.0f};
	private float[] textColor = {0.5f, 0.0f, 0.5f};

	private AnimationEngine animationEngine = new AnimationEngine();

	public void run() {
		animationEngine.setBgColor(bgColor[0], bgColor[1], bgColor[2]);
		animationEngine.setTextColor(textColor[0], textColor[1], textColor[2]);

		new Thread(() -> {
			animationEngine.run();
		}).start();

		while (true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException ex) {
				System.out.println("sleep interrupted");
			}

			currentAmountCents += 100;	
			animationEngine.setText("$" + currentAmountCents/100);
		}
	}
}
