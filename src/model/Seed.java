package model;

/**
 * Classe contenant des fonctions statiques par rapport aux graines
 *
 */
public class Seed {
	//types de graines
	public static final int TYPE_1 = 0;
	public static final int TYPE_2 = 1;
	public static final int TYPE_3 = 2;
	
	/**
	 * Coût des graines en fonction de leur type
	 * @param type
	 * @return
	 */
	public static int getCost(int type) {
		switch (type) {
		case TYPE_1 :
			return 5;
		case TYPE_2 :
			return 7;
		case TYPE_3 :
			return 10;
		}
		return 0;
	}
}
