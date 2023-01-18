package model;

/**
 * Flower est une classe qui représente une fleur dans le jeu
 *
 */
public class Flower extends Unit{
	//types de fleurs
	public static final int FLOWER_RED = 0;
	public static final int FLOWER_BLUE = 1;
	public static final int FLOWER_VIOLET = 2;
	private int typeFlower;
	
	//timer 
	private static int TIMER_GROW_FLOWER=100;
	private int timerFlower;
	
	//état maximal
	private static int STATE_MAX_FLOWER=7;

	//état actuel
	private int state=1;
	
	/**
	 * Fonction constructeur
	 * @param case_x
	 * @param case_y
	 * @param typeFlower
	 */
	public Flower(int case_x, int case_y, int typeFlower) {
		super(case_x, case_y, 50, 50,-10,-15);
		this.typeFlower = typeFlower;
		setNameUnit();
		setTimerFlower();
	}

	/**
	 * Définis la rapidité à laquelle la fleur va grandir
	 */
	protected void setTimerFlower() {
		switch (typeFlower) {
		case FLOWER_RED:
			TIMER_GROW_FLOWER = 500;
			break;
		case FLOWER_BLUE:
			TIMER_GROW_FLOWER = 400;
			break;
		case FLOWER_VIOLET:
			TIMER_GROW_FLOWER = 300;
			break;
		}
	}
	
	/**
	 * Définis le nom de l'unité
	 */
	@Override
	protected void setNameUnit() {
		// TODO Auto-generated method stub
		this.nameUnit="flowere"+state+""+typeFlower;
		this.nameUnitNormal="flowere"+state+""+typeFlower;
		this.nameSelected="flowere"+state+""+typeFlower;
		this.nameCursorMove="flowere"+state+""+typeFlower;
	}

	/**
	 * Gère les évènements relatifs à la fleur
	 */
	@Override
	public void handleEvent() {
		// TODO Auto-generated method stub
		timerFlower++;
		if (timerFlower>=TIMER_GROW_FLOWER && state<STATE_MAX_FLOWER) { //la fleur grandit
			timerFlower=0;
			state++;
			this.nameUnit="flowere"+state+""+typeFlower;
			if (state==5) {					//après ce stade, la fleur va fanner
				TIMER_GROW_FLOWER = 1000;
			}
			if (state==7) {					//on supprime la fleur
				this.harvest();
			}
		}
	}

	/**
	 * Définis les actions possibels que peut faire l'utilisateur avec la fleur
	 */
	@Override
	public void setActions() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Se déroule quand la fleur est cueillie ou morte
	 */
	public void harvest() {
		Game.getInstance().removeUnit(this);
	}
	
	/**
	 * Dit si une action se déroule quand on click sur l'unité
	 */
	@Override
	public boolean clickable() {
		return false;
	}

	/**
	 * Retourne le type de fleur
	 * @return
	 */
	public Integer getType() {
		// TODO Auto-generated method stub
		return typeFlower;
	}
	
	/**
	 * Retourne l'état de la fleur
	 * @return
	 */
	public Integer getState() {
		return state;
	}
	
	/**
	 * Fait grandir plus vite la fleur
	 */
	public void addTimerFlower(int x, int y) {
		if (Math.abs(x-this.case_x)<=1 && Math.abs(y-this.case_y)<=1 && state<=4) {
			this.timerFlower+=2;
		}

	}
	
	/**
	 * Retourne l'ordre de priorité au niveau des clicks
	 */
	@Override
	public int orderPriority() {
		// TODO Auto-generated method stub
		return 2;
	}
}


