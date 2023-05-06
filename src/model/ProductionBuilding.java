package model;

import java.awt.Rectangle;

/**
 * Bâtiment qui augmente la production de fleurs dans les cases environnantes
 *
 */
public class ProductionBuilding extends Building {
	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param width
	 * @param height
	 */
	public ProductionBuilding(int case_x, int case_y) {
		super(case_x, case_y, 60, 60);
		physics = new Rectangle(case_x*Settings.SIZE_CASE,case_y*Settings.SIZE_CASE-25,60,60);
		Map.getInstance().setNodeValue(2, case_x, case_y);
		Map.getInstance().setNodeValue(2, case_x+1, case_y);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Fonction exécutée à chaque tour, qui gère les éléments relatifs à l'unité
	 */
	@Override
	public void handleEvent() {
		for (Unit u : Game.getInstance().getListUnit()) {
			if (u instanceof Flower) {
				((Flower) u).addTimerFlower(this.case_x, this.case_y);
				((Flower) u).addTimerFlower(this.case_x+1, this.case_y);
			}
		}
	}
	
	/**
	 * Définis les actions que l'utilisateur peut faire
	 */
	@Override
	public void setActions() {
		// Pas d'actions possibles
	}

	/**
	 * Définis les noms de l'unité
	 */
	@Override
	protected void setNameUnit() {
		// TODO Auto-generated method stub
		this.nameUnit="productionbuilding";
		this.nameUnitNormal="productionbuilding";
		this.nameSelected="productionbuildingselected";
		this.nameCursorMove="productionbuildingmove";
	}
	
	/**
	 * Définis les ordres de priorités
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

}
