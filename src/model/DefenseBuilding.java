package model;

import java.awt.Rectangle;

/**
 * Cette unité est un bâtiment de défense
 *
 */
public class DefenseBuilding extends Building {

	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param width
	 * @param height
	 */
	public DefenseBuilding(int case_x, int case_y) {
		super(case_x, case_y, 30, 45);
		physics = new Rectangle(case_x*Settings.SIZE_CASE-5,case_y*Settings.SIZE_CASE-25,40,60);
		Map.getInstance().setNodeValue(2, case_x, case_y);

		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Gère les évènements relatifs à l'unité, appelée à chaque tour
	 */
	@Override
	public void handleEvent() {
			for (Unit u : Game.getInstance().getListUnit()) {
				if (u instanceof Rabbit) {
					//Effraie les lapins aux alentours de la case
					((Rabbit) u).scareRabbit(this.case_x-1, this.case_y);
					((Rabbit) u).scareRabbit(this.case_x-1, this.case_y-1);
					((Rabbit) u).scareRabbit(this.case_x-1, this.case_y+1);
					((Rabbit) u).scareRabbit(this.case_x+1, this.case_y);
					((Rabbit) u).scareRabbit(this.case_x+1, this.case_y-1);
					((Rabbit) u).scareRabbit(this.case_x+1, this.case_y+1);
					((Rabbit) u).scareRabbit(this.case_x, this.case_y-1);
					((Rabbit) u).scareRabbit(this.case_x, this.case_y+1);
				}
			}
		
	}
	
	/**
	 * Définis les actions du bâtiment de défense
	 */
	@Override
	public void setActions() {
		//Pas d'actions possibles
	}

	/**
	 * Définis les noms de l'unité
	 */
	@Override
	protected void setNameUnit() {
		// TODO Auto-generated method stub
		this.nameUnit="defensebuilding";
		this.nameUnitNormal="defensebuilding";
		this.nameSelected="defensebuildingselected";
		this.nameCursorMove="defensebuildingmove";
	}

	/**
	 * Définis l'ordre de priorité
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

}
