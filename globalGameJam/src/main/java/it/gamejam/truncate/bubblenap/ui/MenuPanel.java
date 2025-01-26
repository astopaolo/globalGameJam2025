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

import it.gamejam.truncate.bubblenap.ui.audio.SimpleAudioPlayer;
import it.gamejam.truncate.bubblenap.ui.audio.SoundProvider;
import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class MenuPanel extends JPanel {

	private static final Color C_GREEN = new Color(203, 224, 108, 241);
	private static final long serialVersionUID = -5148674646680024173L;
	private static Image playButtonImage = ImageLoader.getImagePlay();
	private static Image ExitButtonImage = ImageLoader.getImageExit();
	private static Image creditsButtonImage = ImageLoader.getImageCredits();
	// private static Image titleImage = ImageLoader.getImageTitle();

	private static Image Background = ImageLoader.getImageBackground();

	private static int PLAY_BUTTON_X = 750;
	private static int PLAY_BUTTON_Y = 270;

	private static int CREDITS_X = PLAY_BUTTON_X - 90;
	private static int CREDITS_Y = PLAY_BUTTON_Y + 150;

	private static int EXIT_X = PLAY_BUTTON_X + 10;
	private static int EXIT_Y = PLAY_BUTTON_Y + 300;

	MainFrame frame;
	private Font titleFont;

	public MenuPanel(final MainFrame frame) {

		this.frame = frame;
		loadFont();
		setPreferredSize(new Dimension(1280, 768));

		requestFocus();

		addMouseListener(new MouseAdapter() {
			@SuppressWarnings("static-access")
			@Override
			public void mouseReleased(final MouseEvent e) {
				if ((e.getX() >= EXIT_X) && (e.getX() <= (EXIT_X + ImageLoader.getImageExit().getWidth(null)))
						&& (e.getY() >= EXIT_Y)
						&& (e.getY() <= (EXIT_Y + ImageLoader.getImageExit().getHeight(null)))) {
					SimpleAudioPlayer.playSyncSoundOnce(SoundProvider.getBubbleMenuClick(), 3f);

					System.exit(0);
				}
				if ((e.getX() >= PLAY_BUTTON_X)
						&& (e.getX() <= (PLAY_BUTTON_X + ImageLoader.getImagePlay().getWidth(null)))
						&& (e.getY() >= PLAY_BUTTON_Y)
						&& (e.getY() <= (PLAY_BUTTON_Y + ImageLoader.getImagePlay().getHeight(null)))) {
					SimpleAudioPlayer.playSyncSoundOnce(SoundProvider.getBubbleMenuClick(), 0f);

					frame.getBackgroundMusic().stop();
					frame.drawPanel(EnumPanel.INTRO_VIDEO_PANEL);
				}
				if ((e.getX() >= CREDITS_X) && (e.getX() <= (CREDITS_X + ImageLoader.getImagePlay().getWidth(null)))
						&& (e.getY() >= CREDITS_Y)
						&& (e.getY() <= (CREDITS_Y + ImageLoader.getImagePlay().getHeight(null)))) {
					SimpleAudioPlayer.playSyncSoundOnce(SoundProvider.getBubbleMenuClick(), 0f);
					frame.drawPanel(EnumPanel.CREDITS_MENU_PANEL);
				}

			}

		});

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(final MouseEvent e) {
				// Play Start
				if ((e.getX() >= PLAY_BUTTON_X)
						&& (e.getX() <= (PLAY_BUTTON_X + ImageLoader.getImagePlay().getWidth(null)))
						&& (e.getY() >= PLAY_BUTTON_Y)
						&& (e.getY() <= (PLAY_BUTTON_Y + ImageLoader.getImagePlay().getHeight(null)))) {

					playButtonImage = ImageLoader.getImagePlayPressed();

				} else {
					playButtonImage = ImageLoader.getImagePlay();
					// PLay End
				}

				// Credits Start
				if ((e.getX() >= CREDITS_X) && (e.getX() <= (CREDITS_X + ImageLoader.getImageCredits().getWidth(null)))
						&& (e.getY() >= CREDITS_Y)
						&& (e.getY() <= (CREDITS_Y + ImageLoader.getImageCredits().getHeight(null)))) {

					creditsButtonImage = ImageLoader.getImageCreditsPressed();

				} else {
					creditsButtonImage = ImageLoader.getImageCredits();
					// Credits End
				}

				// Exit Start
				if ((e.getX() >= EXIT_X) && (e.getX() <= (EXIT_X + ImageLoader.getImageExit().getWidth(null)))
						&& (e.getY() >= EXIT_Y)
						&& (e.getY() <= (EXIT_Y + ImageLoader.getImageExit().getHeight(null)))) {

					ExitButtonImage = ImageLoader.getImageExit_Pressed();

				} else {
					ExitButtonImage = ImageLoader.getImageExit();
					// Exit End
				}

				repaint();
			}

		});
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);

		g.drawImage(Background, 0, 0, this.getWidth(), this.getHeight(), null);

		g.setColor(Color.WHITE);
		g.setFont(titleFont);

		g.drawImage(creditsButtonImage, CREDITS_X, CREDITS_Y, null);

		g.drawImage(playButtonImage, PLAY_BUTTON_X, PLAY_BUTTON_Y, null);
		g.drawImage(ExitButtonImage, EXIT_X, EXIT_Y, null);

		g.setColor(C_GREEN);
		g.setFont(titleFont);

		g.drawString("Bubble Nap", 50, 180);

	}

	private void loadFont() {
		if (titleFont == null) {
			try {
				final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/bubble_gum.otf")));
				titleFont = new Font("Bubble gum", Font.BOLD, 180);
			} catch (IOException | FontFormatException e) {
				// IGNORE
			}
		}

	}
}
