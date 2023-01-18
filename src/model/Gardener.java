package model;

import java.util.LinkedList;
import java.util.List;

public class Gardener extends MovingUnit {
	//États possibles du jardinier
	public static final int NORMAL = 0;
	public static final int SCARING_RABBIT = 1;
	public static final int HARVESTING_FLOWER = 2;
	public static final int PLANTING_FLOWER = 3;
	public static final int COMPOSING_BOUQUET = 4;
	public static final int CONSTRUCTING_BUILDING = 5;
	
	//Animation
	public static final int MOVING = 0;
	public static final int STATIC = 1;
	public static final int HARVEST_FLOWER = 2;
	public static final int PLANT_FLOWER = 3;
	public static final int COMPOSE_BOUQUET = 4;
	public static final int SCARE_RABBIT = 5;
	public static final int BUILDING = 6;
	
	//Apparence
	private int width = 30;
	private int height = 40;
	//Timer
	private static final int TIMING_SCARE_RABBIT = 200;
	private int timerScaringRabbit = -1;
	//Timer animation
	private int TIMER_ANIM = 25;
	private int totalNumberAnim = 0;
	private int timerAnim = 0;
	private int indexAnimList = 3;
	private int indexAnim = 0;
	private boolean onetimeanim=false;
	//Construction
	private int stepConstruction;
	private MainBuilding mainBuildingContainingGardener = null;
	private int stateGardener = NORMAL;
	//Nouvelle case voulue si changement de chemin en cours de déplacement
	int new_case_x = -1;
	int new_case_y = -1;
	
	//Liste des fleurs que le jardinier possède
	List<Integer> flowerContained = new LinkedList<>();
	//Animation
	
	/**
	 * Constructeur
	 * @param case_x
	 * @param case_y
	 */
	public Gardener(int case_x, int case_y) {
		super(case_x, case_y, 30, 40,0,-15);
	}
	
	/**
	 * Gère les évènements relatifs à un jardinier, appelé à chaque tour
	 */
	@Override
	public void handleEvent() {
		//si l'action actuelle est une action qui prend du temps, elle est gérée par la ligne de code suivante
		super.handleEvent();	
		MainBuilding newMainBuilding = null;
		for (Unit u : Game.getInstance().getListUnit()) {
			//Cueillette automatique des fleurs
			if (u instanceof Flower) {
				if (u.case_x==this.case_x && u.case_y == this.case_y && ((Flower)u).getState()==5) {
					this.flowerContained.add(((Flower)u).getType());
					((Flower)u).harvest();
					this.harvestFlower(((Flower)u).getType());
				}
			}
			else if (u instanceof MainBuilding) {
				//On regarde si le jardinier se trouve dans un bâtiment principal ou non
				if (u.case_x==this.case_x && u.case_y == this.case_y) {
					newMainBuilding=(MainBuilding)u;
					if ((MainBuilding)u != mainBuildingContainingGardener) {
						newMainBuilding.addGardener(this);
					}
				}
			}
		}
		
		//Si le bâtiment principal contenant le jardinier n'est pas le même que le précédent, on le supprime du bâtiment précédent
		if (mainBuildingContainingGardener != newMainBuilding && mainBuildingContainingGardener!=null) {
			mainBuildingContainingGardener.removeGardener(this);			
		}
		mainBuildingContainingGardener = newMainBuilding;
		
		//Gestion des animations de déplacement, en fonction de la vitesse actuelle de 
		if ((this.vx!=0 || this.vy!=0) && indexAnimList<=3) {
			if (currentDirection<4) {
				this.indexAnimList=currentDirection;
			}
			else {
				setAnimDiagonal();
			}
		}
		this.handleAnim();
	}
	
