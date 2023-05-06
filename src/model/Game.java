package model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import controller.MouseController;

/**
 * Classe Game, qui contient les fonctions principales liée à la boucle de jeu
 *
 */
public class Game {
	//Type de ressources (pour l'accès aux scores)
	public static final int FLOWER = 0;
	public static final int SEED = 1;
	public static final int COIN = 2;
	//dit si le jeu a commencé ou non
	public static boolean hasBegun = false;
	
	private static Game instanceGame = new Game();
	private Map map;
	private List<Unit> listObjectGame = new LinkedList<>();
	private List<Action> listCurrentActionsGame = new LinkedList<>();
	private List<Unit> listUnitToRemove = new LinkedList<>();
	private List<Unit> listUnitToAdd = new LinkedList<>();
	private int numberBouquetToSell = 0;
	private int[] numberFlower = {0,0,0};
	private int[] numberSeeds = {3,3,3};
	private int numberCoin = 30;
	public String message="message";
	
	//Une action qui nécessite un 2ème click (exemple : déplacer le jardinier)
	private Action actionClickRunning = null;
	
	//Arrivée des lapins dans le jeu
	private static final int TIMER_RABBIT = 300;
	private int timerRabbit = 0;
	private int numberFlowerInGame = 0;
	private int numberRabbit = 0;
	
	//Gestion du temps
	private int initialTime;
	private int currentTime;
	
	//Nom de l'unité actuelle sélectionnée
	public String nameUnit;
	public double modifierWidth;
	/**
	 * Fonction constructeur
	 */
	private Game() {
		initialTime = (int)((new Date()).getTime()/1000);
	}

	/**
	 * Initialise une nouvelle partie
	 */
	public void initialize() {
		hasBegun=true;
		listObjectGame.clear();
		listCurrentActionsGame.clear();
		numberBouquetToSell=0;
		numberFlower = new int[]{0,0,0};
		numberSeeds = new int[]{3,3,3};
		numberCoin = 50;
		initialTime = (int)((new Date()).getTime()/1000);
		MainBuilding g2 = new MainBuilding(6,5);
		listObjectGame.add(g2);
	}
	
	/**
	 * Retourne une valeur disant si une action nécessitant un deuxième click est en cours
	 * @return
	 */
	public boolean isActionClickRunningPresent() {
		return actionClickRunning!=null;
	}
	
	/**
	 * Fonction qui retourne l'unique instance du jeu
	 * @return l'instance du jeu
	 */
	public static Game getInstance() {
		return instanceGame;
	}
	
	/**
	 * Retourne le nombre de graines en fonction du type de la fleur
	 * @return
	 */
	public int getNumberSeedsType(int type) {
		return numberSeeds[type];
	}

	/**
	 * Retourne le tableau du nombre de graines
	 * 	 * @return
	 */
	public int[] getNumberSeeds() {
		return numberSeeds;
	}
	
	/**
	 * Retourne le nombre de pièces
	 * @return
	 */
	public int getNumberCoin() {
		return numberCoin;
	}
	
	/**
	 * Cette fonction cache toutes les actions affichées actuellement à l'écran
	 */
	public void hideAllActions() {
		listCurrentActionsGame.clear();
	}
	
	/**
	 * Cette fonction ajoute le contenu d'un tableau d'actions à la liste des actions que le joueur peut effectuer
	 * @param actions
	 */
	public void addActions(Action[] actions) {
		for (Action a : actions ) {
			this.listCurrentActionsGame.add(a);
		}
	}
	
	/**
	 * Gestion du click sur un endroit du terrain
	 * @param x
	 * @param y
	 */
	public void actionClickPlayground(int x, int y) {
		if (actionClickRunning != null) {
			actionClickRunning.action2Click(x, y);
			this.setActionClickRunning(null);
		}
		else {
			Unit uAction = null;
			for (Unit u : listObjectGame) {
				if (u.isContained(x,y)) {
					if (uAction==null || u.orderPriority()<=uAction.orderPriority()) {
						uAction = u;
					}
				}
				u.setAnimNormal(true);
			}
			if (uAction!=null) {
				this.hideAllActions();
				if (uAction.getAction()!=null) {
					this.addActions(uAction.getActions());
				}
				this.setNameImg(uAction.nameUnitNormal);
				this.modifierWidth = (double)uAction.getW()/(double)uAction.getH();
				uAction.setAnimClick();
			}
		}
	}
	
