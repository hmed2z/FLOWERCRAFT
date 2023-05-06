package model;

import java.awt.Rectangle;

/**
 * Cette unit� est un b�timent de d�fense
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
	 * G�re les �v�nements relatifs � l'unit�, appel�e � chaque tour
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
	 * D�finis les actions du b�timent de d�fense
	 */
	@Override
	public void setActions() {
		//Pas d'actions possibles
	}

	/**
	 * D�finis les noms de l'unit�
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
	 * D�finis l'ordre de priorit�
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

}
