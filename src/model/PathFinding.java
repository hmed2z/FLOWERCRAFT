package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import model.Map.Node;

/**
 * Cette classe contient toutes les fonctions utiles pour la recherche du chemin le plus court entre 2 points, en utilisant 
 * l'algorithme de pathfinding A*
 */
public class PathFinding {
	//constantes de direction
	private static final int LEFT = 0;
	private static final int UP = 1;
	private static final int RIGHT = 2;
	private static final int DOWN = 3;
	private static final int LEFT_UP = 4;
	private static final int LEFT_DOWN = 5;
	private static final int RIGHT_UP = 6;
	private static final int RIGHT_DOWN = 7;
	
	//Liste ouverte et liste fermée, utilisée pour l'algorithme de pathfinding A*
	static List<Node> listOpen = new LinkedList<>();
	static List<Node> listClosed = new LinkedList<>();
	
	/**
	 * Cette fonction recherche le chemin le plus court et renvoie une liste de direction
	 * @param can_enter_building 
	 * @return une liste de direction
	 */
	public static List<Integer> findPath(Node startingNode, Node endNode, Map m, boolean can_enter_building) {
		listOpen.clear();
		listClosed.clear();
		m.clearQuality();
		List<Integer> returnTab = new LinkedList<>();
		boolean found = false;
		Node actNode = startingNode;
		listClosed.add(startingNode);
		//Tant qu'on n'est pas arrivé au noeud final, on applique l'algorithme suivant :
		while (!found) {
			//On récupère les voisins du noeud courant
			List<Node> neighBoor = actNode.getNeighboor();
			for (Node n : neighBoor) {
				//Pour chaque voisin, s'il est déjà contenu dans liste fermée ou qu'on ne peut pas marcher dessus, on passe
				//Sinon, s'il n'est pas contenu dans la liste ouverte, on l'ajoute à la liste ouverte
				//S'il est contenu dans la liste ouverte, on tente de minimiser sa qualité en utilisant le noeud courant comme parent
				//si cela est possible
				
				if (!listClosed.contains(n) && (n.getWalkable()||(n==endNode && n.isMainBuilding() && can_enter_building))) {
					if (listOpen.contains(n)) {
						double newgcote = n.getGCost()-actNode.getGCost()-Settings.SIZE_CASE;
						if (newgcote>0) {
							n.setParentNode(actNode);
							n.setGCost();
							n.setQuality();
						}
					}
					else {
						n.setParentNode(actNode);
						n.setHValue(endNode);
						n.setGCost();
						n.setQuality();
						listOpen.add(n);
					}
				}
			}
			
			//Si la liste ouverte est vide, cela signifie qu'il n'y a pas de solutions
			if (listOpen.size()==0) {
				return new LinkedList<>();
			}
			
			//La liste ouverte n'étant pas vide, on récupère son élément de qualité la plus basse possible afin de l'ajouter 
			//à la liste fermée
			Node nodeToInsert = listOpen.get(0);
			for (Node n : listOpen) {
				if (n.getQuality()<=nodeToInsert.getQuality()) {
					nodeToInsert = n;
				}
			}
			listOpen.remove(nodeToInsert);
			listClosed.add(nodeToInsert);
			
			//On définit le noeud courant comme le dernier noeud ayant été ajouté
			actNode=nodeToInsert;
			if (actNode == endNode) {
				found=true;
			}
		}
		
		//On remonte parmis les noeuds parents pour construire le chemin
		List<Node> path = new LinkedList<>();
		path.add(actNode);
		while (true) {
			actNode = actNode.getParentNode();
			path.add(actNode);
			if (actNode==startingNode) {
				break;
			}
		}
		
		return getListDirection(path);
	}
	
	/**
	 * Cette fonction retourne la liste des directions nécessaires à emprunter pour parcourir la liste donnée
	 * @param listNode
	 * @return
	 */
	private static List<Integer> getListDirection(List<Node> listNode) {
		List<Integer> returnTab = new LinkedList<>();
		for (int i = listNode.size()-1; i>0;i--) {
			returnTab.add(getDirection(listNode.get(i),listNode.get(i-1)));
		}
		return returnTab;
	}
	
	/**
	 * Cette fonction retourne la direction à emprunter pour passer du noeud 1 au noeud 2
	 * @param n1
	 * @param n2
	 * @return
	 */
	private static int getDirection(Node n1, Node n2) {
		String result = "";
		if (n1.x-n2.x>0) {
			result+="L";
		}
		else if (n1.x-n2.x<0) {
			result+="R";
		}
		if (n1.y-n2.y>0) {
			result+="U";
		}
		else if (n1.y-n2.y<0) {
			result+="D";
		}
		
		switch (result) {
			case "L":
				return LEFT;
			case "R":
				return RIGHT;
			case "LU" :
				return LEFT_UP;
			case "LD" :
				return LEFT_DOWN;
			case "RU" :
				return RIGHT_UP;
			case "RD" :
				return RIGHT_DOWN;
			case "U" :
				return UP;
			case "D" :
				return DOWN;
		}
		
		return -1;
	}
	
	/**
	 * Cette fonction calcule la distance entre 2 points (x1,y1) et (x2,y2)
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return la distance entre les 2 points
	 */
	public static double calculateDist(int x1, int x2, int y1, int y2) {
		return Math.sqrt(400*(x1-x2)*(x1-x2)+400*(y1-y2)*(y1-y2));
	}
}
