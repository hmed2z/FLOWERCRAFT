package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Map du jeu
 *
 */
public class Map {
	//Instance de la map
	private static Map instanceMap = new Map();
	//Constantes
	private static final boolean[] WALKABLE_VALUE = {true,false,false};
	private static final boolean[] BUILDING_VALUE = {false,true,false};

	//Map du jeu, repr�sent�e par des noeuds ayant des liens entre eux
	Node[][] map = new Node[14][14];
	
	/**
	 * Fonction constructeur
	 */
	private Map() {
		emptyMap();
	}
	
	/**
	 * Retourne l'instance de la map
	 * @return
	 */
	public static Map getInstance() {
		return instanceMap;
	}
	
	/**
	 * Transforme la map en une map vide (les objets du jeu doivent avoir tous �t� supprim�s auparavant)
	 */
	public void emptyMap() {
		for (int i = 0; i<14; i++) {
			for (int j = 0; j<14; j++) {
				map[i][j] = new Node(i,j);
			}
		}
		map[6][5].setValue(1);
		setNeighboorNode();
	}
	
	/**
	 * Associe chaque noeud � ses voisins
	 */
	public void setNeighboorNode() {
		for (int i = 0; i<14; i++) {
			for (int j = 0; j<14; j++) {
				Node n = map[i][j];
				if (i!=0) {
					n.linkNodeNeighboor(map[i-1][j]);
				}
				if (i!=0 && j!=0) {
					n.linkNodeNeighboor(map[i-1][j-1]);
				}
				if (i!=0 && j!=13) {
					n.linkNodeNeighboor(map[i-1][j+1]);
				}
				if (j!=0) {
					n.linkNodeNeighboor(map[i][j-1]);
				}
				if (j!=13) {
					n.linkNodeNeighboor(map[i][j+1]);
				}
				if (i!=13) {
					n.linkNodeNeighboor(map[i+1][j]);
				}
				if (i!=13 && j!=0) {
					n.linkNodeNeighboor(map[i+1][j-1]);
				}
				if (i!=13 && j!=13) {
					n.linkNodeNeighboor(map[i+1][j+1]);
				}				
			}
		}
	}
	
	
	/**
	 * Fonction qui dit si une unit� d�pla�able peut se d�placer sur une certaine case
	 * @param x coordonn�e de la case sur la map selon l'axe x
	 * @param y coordonn�e de la case sur la map selon l'axe y
	 * @return
	 */
	public boolean canMoveOn(int x, int y) {
		return true;
	}
	
	/**
	 * Cette fonction retourne le noeud situ� sur la case x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public Node getNode(int x, int y) {
		try {
			return map[x][y];
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Cette fonction retourne une valeur indiquant si la case est enti�rement vide (pas de fleurs, de jardinier, de b�timent) ou non
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canConstructOn(int x, int y, Unit exception) {
		if (!this.getNode(x, y).getWalkable()) {
			return false;
		}
		for (Unit u : Game.getInstance().getListUnit()) {
			if (u.case_x==x && u.case_y==y && u!=exception) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Classe Node, un noeud qui repr�sente une case sur la map du jeu 
	 *
	 */
	public class Node {
		//position du noeud sur la map
		int x, y;
		//valeur du noeud
		int value;
		//voisins du noeud
		List<Node> neighBoor = new LinkedList<>();
		//Noeud parent pour le pathfinding
		Node parentNode;
		//qualit� du noeud
		double quality;
		int gcost = 0;
		double hcost = 0;
		
		/**
		 * Fonction constructeur de la classe Node
		 * @param x
		 * @param y
		 */
		public Node(int x, int y) {
			this.x=x;
			this.y=y;
		}

		
		/**
		 * D�finis un certain noeud comme voisin de ce noeud
		 * @param d
		 * @param i
		 */
		public void linkNodeNeighboor(Node d) {
			neighBoor.add(d);
		}
		
		/**
		 * Cette fonction dit si une unit� d�pla�able peut se d�placer sur la case repr�sent�e par le noeud ou non
		 * @return
		 */
		public boolean getWalkable() {
			return (WALKABLE_VALUE[value]);
		}
		
		public void setValue(int value) {
			this.value=value;
		}
		
		/**
		 * Cette fonction met � jour la qualit� du noeud
		 * @param quality
		 */
		public void setQuality() {
			this.quality=gcost+hcost;
		}
		
		/**
		 * Cette fonction d�termine la valeur g du noeud (nombre de cases emprunt�es par rapport � la case d'origine)
		 */
		public void setGCost() {
			this.gcost = this.parentNode.gcost+Settings.SIZE_CASE;
		}

		/**
		 * Cette fonction d�termine la valeur g du noeud (nombre de cases emprunt�es par rapport � la case d'origine)
		 * @param val
		 */
		public void setGCost(int val) {
			this.gcost = val;
		}
		
		/**
		 * Cette fonction retourne la valeur g du noeud (nombre de cases emprunt�es par rapport � la case d'origine)
		 * @return
		 */
		public int getGCost() {
			return this.gcost;
		}
		
		/**
		 * Cette fonction d�termine la valeur heuristique du noeud (distance par rapport � la case d'arriv�e)
		 * @param destination
		 */
		public void setHValue(Node destination) {
			this.hcost=PathFinding.calculateDist(x, destination.x, y, destination.y);
		}
		
		/**
		 * Cette fonction retourne la qualit� du noeud
		 * @return
		 */
		public double getQuality() {
			return this.quality;
		}
		
		/**
		 * Cette fonction retourne les voisins du noeud
		 * @param num
		 * @return
		 */
		public List<Node> getNeighboor() {
			return neighBoor;
		}
		
		/**
		 * Cette fonction d�termine le noeud parent
		 * @param parent
		 */
		public void setParentNode(Node parent) {
			this.parentNode=parent;
		}
		
		/**
		 * Cette fonction retourne le noeud parent
		 * @return
		 */
		public Node getParentNode() {
			return this.parentNode;
		}

		/**
		 * Fonction qui dit si le noeud donn� est un MainBuilding ou non
		 * @return
		 */
		public boolean isMainBuilding() {
			// TODO Auto-generated method stub
			return BUILDING_VALUE[value];
		}
	}

	/**
	 * Cette fonction r�initialise la qualit� initiale de tous les noeuds
	 */
	public void clearQuality() {
		// TODO Auto-generated method stub
		for (Node[] listN : map) {
			for (Node n : listN) {
				n.setParentNode(null);
				n.gcost=0;
				n.hcost=0;
				n.setQuality();
			}
		}
	}

	/**
	 * Cette fonction attribue une valeur � un noeud sp�cifique
	 * @param val
	 * @param i
	 * @param j
	 */
	public void setNodeValue(int val, int i, int j) {
		try {
			map[i][j].setValue(val);
		}
		catch(Exception e) {
			
		}
		
	}

}
