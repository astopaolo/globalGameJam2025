
package it.gamejam.truncate.bubblenap.ui;

import javax.swing.JFrame;

import it.gamejam.truncate.bubblenap.core.GameManager;
import it.gamejam.truncate.bubblenap.ui.audio.SimpleAudioPlayer;
import it.gamejam.truncate.bubblenap.ui.audio.SoundProvider;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -6974599828262854447L;

	private static SimpleAudioPlayer backgroundMusic;

	public static SimpleAudioPlayer getBackgroundMusic() {
		return backgroundMusic;
	}

	public static void main(final String[] args) throws Exception {

		backgroundMusic = new SimpleAudioPlayer(SoundProvider.getMenu(), 3f);
		backgroundMusic.playLoop();
		final JFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);

	}

	public static void setBackgroundMusic(final SimpleAudioPlayer backgroundMusic) {
		MainFrame.backgroundMusic = backgroundMusic;
	}

	private final MenuPanel menuPanel;

	private final CreditsMenuPanel creditsMenu;
	private final GamePanel gamePanel;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		GameManager gameManager = new GameManager();
		setTitle("BubbleNap");
		menuPanel = new MenuPanel(this);
		creditsMenu = new CreditsMenuPanel(this);
		gamePanel = new GamePanel(gameManager);
		gameManager.setRepaintable(gamePanel);
		setUndecorated(true);
		this.setContentPane(menuPanel);
		pack();
		setLocationRelativeTo(null);
	}

	public void drawPanel(final EnumPanel panel) {
		drawPanel(panel, false);
	}

	public void drawPanel(final EnumPanel panel, final boolean restartMusic) {

		switch (panel) {
		case MENU_PANEL:
			this.setContentPane(menuPanel);
			if (restartMusic) {
				try {
					backgroundMusic.restart();
				} catch (final Exception e1) {
					e1.printStackTrace();
				}
			}
			break;

		case CREDITS_MENU_PANEL:
			this.setContentPane(creditsMenu);
			break;
		case GAME_PANEL:
			this.setContentPane(gamePanel);
			gamePanel.requestFocus();
			try {
				// gamePanel.startGame();
			} catch (final Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			break;
		default:
			break;
		}

		pack();
		setLocationRelativeTo(null);
	}
}
