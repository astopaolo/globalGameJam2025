package globalGameJam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;

import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class VoiceCircleGame extends JPanel {
	public static void main(final String[] args) {
		JFrame frame = new JFrame("Voice Circle Game");
		VoiceCircleGame game = new VoiceCircleGame();

		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private float circleY = 250; // Initial vertical position of the circle
	private final int circleRadius = 20;
	private final int windowWidth = 500;

	private final int windowHeight = 500;

	public VoiceCircleGame() {
		setPreferredSize(new Dimension(windowWidth, windowHeight));
		setBackground(Color.BLACK);

		// Start audio processing for pitch detection
		new Thread(this::startPitchDetection).start();
	}

	private void startPitchDetection() {
		try {
			AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
			TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
			microphone.open(format);
			microphone.start();

			TarsosDSPAudioFormat tarsosFormat = new TarsosDSPAudioFormat(format.getSampleRate(),
					format.getSampleSizeInBits(), format.getChannels(), format.isBigEndian(), format.isBigEndian());
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
		repaint();
	}

	@Override
	protected synchronized void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillOval((windowWidth / 2) - circleRadius, (int) circleY - circleRadius, circleRadius * 2, circleRadius * 2);
	}
}