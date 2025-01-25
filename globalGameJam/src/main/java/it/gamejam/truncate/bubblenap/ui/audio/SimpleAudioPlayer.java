package it.gamejam.truncate.bubblenap.ui.audio;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SimpleAudioPlayer {

	public static void playSyncSoundOnce(final byte[] bytes, final float gain) {
		final AtomicReference<Clip> ref = new AtomicReference<Clip>(null);
		try {
			final CountDownLatch syncLatch = new CountDownLatch(1);
			try (AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes))) {

				final Clip clip = AudioSystem.getClip();
				ref.set(clip);
				// Listener which allow method return once sound is completed
				clip.addLineListener(e -> {
					if (e.getType() == LineEvent.Type.STOP) {
						syncLatch.countDown();
					}
				});
				clip.open(stream);
				try {
					final FloatControl control = (FloatControl) clip.getControl(Type.MASTER_GAIN);
					control.setValue(gain);
				} catch (final Exception e) {
					System.err.println("control not supported");
				}
				clip.start();
			}
			syncLatch.await();
			ref.get().close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final byte[] fileAudio;
	// to store current position
	private Long currentFrame;

	private final Clip clip;

	// current status of clip
	private String status;

	private AudioInputStream audioInputStream;

	private final float gain;
	private BufferedImage image;

	/**
	 * constructor to initialize streams and clip
	 * 
	 * @param fileAudio
	 * @param gain
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public SimpleAudioPlayer(final byte[] fileAudio, final float gain)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.fileAudio = fileAudio;
		this.gain = gain;
		// create AudioInputStream object
		audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(fileAudio));

		// final Sample s = new Sample(audioInputStream);
		// final Generator g = new Generator();
		// image = g.generateImage(s, new WaveformOption());

		// create clip reference
		clip = AudioSystem.getClip();

		// open audioInputStream to the clip
		clip.open(audioInputStream);

		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Work as the user enters his choice
	 * 
	 * @param c
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public void gotoChoice(final int c) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		switch (c) {
		case 1:
			pause();
			break;
		case 2:
			resumeAudio();
			break;
		case 3:
			restart();
			break;
		case 4:
			stop();
			break;
		case 5:
			System.out.println("Enter time (" + 0 + ", " + clip.getMicrosecondLength() + ")");
			final Scanner sc = new Scanner(System.in);
			final long c1 = sc.nextLong();
			jump(c1);
			break;

		}

	}

	/**
	 * Method to jump over a specific part
	 * 
	 * @param c
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void jump(final long c) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (c > 0 && c < clip.getMicrosecondLength()) {
			clip.stop();
			clip.close();
			resetAudioStream();
			currentFrame = c;
			clip.setMicrosecondPosition(c);
			this.playLoop();
		}
	}

	/**
	 * Method to pause the audio
	 */
	public void pause() {
		if (status.equals("paused")) {
			System.out.println("audio is already paused");
			return;
		}
		this.currentFrame = this.clip.getMicrosecondPosition();
		clip.stop();
		status = "paused";
	}

	/**
	 * Method to play the audio
	 */
	public void playLoop() {
		// start the clip
		setGain(gain);
		clip.start();
		status = "play";
	}

	/**
	 * Method to reset audio stream
	 * 
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(fileAudio));
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Method to restart the audio
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public void restart() {
		clip.stop();
		clip.close();
		try {
			resetAudioStream();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		currentFrame = 0L;
		clip.setMicrosecondPosition(0);
		this.playLoop();
	}

	/**
	 * Method to resume the audio
	 */
	public void resumeAudio() {
		if (status.equals("play")) {
			System.out.println("Audio is already " + "being played");
			return;
		}
		clip.close();
		try {
			resetAudioStream();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		clip.setMicrosecondPosition(currentFrame);
		this.playLoop();
	}

	public void setGain(final float gain) {
		try {
			final FloatControl control = (FloatControl) clip.getControl(Type.MASTER_GAIN);
			control.setValue(gain);
		} catch (final Exception e) {
			System.err.println("control not supported");
		}
	}

	/**
	 * Method to stop the audio
	 * 
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void stop() {
		try {
			currentFrame = 0L;
			clip.stop();
			clip.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}