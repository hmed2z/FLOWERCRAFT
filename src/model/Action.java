package model;

import java.awt.Rectangle;

/**
 * Cette classe représente une action pouvant être effectuée par une unité
 *
 */
public abstract class Action {
	//timers
	int timerMaxAction = 0;
	int timer = 0;
	
	//apparence
	private static final int SIZE = 30;
	Rectangle physics = new Rectangle();
	//nom
	String name;
	//état
	boolean execution = false;
	boolean pressed = false;
	
	/**
	 * Fonction constructeur
	 */
	public Action() {
		this.setName();
	}
	
	/**
	 * Fonction constructeur
	 * @param x
	 * @param y
	 */
	public Action(int x, int y) {
		this.setName();
		setPhysics(x,y);
	}
	
	/**
	 * Change l'état de pression du bouton
	 * @param pressed
	 */
	public void setPressed(boolean pressed) {
		this.pressed=pressed;
	}
	
	/**
	 * Renvoie une valeur indiquant si le bouton est pressé ou non
	 * @return
	 */
	public boolean isPressed() {
		return this.pressed;
	}
	
	/**
	 * Définis une condition selon laquelle l'action peut être effectuée ou non
	 * @return
	 */
	public abstract boolean condition();
	
	/**
	 * Exécute une action lors du click sur le bouton représentant l'action
	 */
	public abstract void execution();
	/**
	 * Définis le nom de l'action
	 */
	public abstract void setName();
	
	/**
	 * Définis la boucle de l'exécution, si l'action est continue dans le temps
	 * @return
	 */
	public Action executionLoop() {
		return null;
	}
	
	/**
	 * Retourne le nom de l'action
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Action se produisant lors d'un 2ème click (non sur le bouton de l'action)
	 * @param x
	 * @param y
	 */
	public void action2Click(int x, int y) {}
	
	/**
	 * Dit si le point donné est contenu dans la représentation de l'action ou non 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isContained(int x, int y) {
		return physics.contains(x,y);
	}
	
	/**
	 * Définis l'apparence physique de l'action dans l'interface
	 * @param x
	 * @param y
	 */
	public void setPhysics (int x, int y) {
		this.physics.x=x;
		this.physics.y=y;
		this.physics.width=30;
		this.physics.height=30;
	}

	/**
	 * Retourne la coordonnée x
	 * @return
	 */
	public int getX() {
		return this.physics.x;
	}

	/**
	 * Retourne la coordonnée y
	 * @return
	 */
	public int getY() {
		return this.physics.y;
	}

	/**
	 * Retourne la largeur
	 * @return
	 */
	public int getW(){
		return this.physics.width;
	}

	/**
	 * Retourne la hauteur
	 * @return
	 */
	public int getH() {
		return this.physics.height;
	}
}
