package model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import model.Map.Node;

/**
 * Unité pouvant se déplacer
 *
 */
public abstract class MovingUnit extends Unit {
	//constantes de direction
	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	public static final int LEFT_UP = 4;
	public static final int LEFT_DOWN = 5;
	public static final int RIGHT_UP = 6;
	public static final int RIGHT_DOWN = 7;
	
	//vitesse actuelle
	int vx,vy;
	double max_speed;
	//Dit si l'unité peut entrer dans un bâtiment ou non
	boolean can_enter_building = true;

	//chemin emprunté actuellement
	List<Integer> path = new LinkedList<>();
	int currentDirection;
	//prochaine case dans le déplacement
	int casex_destination, casey_destination;

	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param width
	 * @param height
	 */
	public MovingUnit(int case_x, int case_y, int width, int height) {
		super(case_x, case_y, width, height);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param width
	 * @param height
	 */
	public MovingUnit(int case_x, int case_y, int width, int height, int dx, int dy) {
		super(case_x, case_y, width, height,dx,dy);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Mouvement de l'unité
	 */
	public int moveEvent() {
		physics.x+=vx;
		physics.y+=vy;
		this.actualizeCase();
		boolean changement = this.handlePath();
		if (vx==0 && vy==0) {
			return 1;
		}
		else if (changement) {
			return 0;
		}
		return 2;

	}
	
	/**
	 * Fonction qui fait suivre à l'unité le chemin le plus court afin d'arriver à une certaine case
	 * @return si il y a eu une nouvelle trajectoire ou non
	 */
	public boolean handlePath() {
		int size = path.size();
		if (size>0) {
			if (this.physics.x==casex_destination*Settings.SIZE_CASE && this.physics.y-deltaY==casey_destination*Settings.SIZE_CASE) {
				this.path.remove(this.path.get(0));
				this.case_x=casex_destination;
				this.case_y=casey_destination;
				if (size-1>0) {
					setSpeedByDirection(this.path.get(0));
					casex_destination=this.case_x+this.vx;
					casey_destination=this.case_y+this.vy;		
					
				}
				else {
					vx=0;
					vy=0;
				}
				return true;
			}
		}
		else {
			vx=0;
			vy=0;
		}
		return false;
	}
	
	/**
	 * Accorde la vitesse en fonction de la vitesse maximale initiale et de la direction
	 * @param direction
	 */
	public void setSpeedByDirection(int direction) {
		currentDirection=direction;
		switch (direction) {
		case LEFT:
			vx=-1;
			vy=0;
			break;
		case RIGHT:
			vx=1;
			vy=0;
			break;
		case UP:
			vy=-1;
			vx=0;
			break;
		case DOWN:
			vy=1;
			vx=0;
			break;
		case LEFT_UP:
			vx=-1;
			vy=-1;
			break;
		case LEFT_DOWN:
			vx=-1;
			vy=1;
			break;
		case RIGHT_UP:
			vx=1;
			vy=-1;
			break;
		case RIGHT_DOWN:
			vx=1;
			vy=1;
			break;
		}
	}
	
	/**
	 * Fonction qui calcule le chemin le plus court pour atteindre une certaine case
	 * @param cx, coordonée de la case suivant l'axe x dans la map
	 * @param cy, coordonée de la case suivant l'axe y dans la map
	 */
	public boolean pathFinding(int cx, int cy) {
		Node startingNode = Map.getInstance().getNode(this.case_x, this.case_y);
		Node endNode =  Map.getInstance().getNode(cx, cy);
		if (startingNode!=null && endNode!=null) {
			path = PathFinding.findPath(startingNode, endNode, Map.getInstance(),can_enter_building);
			if (path.size()>=1) {						
				setSpeedByDirection(this.path.get(0));
				casex_destination=this.case_x+this.vx;
				casey_destination=this.case_y+this.vy;
				return true;
			}
		}
		return false;
	}
	
	
}
