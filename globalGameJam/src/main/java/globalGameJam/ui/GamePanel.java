package globalGameJam.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import globalGameJam.core.Bubble;
import globalGameJam.core.GameManager;

public class GamePanel extends JPanel implements Repaintable {
	private static final long serialVersionUID = 1L;
	private Bubble bubble;
	private GameManager gameManager;

	public GamePanel(GameManager gameManager) {
		this.gameManager = gameManager;
		this.bubble = gameManager.getBubble();
		setPreferredSize(new Dimension(1200, 800));
		setBackground(Color.WHITE);
		System.out.println("GamePanel.GamePanel()");
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("GamePanel.GamePanel(...).new KeyAdapter() {...}.keyPressed()");
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A: {
					bubble.setRadius(bubble.getRadius() - 15);
					break;
				}
				case KeyEvent.VK_S: {
					bubble.setRadius(bubble.getRadius() + 15);
					break;
				}
				}
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
//		System.out.println("GamePanel.paintComponent() " + bubble.getRadius());
		super.paintComponent(g);
		g.fillOval(600, 400, (int) bubble.getRadius(), (int) bubble.getRadius());
	}

	@Override
	public void update() {
		repaint();
	}

}
