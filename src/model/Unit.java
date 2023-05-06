package model;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
/**
 * Un unit� quelconque qui est situ� sur la map
 */
public abstract class Unit {
	//noms (utilis�s pour la r�cup�ration des images)
	String nameUnit;
	String nameUnitNormal;
	String nameSelected;
	String nameCursorMove;
	
	//position et taille (sur l'�cran)
	Rectangle physics;
	
	//position sur la map
	int case_x;
	int case_y;
	//Si l'unit� d�passe les cases (selon l'axe y), dans les calculs  il faut prendre �a en compte
	protected int deltaY = 0;
	
	//Actions que l'utilisateur peut effectuer apr�s click sur l'unit�
	Action[] actions;
	//Dans le cas des actions durant dans le temps, action actuelle qui est en train d'�tre effectu�e
	Action currentAction;
	
	/**
	 * Fonction constructeur
	 * @param _x
	 * @param _y
	 */
	public Unit(int case_x, int case_y, int width, int height) {
		physics = new Rectangle(case_x*Settings.SIZE_CASE,case_y*Settings.SIZE_CASE,width,height);
		this.case_x = case_x;
		this.case_y = case_y;
		setNameUnit();
		setActions();
	}

	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param width
	 * @param height
	 * @param dx
	 * @param dy
	 */
	public Unit(int case_x, int case_y, int width, int height, int dx, int dy) {
		physics = new Rectangle(case_x*Settings.SIZE_CASE+dx,case_y*Settings.SIZE_CASE+dy,width,height);
		this.case_x = case_x;
		this.case_y = case_y;
		deltaY=dy;
		setNameUnit();
		setActions();
	}
	
	/**
	 * D�finis le nom de l'unit�
	 */
	protected abstract void setNameUnit();
	
	/**
	 * Retourne le nom de l'unit�
	 * @return
	 */
	public String getNameUnit() {
		return nameUnit;
	}
	/**
	 * Fonction appel�e � chaque tour qui g�re les �v�nements relatifs � l'unit�
	 */
	public void handleEvent() {
		if (currentAction != null) {
			currentAction = currentAction.executionLoop();
		}
	}

	/**
	 * Dit si le point donn� fait partie de l'objet ou non
	 * @param p
	 * @return
	 */
	public boolean isContained(Point p) {
		return physics.contains(p);
	}
	
	/**
	 * D�finis les actions 
	 */
	public abstract void setActions();
	
	/**
	 * Retourne la liste des actions possibles de l'unit�
	 * @return
	 */
	public Action[] getAction() {
		return actions;
	}
	
	/**
	 * Cette fonction retourne la liste des actions possibles que peut effectuer l'unit�
	 * @return
	 */
	public Action[] getActions() {
		return this.actions;
	}
	
	/**
	 * Dit si le point donn� fait partie de l'objet ou non
	 * @param p
	 * @return
	 */
	public boolean isContained(int x, int y) {
		return physics.contains(new Point(x,y));
	}
	
	/**
	 * Retourne la case contenant l'unit�
	 */
	public Point getCase() {
		return new Point(case_x,case_y);
	}
	
	/**
	 * Retourne la coordonn�e x de l'unit�
	 * @return
	 */
	public int getX() {
		return (int)this.physics.getX();
	}

	/**
	 * Retourne la coordonn�e y de l'unit�
	 * @return
	 */
	public int getY() {
		return (int)this.physics.getY();
	}
	
	/**
	 * Retourne la largeur de l'unit�
	 * @return
	 */
	public int getW() {
		return (int)this.physics.getWidth();
	}
	
	/**
	 * Retourne la hauteur de l'unit�
	 * @return
	 */
	public int getH() {
		return (int)this.physics.getHeight();
	}
	
	/**
	 * Actualise la position sur la map, par rapport aux coordonn�es sur l'�cran
	 */
	public void actualizeCase() {
		case_x = physics.x/30;
		case_y = (physics.y-deltaY)/30;
	}
	
	/**
	 * Cette fonction dit si le click sur l'unit� peut produire une action ou non
	 * @return
	 */
	public boolean clickable() {
		return true;
	}
	
	/**
	 * D�finis l'animation lors d'un click
	 */
	public void setAnimClick() {
		this.nameUnit=this.nameSelected;
	}
	
	/**
	 * D�finis l'animation lors du passage avec la souris
	 */
	public void setAnimMouseMove() {
		if (!this.nameUnit.equals(this.nameSelected))
			this.nameUnit=this.nameCursorMove;
	}
	
	/**
	 * D�finis l'animation de base
	 * @param mode
	 */
	public void setAnimNormal(boolean mode) {
		if (this.nameUnit.equals(this.nameCursorMove)||(this.nameUnit.equals(this.nameSelected)&&mode))
			this.nameUnit=this.nameUnitNormal;
	}	
	
	/**
	 * D�finis un ordre de priorit� entre les unit�s pour d�terminer quels unit�s sont affich�es en premier
	 * @return
	 */
	public abstract int orderPriority();
}
