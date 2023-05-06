package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import controller.MouseController;
import model.Game;

/**
 * Class Window, fenêtre du jeu
 *
 */
public class Window extends JFrame implements Runnable, WindowListener {
	public static Window instance = new Window();
	public static int TIMER_MAX_ACTUALIZE_DRAWING = 15;
	private int timerActualizeDrawing = 0;
	Camera display = new Camera();
	Thread t;
	
	/**
	 * Fonction constructeur, mise en privée car une seule instance possible
	 */
	private Window() {
		setGraphicalDisplay();
		Resources.loadResources();
		this.addMouseListener(MouseController.getInstance());
		this.addMouseMotionListener(MouseController.getInstance());
		t = new Thread(this);
		t.start();
	}
	
	/**
	 * Cette fonction donne son aspect graphique à la fenêtre
	 */
	private void setGraphicalDisplay() {
		this.setResizable(false);
		this.setSize(700,500);
		this.setVisible(true);
		this.setContentPane(display);
	}
	
	/**
	 * Fonction main
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * Boucle principale du jeu
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {			
			try {
				Thread.sleep(5);
				timerActualizeDrawing++;
				if (timerActualizeDrawing>=Window.TIMER_MAX_ACTUALIZE_DRAWING) {
					timerActualizeDrawing=0;
					Drawing.actualizeDrawingUnitMap(Game.getInstance().getListUnit());
				}
				display.repaint();
				Game.getInstance().run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		t.stop();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