	private void setNameImg(String nameUnit) {
		this.nameUnit=nameUnit;
	}

	/**
	 * Gestion des animations lors du passage de la souris
	 * @param x
	 * @param y
	 */
	public void actionMoveMousePlayground(int x, int y) {
			for (Unit u : listObjectGame) {
				if (u.isContained(x,y) && u.clickable()) {
					u.setAnimMouseMove();
				}
				else {
					u.setAnimNormal(false);
				}
			}
		
	}
	
	/**
	 * Calcule quels actions sont affectées par les évènements de click
	 * @param x
	 * @param y
	 */
	public void actionClickActions(int x, int y) {
		for (Action act : listCurrentActionsGame) {
			if (act.isContained(x,y)) {
				act.execution();
				return;
			}
		}
	}
	
	/**
	 * Calcule quels actions sont affectées par les évènements de MousePressed
	 * @param x
	 * @param y
	 */
	public void actionPressActions(int x, int y) {
		for (Action act : listCurrentActionsGame) {
			if (act.isContained(x,y)) {
				act.setPressed(true);
				return;
			}
		}
	}
	
	/**
	 * Relâchement de la souris
	 * @param x
	 * @param y
	 */
	public void actionReleasedActions() {
		for (Action act : listCurrentActionsGame) {
			act.setPressed(false);
		}
	}
	
	/**
	 * Cette fonction définit l'action nécessitant un 2ème click étant en cours
	 * @param a
	 */
	public void setActionClickRunning(Action a) {
		this.actionClickRunning = a;
	}
	
	/**
	 * Retourne la liste des unités du jeu
	 * @return
	 */
	public List<Unit> getListUnit() {
		return listObjectGame;
	}
	
	/**
	 * Boucle du jeu
	 */
	public void run() {
		// TODO Auto-generated method stub
		for (Unit u : listUnitToRemove) {
			listObjectGame.remove(u);
		}
		for (Unit u : listUnitToAdd) {
			listObjectGame.add(u);
		}
		listUnitToRemove.clear();
		listUnitToAdd.clear();
		for (Unit u : listObjectGame) {
			u.handleEvent();
		}
		//Arrivée des lapins dans le jeu
		handleRabbit();
		//Gestion du temps
		actualizeTime();
		
	}

	/**
	 * Retourne la liste d'action actuelle que le joueur peut effectuer
	 * @return
	 */
	public List<Action> getActions() {
		// TODO Auto-generated method stub
		return this.listCurrentActionsGame;
	}

	/**
	 * Supprime une unité du jeu
	 * @param u
	 */
	public void removeUnit(Unit u) {
		if (u instanceof Flower) {
			numberFlowerInGame--;
		}
		else if (u instanceof Rabbit) {
			numberRabbit--;
		}
		listUnitToRemove.add(u);
	}

	/**
	 * Ajoute une unité au jeu
	 * @param u
	 */
	public void addUnit(Unit u) {
		if (u instanceof Flower) {
			numberFlowerInGame++;
		}
		else if (u instanceof Rabbit) {
			numberRabbit++;
		}
		listUnitToAdd.add(u);
	}
	
	/**
	 * Ajoute une unité au jeu à la position spécifiée
	 * @param u
	 * @param index
	 */
	public void addUnit(Unit u, int index) {
		if (u instanceof Flower) {
			numberFlowerInGame++;
		}
		listUnitToAdd.add(index,u);
	}
	
	/**
	 * Collecte d'une fleur
	 * @param type
	 */
	public void harvestFlower(int type) {
		numberFlower[type]+=1;
	}
	