	/**
	 * Définis les actions relatives au jardinier
	 */
	@Override
	public void setActions() {
		// TODO Auto-generated method stub
		this.actions = new Action[9];
		//Déplacer le jardinier
		this.actions[0] = new Action(0,0) {
			public boolean condition() {
				return true;
			}
			public void execution() {
				Game.getInstance().setActionClickRunning(this);
			}
			@Override
			public Action executionLoop() {
				int result = Gardener.super.moveEvent();
				if (result==1) { // le jardinier a fini son déplacement				
					Gardener.this.setAnim(STATIC);
					return null;
				}
				else if (result==0 & new_case_x!=-1) //le jardinier est sur une case de manière 'stable' mais doit changer de direction
				{
					Gardener.this.pathFinding(new_case_x,new_case_y);
					new_case_x=-1;
					new_case_y=-1;
				}
				return this;
			}
			public void action2Click(int x, int y) {
				if (currentAction!=this) {	//le jardinier n'était pas en train de se déplacer
					Gardener.this.pathFinding(x/Settings.SIZE_CASE,y/Settings.SIZE_CASE);
					Gardener.this.currentAction=this;
					Gardener.this.setAnim(MOVING);
					Gardener.this.changeAnim();
				}
				else {
					new_case_x=x/Settings.SIZE_CASE;
					new_case_y=y/Settings.SIZE_CASE;
				}
			}
			public void setName() {
				this.name="actionmove";
			}
		};
		//Créer un bâtiment principal
		this.actions[1] = new Action(40,0) {
			public boolean condition() {
				return currentAction==null && Map.getInstance().canConstructOn(case_x, case_y,Gardener.this) && Game.getInstance().getNumberCoin()>=Building.getCost(Building.MAIN_BUILDING);
			}
			public void execution() {
				if (condition()) {
					this.timerMaxAction=2000;
					Game.getInstance().buyBuilding(Building.MAIN_BUILDING);
					Gardener.this.currentAction=this;
					Gardener.this.setAnim(BUILDING);
					Gardener.this.stepConstruction=0;
					changeAnim();
				}
			}
			@Override
			public Action executionLoop() {
				timer++;
				//Animation de construction
				if (timer%(timerMaxAction/5)==0) {
					stepConstruction++;
					indexAnimList=4+stepConstruction;
					changeAnim();
				}
				if (timer>=timerMaxAction) {
					timer=0;			
					//On ajoute l'unité et on déplace le jardinier
					Game.getInstance().addUnit(new MainBuilding(Gardener.this.case_x,Gardener.this.case_y));
					Gardener.this.pathFinding(case_x, case_y+1);
					Gardener.this.physics.height=40;
					Gardener.this.physics.width=30;
					return Gardener.this.getAction()[0];
				}
				return this;
			}
			
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionbuildingmain";
			}
		};
		//Créer un bâtiment de défense
		this.actions[2] = new Action(80,0) {
			public boolean condition() {
				return Map.getInstance().canConstructOn(case_x, case_y,Gardener.this) && Game.getInstance().getNumberCoin()>=Building.getCost(Building.DEFENSE_BUILDING);
			}
			public void execution() {
				if (condition()) {
					this.timerMaxAction=1000;
					Game.getInstance().buyBuilding(Building.DEFENSE_BUILDING);
					Gardener.this.currentAction=this;
					Gardener.this.setAnim(BUILDING);
					Gardener.this.stepConstruction=0;
					changeAnim();
				}
			}
			@Override
			public Action executionLoop() {
				timer++;
				if (timer%(timerMaxAction/5)==0) {
					stepConstruction++;
					indexAnimList=4+stepConstruction;
					changeAnim();
				}
				if (timer>=timerMaxAction) {
					timer=0;				
					Game.getInstance().addUnit(new DefenseBuilding(Gardener.this.case_x,Gardener.this.case_y));
					Gardener.this.pathFinding(case_x, case_y+1);
					Gardener.this.physics.height=40;
					Gardener.this.physics.width=30;
					return Gardener.this.getAction()[0];
				}
				return this;
			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionbuildingdefense";
			}
		};
		//Créer un bâtiment de production
		this.actions[6] =  new Action(120,0) {
			public boolean condition() {
				return Map.getInstance().canConstructOn(case_x, case_y,Gardener.this) && Game.getInstance().getNumberCoin()>=Building.getCost(Building.PRODUCTION_BUILDING);
			}
			public void execution() {
				if (condition()) {
					this.timerMaxAction=1000;
					Game.getInstance().buyBuilding(Building.PRODUCTION_BUILDING);
					Gardener.this.currentAction=this;
					Gardener.this.setAnim(BUILDING);
					changeAnim();
					Gardener.this.stepConstruction=0;
				}
			}
			@Override
			public Action executionLoop() {
				timer++;
				if (timer%(timerMaxAction/5)==0) {
					stepConstruction++;
					indexAnimList=4+stepConstruction;
					changeAnim();
				}
				if (timer>=timerMaxAction) {
					timer=0;				
					Game.getInstance().addUnit(new ProductionBuilding(Gardener.this.case_x,Gardener.this.case_y));
					Gardener.this.pathFinding(case_x, case_y+1);
					Gardener.this.physics.height=40;
					Gardener.this.physics.width=30;
					return Gardener.this.getAction()[0];
				}
				return this;
			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionbuildingproduction";
			}
		};
		//Planter une fleur rouge
		this.actions[3] = new Action(0,40) {
			public boolean condition() {
				return 	Game.getInstance().getNumberSeedsType(Seed.TYPE_1)>=1 && Map.getInstance().canConstructOn(case_x, case_y,Gardener.this);
			}
			public void execution() {
				if (condition()) {
					Gardener.this.setAnim(PLANTING_FLOWER);
					changeAnim();
					Gardener.this.currentAction=this;
					this.timerMaxAction=100;
				}
			}
			@Override
			public Action executionLoop() {
				timer++;
				if (timer>=timerMaxAction) {
					timer=0;				
					Game.getInstance().getNumberSeeds()[Seed.TYPE_1]-=1;
					Game.getInstance().addUnit(new Flower(Gardener.this.case_x,Gardener.this.case_y, Flower.FLOWER_RED),0);
					Gardener.this.setAnim(STATIC);
					return null;
				}
				return this;
			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionplantflower";
			}
		};
		//Planter une fleur bleue
		this.actions[7] = new Action(40,40) {
			public boolean condition() {
				return 	Game.getInstance().getNumberSeedsType(Seed.TYPE_2)>=1 && Map.getInstance().canConstructOn(case_x, case_y,Gardener.this);
			}
			public void execution() {
				if (condition()) {
					Game.getInstance().getNumberSeeds()[Seed.TYPE_2]-=1;
					Game.getInstance().addUnit(new Flower(Gardener.this.case_x,Gardener.this.case_y, Flower.FLOWER_BLUE),0);	
					Gardener.this.setAnim(PLANTING_FLOWER);
					changeAnim();}
			}
			public void action2Click(int x, int y) {

			}

			public void setName() {
				this.name="actionplantflowerblue";
			}
		};
		//Planter une fleur violette
		this.actions[8] = new Action(80,40) {
			public boolean condition() {
				return 	Game.getInstance().getNumberSeedsType(Seed.TYPE_3)>=1 && Map.getInstance().canConstructOn(case_x, case_y,Gardener.this);
			}
			public void execution() {
				if (condition()) {
					Game.getInstance().getNumberSeeds()[Seed.TYPE_3]-=1;
					Game.getInstance().addUnit(new Flower(Gardener.this.case_x,Gardener.this.case_y, Flower.FLOWER_VIOLET),0);	
					Gardener.this.setAnim(PLANTING_FLOWER);
					changeAnim();
				}
			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionplantflowerviolet";
			}
		};
		//Créer un bouquet
		this.actions[4] = new Action(0,80) {
			public boolean condition() {
				return (Gardener.this.mainBuildingContainingGardener!=null && Game.getInstance().hasFlowerOfEachType());
			}
			public void execution() {
				if (condition()) {
					this.timerMaxAction=200;
					Gardener.this.currentAction = this;
					Gardener.this.setAnim(COMPOSE_BOUQUET);
					changeAnim();
				}
			}
			public void action2Click(int x, int y) {

			}
			@Override
			public Action executionLoop() {
				timer++;
				if (timer>=timerMaxAction) {
					timer=0;				
					Game.getInstance().addBouquetToSell();
					Game.getInstance().removeOneFlowerOfEachType();
					Gardener.this.setAnim(STATIC);
					changeAnim();
					return null;
				}
				return this;
			}
			public void setName() {
				this.name="actionbouquet";
			}
		};
		//Effrayer les lapins
		this.actions[5] = new Action(120,40) {
			public boolean condition() {
				return true;
			}
			public void execution() {
				if (condition()) {
					this.timerMaxAction=200;
					Gardener.this.setAnim(SCARE_RABBIT);
					changeAnim();
					Gardener.this.currentAction=this;
				}
			}
			public void action2Click(int x, int y) {

			}
			@Override
			public Action executionLoop() {
				timer++;
				for (Unit u : Game.getInstance().getListUnit()) {
					if (u instanceof Rabbit) {
						((Rabbit) u).scareRabbit(Gardener.this.case_x, Gardener.this.case_y);
					}
				}
				if (timer>=timerMaxAction) {
					timer=0;				
					Gardener.this.setAnim(STATIC);
					changeAnim();
					return null;
				}
				return this;
			}
			public void setName() {
				this.name="actionscarerabbit";
			}
		};
	}

	/**
	 * Le jardinier cueille une fleur
	 * @param type
	 */
	public void harvestFlower(int type) {
		this.indexAnimList = 10+type;
		setAnim(HARVEST_FLOWER);
		changeAnim();
	}
	
	/**
	 * Définis le nom de l'unité
	 */
	@Override
	protected void setNameUnit() {
		// TODO Auto-generated method stub
		this.nameUnit="gardener30";
		this.nameUnitNormal="gardener30";
		this.nameSelected="gardenermovecursor";
		this.nameCursorMove="gardenerselected";
	}
	
	/**
	 * Retourne la valeur de stateGardener
	 * @return
	 */
	public int getState() {
		return stateGardener;
	}
	
	/**
	 * Donne une nouvelle valeur à stateGardener
	 * @param value
	 */
	public void setState(int value) {
		stateGardener=value;
	}
	
	/**
	 * Retire toutes les fleurs que le jardinier possède
	 */
	public void clearFlower() {
		this.flowerContained.clear();
	}
	
	/**
	 * Renvoie la liste des fleurs que le jardinier possède
	 * @return
	 */
	public List<Integer> getListFlower() {
		return this.flowerContained;
	}
	
	/**
	 * Définis la nouvelle animation
	 * @param indexAnimList
	 */
	public void setAnim(int indexAnimList) {
			TIMER_ANIM=25;
			switch (indexAnimList) {
			case STATIC :
				totalNumberAnim = 0;
				this.indexAnimList = 3;
				Gardener.this.physics.height=40;
				Gardener.this.physics.width=30;
				break;
			case MOVING :
				this.totalNumberAnim = 4;
				Gardener.this.physics.height=40;
				Gardener.this.physics.width=30;
				break;
			case HARVEST_FLOWER :
				totalNumberAnim=3;
				TIMER_ANIM=50;
				Gardener.this.physics.height=50;
				Gardener.this.physics.width=50;
				onetimeanim=true;
				break;
			case PLANT_FLOWER :
				totalNumberAnim=3;
				this.indexAnimList=9;
				Gardener.this.physics.height=50;
				Gardener.this.physics.width=50;
				break;
			case COMPOSE_BOUQUET :
				totalNumberAnim=4;
				TIMER_ANIM=50;
				this.indexAnimList=14;
				Gardener.this.physics.height=40;
				Gardener.this.physics.width=30;
				break;
			case SCARE_RABBIT : 
				totalNumberAnim=2;
				this.indexAnimList=13;
				Gardener.this.physics.height=40;
				Gardener.this.physics.width=30;
				break;				
			case BUILDING :
				totalNumberAnim=4;
				this.indexAnimList=4;
				Gardener.this.physics.height=50;
				Gardener.this.physics.width=50;
				break;
			}
		
	}
	
	/**
	 * Gestion des animations
	 */
	private void handleAnim() {
		timerAnim++;
		if (timerAnim>=TIMER_ANIM) {
			timerAnim=0;
			changeAnim();
		}
	}

	/**
	 * Change l'animation en cours
	 */
	private void changeAnim() {
		if ((!this.nameUnit.equals(this.nameCursorMove)&&!(this.nameUnit.equals(this.nameSelected)&&this.vx==0&&this.vy==0))||indexAnimList>=4) {
			indexAnim++;
			if (indexAnim>=totalNumberAnim && !onetimeanim) {
				indexAnim=0;
			}
			else if (indexAnim>=totalNumberAnim) {
				setAnim(STATIC);
				onetimeanim=false;
			}
			this.nameUnit="gardener"+indexAnimList+""+indexAnim;
		}
	}
	
	/**
	 * Redéfinition de la gestion de l'animation quand il y a un passage avec la souris
	 */
	@Override
	public void setAnimMouseMove() {
		if (!this.nameUnit.equals(this.nameSelected) && indexAnimList<=3)
			this.nameUnit=this.nameCursorMove;
	}
	
	/**
	 * Définis la direction de l'animation quand le personnage se déplace en diagonale
	 */
	private void setAnimDiagonal() {
		switch (currentDirection) {
		case 4:
			indexAnimList=0;
			break;
		case 5:
			indexAnimList=3;
			break;
		case 6:
			indexAnimList=1;
			break;
		case 7:
			indexAnimList=2;
			break;
		}
	}
	
	/**
	 * Définis l'ordre de priorité au niveau du click sur les unités
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Retourne le coût de l'achat d'un jardinier
	 * @return
	 */
	public static int getCost() {
		return 20;
	}
}
