package it.gamejam.truncate.bubblenap.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.gamejam.truncate.bubblenap.core.GameManager;

public class MainFrameLuigiTBD extends JFrame {

	public static void main(String[] args) {
		MainFrameLuigiTBD mf = new MainFrameLuigiTBD();
		mf.setVisible(true);
		mf.setup();
	}

	public MainFrameLuigiTBD() {
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
