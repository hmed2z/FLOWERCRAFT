package model;

/**
 * Building est une classe représentant un bâtiment sur la map
 *
 */
public abstract class Building extends Unit {
	public static final int MAIN_BUILDING = 0;
	public static final int DEFENSE_BUILDING = 1;
	public static final int PRODUCTION_BUILDING = 2;
	
	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param width
	 * @param height
	 */
	public Building(int case_x, int case_y, int width, int height) {
		super(case_x, case_y, width, height);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Retourne le coût du bâtiment de type donné
	 * @param typeBuilding
	 * @return
	 */
	public static int getCost(int typeBuilding) {
		switch (typeBuilding) {
		case Building.MAIN_BUILDING :
			return 50;
		case Building.PRODUCTION_BUILDING :
			return 40;
		case Building.DEFENSE_BUILDING :
			return 30;
		}
		return 0;
	}

}
