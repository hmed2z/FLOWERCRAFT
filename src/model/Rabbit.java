package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import model.Map.Node;

/**
 * Rabbit est une classe représentant un lapin dans le jeu
 *
 */
public class Rabbit extends MovingUnit {
	//états possibles
	private static final int CHASING_FLOWER = 0;
	private static final int EATING = 1;
	private static final int RUNNING_AWAY_TO_COME_BACK = 2;
	private static final int RUNNING_AWAY_TO_DISAPPEAR = 3;
	private static final int DISAPPEAR = 4;
	private static final int CHANGING_FLOWER = 5;
	int nextState = CHASING_FLOWER;
	int state = CHASING_FLOWER;
	//Liste des fleurs qui sont actuellement pourchassées par un lapin
	private static List<Flower> flowerChased = new LinkedList<>();
	//Case finale voulue lors des déplacements
	private Node wantedCase;
	private Node caseScared;
	//timer
	private int TIMER_BETWEEN_JUMP = 200;
	private int timer=-1;
	//Fleur actuelle chassée
	Flower flower = null;
	//Nombres de fleurs mangées, nombre de fois ou le lapin a pris peur
	int numberFlowerEaten = 0;
	int numberScared = 0;
	//Distance par rapport à la case désirée
	double distCaseWanted;
	//côté vers lequel le lapin est dirigé
	public boolean side = false;
	
	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 */
	public Rabbit(int case_x, int case_y) {
		super(case_x, case_y, 30, 30);
		can_enter_building=false;
		this.max_speed=1;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Fonction appelée à chaque tour, qui gère les actions relatives à l'unité
	 */
	@Override
	public void handleEvent() {
		try {
			if (state!=DISAPPEAR) {
				if (timer==-1) {
					physics.x+=vx;
					physics.y+=vy;
					this.actualizeCase();
					if (handlePath()) {
						timer=0;
					}
					if (this.path.size()==0 && (this.case_x!=wantedCase.x || this.case_y!=wantedCase.y)) {
						seekNewPath();
					}
					else if (this.physics.x==wantedCase.x*Settings.SIZE_CASE && this.physics.y==wantedCase.y*Settings.SIZE_CASE ) {
						if (state==CHASING_FLOWER) {
							timer=0;
							state=EATING;
						}
						else if (state == CHANGING_FLOWER) {
							if (!seekClosestFlower() || numberFlowerEaten==2) {
								state=RUNNING_AWAY_TO_DISAPPEAR;
								wantedCase = Map.getInstance().getNode(13-(int)(Math.random()*5),13);
							}
							distCaseWanted = PathFinding.calculateDist(this.case_x, wantedCase.x, this.case_y, wantedCase.y);
						}
						else if (state==RUNNING_AWAY_TO_DISAPPEAR) {
							state=DISAPPEAR;
						}
						else if (state==RUNNING_AWAY_TO_COME_BACK && (this.case_x!=caseScared.x || this.case_y!=caseScared.y)) {
							wantedCase = caseScared;
							if (!this.pathFinding(wantedCase.x, wantedCase.y)) {
								state=RUNNING_AWAY_TO_DISAPPEAR;
								wantedCase = Map.getInstance().getNode(13-(int)(Math.random()*5),13);
							}
							distCaseWanted = PathFinding.calculateDist(this.case_x, wantedCase.x, this.case_y, wantedCase.y);
						}
						else if (state==RUNNING_AWAY_TO_COME_BACK) {
							state=CHANGING_FLOWER;
							if (!seekClosestFlower() || numberFlowerEaten==2) {
								state=RUNNING_AWAY_TO_DISAPPEAR;
								wantedCase = Map.getInstance().getNode(13-(int)(Math.random()*5),13);
								distCaseWanted = PathFinding.calculateDist(this.case_x, wantedCase.x, this.case_y, wantedCase.y);
							}
						}
						
					}
				}
				else {
					timer++;
					if (timer>=TIMER_BETWEEN_JUMP && state!=EATING) {
						timer=-1;
					}
					else if (timer>=4*TIMER_BETWEEN_JUMP && state==EATING) {
						timer=-1;
						numberFlowerEaten++;
						Game.getInstance().removeUnit(flower);
						this.flower=null;
						state=CHANGING_FLOWER;
					}
				}			
			}
			else {				
				physics.x+=vx;
				physics.y+=vy;
				this.vy=1;
				if (this.physics.y>=400) {
					Game.getInstance().removeUnit(this);
				}
			}
			if (vx<0) {
				side=true;
			}
			else if (vx>0) {
				side=false;
			}
			
			if (state==(Rabbit.RUNNING_AWAY_TO_COME_BACK) || state==(Rabbit.RUNNING_AWAY_TO_DISAPPEAR)) {
				TIMER_BETWEEN_JUMP = 20;
			}
			else {
				TIMER_BETWEEN_JUMP = 100;
			}
		}
		catch (Exception e) {
			//En cas d'erreur, le lapin s'enfuit
			state=RUNNING_AWAY_TO_DISAPPEAR;
			wantedCase = Map.getInstance().getNode(13-(int)(Math.random()*5),13);
			distCaseWanted = PathFinding.calculateDist(this.case_x, wantedCase.x, this.case_y, wantedCase.y);
		}
	}

	/**
	 * Définis le nom de l'unité
	 */
	@Override
	protected void setNameUnit() {
		// TODO Auto-generated method stub
		this.nameUnit="rabbit";
	}

	/**
	 * Définis les actions que l'utilisateur peut faire en cliquant sur le lapin
	 */
	@Override
	public void setActions() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Cette fonction effraie le lapin
	 */
	public void scared() {
		if (numberScared>=1) {
			TIMER_BETWEEN_JUMP-=50;
			state=RUNNING_AWAY_TO_DISAPPEAR;
			wantedCase = Map.getInstance().getNode(13-(int)(Math.random()*5),13);
			distCaseWanted = PathFinding.calculateDist(this.case_x, wantedCase.x, this.case_y, wantedCase.y);
		}
		else {
			state=RUNNING_AWAY_TO_COME_BACK;
		}
	}
	
	/**
	 * Algorithme pseudo aléatoire qui choisit une case aléatoire étant plus proche que lui de la case voulue
	 * que ce soit selon l'axe x ou l'axe y, et qui est situé entre la case voulue et lui-même dans ces 2 directions
	 */
	public void seekNewPath() {
		try {
			List<Point> possibleCases = new LinkedList<>();
			for (int i = -1; i<2;i++) {
				for (int j = -1; j<2;j++) {
					if (i!=0 || j!=0) {
						if (PathFinding.calculateDist(this.case_x+i,wantedCase.x, this.case_y+j, wantedCase.y)<distCaseWanted) {
							possibleCases.add(new Point(this.case_x+i,this.case_y+j));
						}
					}
				}
			}
			int caseRandom = (int)(Math.random()*possibleCases.size());
			distCaseWanted = PathFinding.calculateDist(possibleCases.get(caseRandom).x,wantedCase.x,possibleCases.get(caseRandom).y,wantedCase.y);
			if ((!this.pathFinding(possibleCases.get(caseRandom).x, possibleCases.get(caseRandom).y))) {
				for (Point p : possibleCases) {
					if (this.pathFinding(p.x, p.y)) {
						distCaseWanted = PathFinding.calculateDist(p.x,wantedCase.x,p.y,wantedCase.y);
						return;
					}
				}
			}
		}
		catch (Exception e) {
			//En cas d'erreur, si l'algorithme ne trouve pas de chemin plus proche, le lapin tente de prendre le chemin le plus court
			this.pathFinding(wantedCase.x, wantedCase.y);
		}

	}
	
	/**
	 * Cherche la fleur la plus proche du lapin
	 * @return
	 */
	public boolean seekClosestFlower() {
		double minDist = 1000;
		wantedCase=null;
		for (Unit u : Game.getInstance().getListUnit()) {
			if (u instanceof Flower) {
				double ndist = PathFinding.calculateDist(u.case_x, this.case_x, u.case_y, this.case_y); 
				if (ndist<minDist && !Rabbit.flowerChased.contains((Flower)u)) {
					wantedCase = Map.getInstance().getNode(u.case_x, u.case_y);
					distCaseWanted = PathFinding.calculateDist(this.case_x, wantedCase.x, this.case_y, wantedCase.y);
					flower = (Flower)u;
				}
			}
		}
		return wantedCase!=null;
	}
	
	/**
	 * Retourne une valeur disant si on peut effectuer des actions à partir de l'unité ou non
	 */
	@Override
	public boolean clickable() {
		return false;
	}
	
	/**
	 * Effraie le lapin, en partant de la case (case_x,case_y)
	 * @param case_x
	 * @param case_y
	 */
	public void scareRabbit(int case_x, int case_y) {
		if (Math.abs(case_y-this.case_y)<=1 && Math.abs(case_x-this.case_x)<=1 && state!=RUNNING_AWAY_TO_COME_BACK) {
			int nx = case_x-this.case_x>0?this.case_x-2:this.case_x+2;
			int ny = case_y-this.case_y>0?this.case_y-2:this.case_y+2;
			caseScared = Map.getInstance().getNode(nx, ny);
			this.scared();
			this.numberScared++;
		}
	}
	
	/**
	 * Définis un ordre de priorité au niveau des click sur les unités
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 2;
	}
}