	/**
	 * Retourne une donnée du jeu, comme par exemple le nombre de fleurs d'un certain type, le nombre de graines d'un certain type
	 * ou le nombre de pièces actuelle
	 * @param type
	 * @param type2
	 * @return
	 */
	public int getStat(int type, int type2) {
		switch (type) {
		case COIN : 
			return numberCoin;
		case FLOWER :
			return numberFlower[type2];
		case SEED :
			return numberSeeds[type2];
		}
		return -1;
	}

	/**
	 * Achète des graines d'un certain type
	 * @param i
	 */
	public void buySeeds(int i) {
		// TODO Auto-generated method stub
		this.numberCoin-=Seed.getCost(i);
		this.numberSeeds[i]+=3;
	}

	/**
	 * Achète un jardinier
	 * @param i
	 */
	public void buyGardener(int x, int y) {
		// TODO Auto-generated method stub
		Gardener g = new Gardener(x, y);
		Game.getInstance().addUnit(g);
		this.numberCoin-=Gardener.getCost();
	}
	
	/**
	 * Incrémente le nombre de pièces par une certaine valeur
	 * @param val
	 */
	public void addCoin(int val) {
		// TODO Auto-generated method stub
		this.numberCoin+=val;
	}

	/**
	 * Cette fonction dit si le joueur possède une fleur de chaque type
	 * @return
	 */
	public boolean hasFlowerOfEachType() {
		// TODO Auto-generated method stub
		return (numberFlower[0]>=1 && numberFlower[1]>=1 && numberFlower[2]>=1);
	}

	/**
	 * Cette fonction supprime une fleur de chaque type
	 */
	public void removeOneFlowerOfEachType() {
		// TODO Auto-generated method stub
		for (int i = 0; i<3;i++) {
			this.numberFlower[i]-=1;
		}
	}
	
	/**
	 * Cette fonction retourne le nombre de bouquet qui sont actuellement en vente
	 * @return
	 */
	public int getNumberBouquetToSell() {
		return this.numberBouquetToSell;
	}
	
	/**
	 * Cette fonction ajoute un bouquet aux bouquets étant en vente
	 */
	public void addBouquetToSell() {
		numberBouquetToSell++;
	}
	
	/**
	 * Cette fonction supprime tous les bouquets en vente
	 */
	public void removeNumberBouquet() {
		this.numberBouquetToSell=0;
	}
	
	/**
	 * Cette fonction gère l'arrivée des lapins
	 */
	public void handleRabbit() {
		timerRabbit++;
		if (timerRabbit>=Game.TIMER_RABBIT) {
			timerRabbit=0;
			if (numberFlowerInGame-numberRabbit>0) {
				Rabbit newRabbit = new Rabbit(13,13-(int)(Math.random()*3));
				newRabbit.seekClosestFlower();
				this.addUnit(newRabbit);
			}
			if (numberFlowerInGame<0) {
				numberFlowerInGame=0;
			}
		}
	}

	/**
	 * Construit un bâtiment
	 * @param i
	 */
	public void buyBuilding(int i) {
		// TODO Auto-generated method stub
		this.numberCoin-=Building.getCost(i);;
	}
	
	/**
	 * Actualise le temps qui s'écoule depuis le début de la partie
	 */
	public void actualizeTime() {
		currentTime = (int)((new Date()).getTime()/1000) - initialTime;
		if (currentTime>=300) {
			if (this.numberCoin>=200) {
				Game.hasBegun=false;
				message="messagecongratulations";
			}
			else {
				Game.hasBegun=false;
				message="messagegameover";
			}
		}
	}
	
	/**
	 * Retourne le nombre de secondes qui se sont écoulées depuis le début de la partie
	 * @return
	 */
	public int getCurrentTime() {
		return currentTime;
	}

	/**
	 * Dit si le jeu a commencé ou non
	 * @return
	 */
	public boolean hasBegun() {
		// TODO Auto-generated method stub
		return hasBegun;
	}
}
