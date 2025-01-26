package it.gamejam.truncate.bubblenap.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import it.gamejam.truncate.bubblenap.core.GameManager;
import it.gamejam.truncate.bubblenap.ui.audio.SimpleAudioPlayer;
import it.gamejam.truncate.bubblenap.ui.audio.SoundProvider;
import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class ScorePanel extends JPanel {
	private static final long serialVersionUID = -2020644767984510521L;

	private static Image scoreBackground = ImageLoader.getImageScoreBackground();
	private static Image back = ImageLoader.getImageBack();
	private static final int BACK_X = (1280 / 2) - 150;
	private static final int BACK_Y = 643;
	MainFrame frame;
	private GameManager gameManager;
	private Font bubbleFont;

	public ScorePanel(final GameManager gameManager, final MainFrame frame) {
		this.gameManager = gameManager;
		this.frame = frame;

		this.setLayout(null);
		setPreferredSize(new Dimension(1280, 768));

		requestFocus();

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(final MouseEvent e) {
				// Back Start
				if ((e.getX() >= BACK_X) && (e.getX() <= (BACK_X + ImageLoader.getImageBack().getWidth(null)))
						&& (e.getY() >= BACK_Y)
						&& (e.getY() <= (BACK_Y + ImageLoader.getImageBack().getHeight(null)))) {
					back = ImageLoader.getImageBackPressed();
				} else {
					back = ImageLoader.getImageBack();
					// Back End
				}

				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent e) {
				// pulsante back
				if ((e.getX() >= BACK_X) && (e.getX() <= (BACK_X + ImageLoader.getImageBack().getWidth(null)))
						&& (e.getY() >= BACK_Y)
						&& (e.getY() <= (BACK_Y + ImageLoader.getImageBack().getHeight(null)))) {
					SimpleAudioPlayer.playSyncSoundOnce(SoundProvider.getBubbleMenuClick(), 3f);

					frame.drawPanel(EnumPanel.MENU_PANEL);
				}
			}

		});

		showScore();
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(scoreBackground, 0, 0, this.getWidth(), this.getHeight(), null);
		g.drawImage(back, BACK_X, BACK_Y, 330, 83, null);

		g.setColor(Color.CYAN);
		g.setFont(bubbleFont);

		g.drawString("Points" + gameManager.getPoints(), 400, 300);

		g.drawString("" + gameManager.getPoints(), 400, 385);

	}

	private void showScore() {
		if (bubbleFont == null) {
			try {
				final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/bubblegums.ttf")));
				bubbleFont = new Font("bubblegums", Font.BOLD, 50);
			} catch (IOException | FontFormatException e) {
				// IGNORE
			}
		}
	}
}
