package globalGameJam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class VoiceCircleGame extends JPanel {
	// Inner class for Enemy
	private static class Enemy {
		int x, y, size;
		private final int speed;

		public Enemy(final int startX, final int startY) {
			this.x = startX;
			this.y = startY;
			this.size = 30;
			this.speed = (int) (Math.random() * 2); // Random speed between 5 and 10
		}

		public void move() {
			x -= speed;
		}
	}

	public static void main(final String[] args) {
		JFrame frame = new JFrame("Voice Circle Game");
		VoiceCircleGame game = new VoiceCircleGame();

		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private final int windowWidth = 1500;

	private final int windowHeight = 500;
	private float circleY = windowHeight / 2; // Initial vertical position of the circle
	private final int circleRadius = 20;

	private final ArrayList<Enemy> enemies = new ArrayList<>();

	private final Random random = new Random();

	private boolean gameOver = false;

	public VoiceCircleGame() {
		setPreferredSize(new Dimension(windowWidth, windowHeight));
		setBackground(Color.BLACK);

		// Start audio processing for pitch detection
		new Thread(this::startPitchDetection).start();

		// Start enemy generation
		new Thread(this::generateEnemies).start();

		// Game loop to update enemy positions
		new Timer(16, e -> {
			if (!gameOver) {
				updateEnemies();
				checkCollisions();
				repaint();
			}
		}).start();
	}

	private synchronized void checkCollisions() {
		for (Enemy enemy : enemies) {
			if (Math.hypot((windowWidth / 2) - enemy.x, circleY - enemy.y) < (circleRadius + (enemy.size / 2.0))) {
				gameOver = true;
				repaint();
				break;
			}
		}
	}

	private void generateEnemies() {
		while (!gameOver) {
			try {
				Thread.sleep(random.nextInt(1000)); // Random delay between enemies
				enemies.add(new Enemy(windowWidth, random.nextInt(windowHeight - 40) + 20));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void startPitchDetection() {
		try {
			AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
			TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
			microphone.open(format);
			microphone.start();

			JVMAudioInputStream audioStream = new JVMAudioInputStream(new AudioInputStream(microphone));

			PitchDetectionHandler handler = (pitchDetectionResult, audioEvent) -> {
				float pitch = pitchDetectionResult.getPitch();
				if (pitch > 0) { // Ignore unvoiced sounds
					updateCirclePosition(pitch);
				}
			};

			AudioProcessor pitchProcessor = new PitchProcessor(PitchEstimationAlgorithm.YIN, 44100, 1024, handler);
			be.tarsos.dsp.AudioDispatcher dispatcher = new be.tarsos.dsp.AudioDispatcher(audioStream, 1024, 0);
			dispatcher.addAudioProcessor(pitchProcessor);

			dispatcher.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void updateCirclePosition(final float pitch) {
		if (pitch < 200) { // Low pitch moves the circle down
			circleY += 5;
		} else if (pitch > 400) { // High pitch moves the circle up
			circleY -= 5;
		}

		// Clamp the circle within the window boundaries
		circleY = Math.max(circleRadius, Math.min(windowHeight - circleRadius, circleY));
	}

	private synchronized void updateEnemies() {
		Iterator<Enemy> iterator = enemies.iterator();
		while (iterator.hasNext()) {
			Enemy enemy = iterator.next();
			enemy.move();
			if ((enemy.x + enemy.size) < 0) { // Remove enemies that go off screen
				iterator.remove();
			}
		}
	}

	@Override
	protected synchronized void paintComponent(final Graphics g) {
		super.paintComponent(g);

		if (gameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 36));
			g.drawString("Game Over", (windowWidth / 2) - 100, windowHeight / 2);
			return;
		}

		// Draw the circle
		g.setColor(Color.RED);
		g.fillOval((windowWidth / 2) - circleRadius, (int) circleY - circleRadius, circleRadius * 2, circleRadius * 2);

		// Draw enemies
		g.setColor(Color.GREEN);
		for (Enemy enemy : enemies) {
			g.fillOval(enemy.x - (enemy.size / 2), enemy.y - (enemy.size / 2), enemy.size, enemy.size);
		}
	}
}
