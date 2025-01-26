package it.gamejam.truncate.bubblenap.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.Timer;

import it.gamejam.truncate.bubblenap.ui.audio.SimpleAudioPlayer;

public class VideoPanel extends JPanel {

	private static final long serialVersionUID = 5857392141854818722L;
	MainFrame frame;
	private List<Image> videoFrames;
	private int currentFrame;
	private EnumPanel nextPanelToDraw;
	private List<byte[]> musics;
	private Font bubbleFont;
	private Optional<String> message;

	public VideoPanel(final MainFrame frame, final List<Image> videoFrames, final EnumPanel nextPanelToDraw,
			final List<byte[]> musics, final Optional<String> message) {
		this.frame = frame;
		this.nextPanelToDraw = nextPanelToDraw;
		this.musics = musics;
		this.message = message;
		this.setLayout(null);
		this.videoFrames = videoFrames;
		currentFrame = 0;
		setPreferredSize(new Dimension(1280, 768));
		requestFocus();
		loadFont();
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);

		if (currentFrame < videoFrames.size()) {
			g.drawImage(videoFrames.get(currentFrame), 0, 0, this.getWidth(), this.getHeight(), null);
			currentFrame++;

			if (message.isPresent()) {
				g.setColor(Color.WHITE);
				g.setFont(bubbleFont);

				g.drawString(message.get(), 15, 768 - 50);
			}
		}
	}

	public void playVideoAndDrawNextPanel() {
		currentFrame = 0;
		for (byte[] music : musics) {

			new Thread(() -> SimpleAudioPlayer.playSyncSoundOnce(music, 3f)).start();
		}

		Timer timer = new Timer(1000 / 24, e -> {
			repaint();

			if (currentFrame == (videoFrames.size() - 1)) {
				((Timer) e.getSource()).stop();

				frame.drawPanel(nextPanelToDraw);
			}

		});
		timer.start();

	}

	private void loadFont() {
		if (bubbleFont == null) {
			try {
				final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/bubble_gum.otf")));
				bubbleFont = new Font("Bubble gum", Font.BOLD, 55);
			} catch (IOException | FontFormatException e) {
				// IGNORE
			}
		}

	}

}
