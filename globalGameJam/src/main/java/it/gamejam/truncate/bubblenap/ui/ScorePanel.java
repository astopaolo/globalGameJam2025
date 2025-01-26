package it.gamejam.truncate.bubblenap.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class ScorePanel extends JPanel {
	private static final long serialVersionUID = -2020644767984510521L;

	private static Image scoreBackground = ImageLoader.getImageScoreBackground();
	MainFrame frame;

	public ScorePanel(final MainFrame frame) {
		this.frame = frame;

		this.setLayout(null);
		setPreferredSize(new Dimension(1280, 768));

		requestFocus();

	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(scoreBackground, 0, 0, this.getWidth(), this.getHeight(), null);

	}
}
