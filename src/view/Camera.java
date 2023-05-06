package view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import controller.MouseController;
import model.Game;

/**
 * La class Camera est une JPanel qui sert à afficher l'écran du jeu 
 *
 */
public class Camera extends JPanel {
	/**
	 * Fonction constructeur
	 */
	public Camera() {
		super();
	}
	
	/**
	 *Fonctionne qui dessine le jeu sur l'écran
	 */
	@Override
	public void paint(Graphics g ) {
		Drawing.drawBackground(g);		
		Drawing.drawPlayground(g);
		Drawing.drawPanel(g);
		if (!Game.getInstance().hasBegun()) {
			Drawing.drawMessage(g);
		}
	}
}
