package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import model.Game;

/**
 * Cette classe est dédiée à la récupération des interactions entre l'utilisateur et le programme par l'intermédiaire de la souris
 *
 */
public class MouseController implements MouseListener, MouseMotionListener {
	private static MouseController mouseController = new MouseController();
	
	public int mouse_x=-1;
	public int mouse_y=-1;

	/**
	 * Fonction constructeur. On la met en private afin que MouseController ne puisse être instancié et qu'il n'y ait
	 * ainsi qu'un seul objet mouseController
	 */
	private MouseController() {}
	
	/**
	 * Fonction qui retourne l'unique instance de MouseController
	 * @return
	 */
	public static MouseController getInstance(){
		return mouseController;
	}
	   
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (Game.getInstance().hasBegun()) {
			if (e.getX()<20+14*30) {
				int real_x_play_ground = e.getX()-20;
				int real_y_play_ground = e.getY()-50;
				Game.getInstance().actionClickPlayground(real_x_play_ground, real_y_play_ground);
			}
			else {
				int real_x_panel = e.getX()-472;
				int real_y_panel = e.getY()-274;
				Game.getInstance().actionClickActions(real_x_panel, real_y_panel);
			}
		}
		else {
			Game.getInstance().initialize();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getX()<20+14*30) {

		}
		else {
			int real_x_panel = e.getX()-472;
			int real_y_panel = e.getY()-274;
			
			Game.getInstance().actionPressActions(real_x_panel, real_y_panel);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.getInstance().actionReleasedActions();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouse_x = e.getX()-20;
		mouse_y = e.getY()-50;
		Game.getInstance().actionMoveMousePlayground(mouse_x, mouse_y);
	}

}
