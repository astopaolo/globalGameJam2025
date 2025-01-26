package it.gamejam.truncate.bubblenap.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import it.gamejam.truncate.bubblenap.ui.audio.SimpleAudioPlayer;
import it.gamejam.truncate.bubblenap.ui.audio.SoundProvider;
import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = -5148674646680024173L;
	private static Image playButtonImage = ImageLoader.getImagePlay();
	private static Image ExitButtonImage = ImageLoader.getImageExit();
	private static Image creditsButtonImage = ImageLoader.getImageCredits();
	private static Image titleImage = ImageLoader.getImageTitle();

	private static Image Background = ImageLoader.getImageBackground();

	private static int PLAY_BUTTON_X = 840;
	private static int PLAY_BUTTON_Y = 300;

	private static int CREDITS_X = PLAY_BUTTON_X - 40;
	private static int CREDITS_Y = PLAY_BUTTON_Y + 150;

	private static int EXIT_X = PLAY_BUTTON_X;
	private static int EXIT_Y = PLAY_BUTTON_Y + 300;

	MainFrame frame;

	public MenuPanel(final MainFrame frame) {

		this.frame = frame;

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

		g.drawImage(creditsButtonImage, CREDITS_X, CREDITS_Y, 400, 72, null);

		g.drawImage(playButtonImage, PLAY_BUTTON_X, PLAY_BUTTON_Y, 330, 83, null);
		g.drawImage(ExitButtonImage, EXIT_X, EXIT_Y, 330, 83, null);

		g.drawImage(titleImage, 130, 0, null);

	}
}
