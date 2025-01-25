package globalGameJam.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import globalGameJam.core.GameManager;

public class MainFrame extends JFrame {

	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.setVisible(true);
		mf.setup();
	}

	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void setup() {
		GameManager gameManager = new GameManager();
		GamePanel gamePanel = new GamePanel(gameManager);
		gameManager.setRepaintable(gamePanel);
		gameManager.startSound();
		showPanel(gamePanel);
		pack();
	}

	public void showPanel(JPanel panel) {
		setContentPane(panel);
		revalidate();
		panel.requestFocus();
		System.out.println(getFocusOwner());
//		panel.shown();
	}
}
