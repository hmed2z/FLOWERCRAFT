package model;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

/**
 * Bâtiment principal
 *
 */
public class MainBuilding extends Building {
	//Liste des jardiniers étant situés dans le bâtiment
	List<Gardener> listGardener = new LinkedList<>();
	
	/**
	 * Fonction constructeur
	 */
	public MainBuilding(int case_x, int case_y) {
		super(case_x, case_y, 40, 40);
		physics = new Rectangle(case_x*Settings.SIZE_CASE-30,case_y*Settings.SIZE_CASE-55,90,75);
		Map.getInstance().setNodeValue(1, case_x, case_y);
		Map.getInstance().setNodeValue(2, case_x-1, case_y);
		Map.getInstance().setNodeValue(2, case_x+1, case_y);
		Map.getInstance().setNodeValue(2, case_x-1, case_y-1);
		Map.getInstance().setNodeValue(2, case_x+1, case_y-1);
		Map.getInstance().setNodeValue(2, case_x, case_y-1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Appelée à chaque tour, gère les évènements relatifs à l'unité
	 */
	@Override
	public void handleEvent() {
		//Vente des bouquets
		for (int i = 0; i<Game.getInstance().getNumberBouquetToSell();i++) {
			sellBouquet();
		}
		Game.getInstance().removeNumberBouquet();
	}
	
	/**
	 * Cette fonction définis les actions possibles du bâtiment
	 */
	@Override
	public void setActions() {
		this.actions = new Action[4];
		//Acheter des graines de 3 types différents
		//Type 1
		this.actions[0] = new Action(0,0) {
			public boolean condition() {
				return Game.getInstance().getNumberCoin()>=Seed.getCost(Seed.TYPE_1);
			}
			public void execution() {
				if (condition()) {
					Game.getInstance().buySeeds(Seed.TYPE_1);
				}

			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionbuyseedstype1";
			}
		};
		//Type 2
		this.actions[1] = new Action(40,0) {
			public boolean condition() {
				return Game.getInstance().getNumberCoin()>=Seed.getCost(Seed.TYPE_2);
			}
			public void execution() {
				if (condition()) {
					Game.getInstance().buySeeds(Seed.TYPE_2);
				}

			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionbuyseedstype2";
			}
		};
		//Type 3
		this.actions[2] = new Action(80,0) {
			public boolean condition() {
				return Game.getInstance().getNumberCoin()>=Seed.getCost(Seed.TYPE_3);
			}
			public void execution() {
				if (condition()) {
					Game.getInstance().buySeeds(Seed.TYPE_3);
					
				}

			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actionbuyseedstype3";
			}
		};
		//Acheter un jardinier
		this.actions[3] = new Action(120,0) {
			public boolean condition() {
				return Game.getInstance().getNumberCoin()>=Gardener.getCost();
			}
			public void execution() {
				if (condition()) {
					Game.getInstance().buyGardener(case_x, case_y);
				}

			}
			public void action2Click(int x, int y) {

			}
			public void setName() {
				this.name="actiongardener";
			}
		};
	}

	/**
	 * Définis le nom de l'unité
	 */
	@Override
	protected void setNameUnit() {
		// TODO Auto-generated method stub
		this.nameUnit="mainbuilding";
		this.nameUnitNormal="mainbuilding";
		this.nameSelected="mainbuildingselected";
		this.nameCursorMove="mainbuildingmove";
	}
	
	/**
	 * Ajoute un jardinier à la liste des jardiniers contenus dans le bâtiment
	 * @param g
	 */
	public void addGardener(Gardener g) {
		if (!this.listGardener.contains(g)) {
			this.listGardener.add(g);
			//Récolte les fleurs que le jardinier a en sa possession actuellement
			for (Integer type : g.getListFlower()) {
				Game.getInstance().harvestFlower(type);
			}
			g.clearFlower();
		}

	}
	
	/**
	 * Supprime un jardinier de la liste des jardiniers contenus dans le bâtiment
	 * @param g
	 */
	public void removeGardener(Gardener g) {
		this.listGardener.remove(g);
	}
	
	/**
	 * Vend un bouquet
	 */
	public void sellBouquet() {
		Game.getInstance().addCoin(30);
	}
	
	/**
	 * Définis la priorité vis-à-vis du click sur l'unité
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 1;
	}
}
