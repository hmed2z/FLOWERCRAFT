package view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.ImageObserver;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import controller.MouseController;
import model.Game;
import model.Gardener;
import model.Rabbit;
import model.Unit;
import model.Action;

/**
 * Classe qui contient toutes les fonctions de dessin du jeu
 *
 */
public class Drawing {
	//Cette SortedMap est utilisée de manière à ce que les graphismes des unités soient dessinées dans le bon ordre, pour donner un effet de "3D"
	public static SortedMap<Integer,List<Unit>> drawingUnitMap = new TreeMap<>();
	/**
	 * Fonction qui dessine le background
	 */
	public static void drawBackground(Graphics g) {
		g.setColor(Resources.backgroundColor);
		g.fillRect(0, 0, 700, 500);		
	}
	
	/**
	 * Actualise l'ordre de dessin des unités
	 */
	public static void actualizeDrawingUnitMap(List<Unit> l) {
		drawingUnitMap = new TreeMap<>();
		for (Unit u : l) {
			if (!drawingUnitMap.containsKey(u.getY()+u.getH())) {
				LinkedList<Unit> listElement = new LinkedList<>();
				listElement.add(u);
				drawingUnitMap.put(u.getY()+u.getH(), listElement);				
			}
			else {
				drawingUnitMap.get(u.getY()+u.getH()).add(u);
			}
		}
	}
	
	/**
	 * Fonction qui dessine le terrain du jeu
	 * @param g
	 */
	public static void drawPlayground(Graphics g) {
		g.drawImage(Resources.getResources("terrain"), 20,20, null);
		if (Game.getInstance().isActionClickRunningPresent()) {
			if (MouseController.getInstance().mouse_x/30>=0 && MouseController.getInstance().mouse_y/30>=0 && MouseController.getInstance().mouse_x/30<14 && MouseController.getInstance().mouse_y/30<14) {
				Drawing.drawCase((Graphics2D)g, 20+(MouseController.getInstance().mouse_x/30)*30, 20+(MouseController.getInstance().mouse_y/30)*30);
			}
		}
		for (Map.Entry<Integer,List<Unit>> lu : drawingUnitMap.entrySet()) {
			for (Unit u : lu.getValue()) {
				drawUnit(u,g);
			}
		}
	}

	/**
	 * Cette fonction dessine le panel d'information, affichant le score, le nombre de graines, l'objet actuel sélectionné et les actions possibles
	 * @param g
	 */
	public static void drawPanel(Graphics g) {
		g.drawImage(Resources.getResources("title"),463,20,null);
		g.setFont(Resources.fontGame);
		g.setColor(Resources.fontColor);

		g.drawString(String.valueOf(Game.getInstance().getNumberCoin()), 498, 70);
		g.drawString(String.valueOf(Game.getInstance().getCurrentTime()), 598, 70);
		for (int i = 0; i<3;i++) {
			g.drawString(String.valueOf(Game.getInstance().getStat(Game.SEED,i)),502+i*60,102);
		}
		for (int i = 0; i<3;i++) {
			g.drawString(String.valueOf(Game.getInstance().getStat(Game.FLOWER,i)),502+i*60,132);
		}
		g.drawImage(Resources.getResources("coin"),472,55,20,20,null);
		g.drawImage(Resources.getResources("time"),570,55,20,20,null);
		g.drawImage(Resources.getResources("seed1"),470,80,30,30,null);
		g.drawImage(Resources.getResources("seed2"),530,80,30,30,null);
		g.drawImage(Resources.getResources("seed3"),590,80,30,30,null);
		g.drawImage(Resources.getResources("flower1"),472,115,24,24,null);
		g.drawImage(Resources.getResources("flower2"),532,115,24,24,null);
		g.drawImage(Resources.getResources("flower3"),592,115,24,24,null);
		g.drawImage(Resources.getResources("box"), 473, 150, null);
		g.drawImage(Resources.getResources(Game.getInstance().nameUnit), 555-(int)(25*Game.getInstance().modifierWidth), 170,(int)(50*Game.getInstance().modifierWidth),50,null);
		for (Action a : Game.getInstance().getActions()) {
			drawAction(a,g);
		}
	}
	
	/**
	 * Fonction qui dessine une unité sur l'écran
	 * @param g
	 */
	public static void drawUnit(Unit u, Graphics g) {
		if (u instanceof Rabbit) {
			if (((Rabbit)u).side) {
				g.drawImage(Resources.getResources(u.getNameUnit()), u.getX()+20, u.getY()+20, u.getW(), u.getH(), null);
			}
			else {
				g.drawImage(Resources.getResources(u.getNameUnit()), u.getX()+20+u.getW(), u.getY()+20, -u.getW(), u.getH(), null);
			}

		}
		else {
			if (u instanceof Gardener) {
				int nbrFlower = ((Gardener)u).getListFlower().size();
				if (nbrFlower!=0) {
					g.drawImage(Resources.getResources("actionbouquet"), u.getX()+20, u.getY(), 20,20, null);
					g.setColor(Resources.fontColor);
					g.drawString(String.valueOf(nbrFlower),u.getX()+38,u.getY()+12);
				}
			}
			g.drawImage(Resources.getResources(u.getNameUnit()), u.getX()+20, u.getY()+20, u.getW(), u.getH(), null);
		}

	}
	
	/**
	 * Dessine les actions possibles que l'utilisateur peut effectuer
	 * @param a
	 * @param g
	 */
	public static void drawAction(Action a, Graphics g) {
		if (!a.condition()) {
			g.drawImage(Resources.getResources("actiondisabled"), a.getX()+473, a.getY()+244, a.getW(), a.getH(), null);	
			g.drawImage(Resources.getResources(a.getName()), a.getX()+473, a.getY()+244, a.getW(), a.getH(), null);
			g.drawImage(Resources.getResources("filterdisabled"), a.getX()+473, a.getY()+246, a.getW(), a.getH(), null);	
		}
		else if (a.isPressed()) {
			g.drawImage(Resources.getResources("actionbackground2"), a.getX()+473, a.getY()+244, a.getW(), a.getH(), null);		
			g.drawImage(Resources.getResources(a.getName()), a.getX()+473, a.getY()+246, a.getW(), a.getH(), null);
		}
		else {
			g.drawImage(Resources.getResources("actionbackground"), a.getX()+473, a.getY()+244, a.getW(), a.getH(), null);	
			g.drawImage(Resources.getResources(a.getName()), a.getX()+473, a.getY()+244, a.getW(), a.getH(), null);
		}
	}

	/**
	 * Dessine la case sur laquelle l'utililsateur peut se déplacer
	 * @param g
	 * @param x
	 * @param y
	 */
	public static void drawCase(Graphics2D g, int x, int y) {
		g.setColor(Resources.caseColor);
		g.setStroke(new BasicStroke(3));
		RoundRectangle2D.Double r = new RoundRectangle2D.Double(x,y,30,30,5,5);
		g.draw(r);
	}

	/**
	 * Dessine le message de début
	 * @param g
	 */
	public static void drawMessage(Graphics g) {
		g.drawImage(Resources.getResources(Game.getInstance().message), 0, 0, null);
	}
}
