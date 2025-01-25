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

public class CreditsMenuPanel extends JPanel {

	private static final long serialVersionUID = 7717652359188444409L;
	private static Image backgroundBlur = ImageLoader.getImageBackground_Blur();
	private static Image back = ImageLoader.getImageBack();
//	private static Image astorino = ImageLoader.getImageAstorino();
//	private static Image riolo = ImageLoader.getImageRiolo();
//	private static Image granata = ImageLoader.getImageGranata();
//	private static Image algieri = ImageLoader.getImageAlgieri();

//	private static int GRANATA_X = 10;
//	private static int GRANATA_Y = 70;
//
//	private static int ASTORINO_X = GRANATA_X + 340;
//	private static int ASTORINO_Y = GRANATA_Y + 80;
//	private static int RIOLO_X = ASTORINO_X + 300;
//	private static int RIOLO_Y = GRANATA_Y;
//	private static int ALGIERI_X = RIOLO_X + 310;
//	private static int ALGIERI_Y = GRANATA_Y + 80;

	private static int BACK_X = (1280 / 2) - 150;
	private static int BACK_Y = 643;

	MainFrame frame;

	public CreditsMenuPanel(final MainFrame frame) {

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
					SimpleAudioPlayer.playSyncSoundOnce(SoundProvider.getMorseMenu(), 0f);

					frame.drawPanel(EnumPanel.MENU_PANEL);
				}
			}

		});
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundBlur, 0, 0, this.getWidth(), this.getHeight(), null);
		g.drawImage(back, BACK_X, BACK_Y, 330, 83, null);

//		g.drawImage(granata, GRANATA_X, GRANATA_Y, null);
//		g.drawImage(astorino, ASTORINO_X, ASTORINO_Y, null);
//		g.drawImage(riolo, RIOLO_X, RIOLO_Y, null);
//		g.drawImage(algieri, ALGIERI_X, ALGIERI_Y, null);

	}
}
