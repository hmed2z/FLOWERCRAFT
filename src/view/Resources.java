package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Classe Resources, s'occupe de la récupération et de la gestion des resources du jeu
 *
 */
public class Resources {
	//Couleurs
	public static Color darkCaseColor = new Color(0xaad751);
	public static Color lightCaseColor = new Color(0xa2d149);
	public static Color backgroundColor = new Color(0x578a34);
	public static Color caseColor = new Color(0xc2ff4a);
	public static Color fontColor = new Color(0xefffd1);
	
	//Polices
	public static Font fontGame;
	
	private static Map<String,Image> mapImage = new HashMap<>();
	
	/**
	 * Charge les images et les polices
	 */
	public static void loadResources() {
		generateTerrain(30,14,14);
		try {
			mapImage.put("title", ImageIO.read(new File("resources/title.png")));
			mapImage.put("box", ImageIO.read(new File("resources/box.png")));
			for (int i = 0;i<4;i++) {
				for (int j = 0; j<3;j++) {
					mapImage.put("gardener"+i+""+j, ImageIO.read(new File("resources/gardener"+i+""+j+".png")));
				}
				mapImage.put("gardener"+i+"3", ImageIO.read(new File("resources/gardener"+i+""+1+".png")));
			}
			for (int i = 4;i<9;i++) {
				for (int j = 0; j<4;j++) {
					mapImage.put("gardener"+i+""+j, ImageIO.read(new File("resources/gardener"+i+""+j+".png")));
				}
			}
			for (int j = 0; j<3;j++) {
				mapImage.put("gardener9"+j, ImageIO.read(new File("resources/gardener9"+j+".png")));
			}
			for (int i = 10;i<13;i++) {
				for (int j = 0; j<3;j++) {
					mapImage.put("gardener"+i+""+j, ImageIO.read(new File("resources/gardener"+i+""+j+".png")));
				}
			}
			for (int j = 0; j<2;j++) {
				mapImage.put("gardener13"+j, ImageIO.read(new File("resources/gardener13"+j+".png")));
			}
			for (int j = 0; j<3;j++) {
				mapImage.put("gardener14"+j, ImageIO.read(new File("resources/gardener14"+j+".png")));
			}
			mapImage.put("gardenerselected", ImageIO.read(new File("resources/gardenerselected.png")));
			mapImage.put("gardenermovecursor", ImageIO.read(new File("resources/gardenerclick.png")));

			mapImage.put("actiongardener", ImageIO.read(new File("resources/actionGardener.png")));
			mapImage.put("actionbackground", ImageIO.read(new File("resources/actionButton.png")));
			mapImage.put("actionbackground2", ImageIO.read(new File("resources/actionButtonE2.png")));
			mapImage.put("actionmove", ImageIO.read(new File("resources/actionmove.png")));
			mapImage.put("actionbuildingmain", ImageIO.read(new File("resources/actionbuilding.png")));
			mapImage.put("actionbuildingdefense", ImageIO.read(new File("resources/actiondefensebuilding.png")));
			mapImage.put("actionbuildingproduction", ImageIO.read(new File("resources/actionproductionbuilding.png")));
			mapImage.put("actionbuyseedstype1", ImageIO.read(new File("resources/actionbuyseedstype1.png")));
			mapImage.put("actionbuyseedstype2", ImageIO.read(new File("resources/actionbuyseedstype2.png")));
			mapImage.put("actionbuyseedstype3", ImageIO.read(new File("resources/actionbuyseedstype3.png")));
			mapImage.put("actionplantflower", ImageIO.read(new File("resources/actionbuttonflower.png")));
			mapImage.put("actionplantflowerblue", ImageIO.read(new File("resources/actionbuttonflowerblue.png")));
			mapImage.put("actionplantflowerviolet", ImageIO.read(new File("resources/actionbuttonflowerviolet.png")));
			mapImage.put("actionbouquet", ImageIO.read(new File("resources/actionbouquet.png")));
			mapImage.put("actionsellbouquet", ImageIO.read(new File("resources/actionsellbouquet.png")));
			mapImage.put("actionscarerabbit", ImageIO.read(new File("resources/scarerabbit.png")));
			mapImage.put("actiondisabled", ImageIO.read(new File("resources/actionButtonDisabledE2.png")));
			mapImage.put("filterdisabled", ImageIO.read(new File("resources/filterdisabled.png")));
			mapImage.put("mainbuilding", ImageIO.read(new File("resources/mainbuilding.png")));
			mapImage.put("mainbuildingmove", ImageIO.read(new File("resources/mainbuildingmousemove.png")));
			mapImage.put("mainbuildingselected", ImageIO.read(new File("resources/mainbuildingselected.png")));
			mapImage.put("defensebuilding", ImageIO.read(new File("resources/defensebuilding.png")));
			mapImage.put("defensebuildingmove", ImageIO.read(new File("resources/defensebuildingmousemove.png")));
			mapImage.put("defensebuildingselected", ImageIO.read(new File("resources/defensebuildingselected.png")));
			mapImage.put("productionbuilding", ImageIO.read(new File("resources/productionbuilding.png")));
			mapImage.put("productionbuildingmove", ImageIO.read(new File("resources/productionbuildingmousemove.png")));
			mapImage.put("productionbuildingselected", ImageIO.read(new File("resources/productionbuildingselected.png")));
			mapImage.put("flowere10", ImageIO.read(new File("resources/flowerstep1.png")));
			mapImage.put("flowere20", ImageIO.read(new File("resources/flowerstep2.png")));
			mapImage.put("flowere30", ImageIO.read(new File("resources/flowerstep3.png")));
			mapImage.put("flowere40", ImageIO.read(new File("resources/flowerstep4.png")));
			mapImage.put("flowere50", ImageIO.read(new File("resources/flowerstep5.png")));
			mapImage.put("flowere60", ImageIO.read(new File("resources/flowerdead.png")));
			mapImage.put("flowere11", ImageIO.read(new File("resources/flowerstep1.png")));
			mapImage.put("flowere21", ImageIO.read(new File("resources/flowerstep2b.png")));
			mapImage.put("flowere31", ImageIO.read(new File("resources/flowerstep3b.png")));
			mapImage.put("flowere41", ImageIO.read(new File("resources/flowerstep4b.png")));
			mapImage.put("flowere51", ImageIO.read(new File("resources/flowerstep5b.png")));
			mapImage.put("flowere61", ImageIO.read(new File("resources/flowerdead.png")));
			mapImage.put("flowere12", ImageIO.read(new File("resources/flowerstep1.png")));
			mapImage.put("flowere22", ImageIO.read(new File("resources/flowerstep2v.png")));
			mapImage.put("flowere32", ImageIO.read(new File("resources/flowerstep3v.png")));
			mapImage.put("flowere42", ImageIO.read(new File("resources/flowerstep4v.png")));
			mapImage.put("flowere52", ImageIO.read(new File("resources/flowerstep5v.png")));
			mapImage.put("flowere62", ImageIO.read(new File("resources/flowerdead.png")));
			mapImage.put("rabbit", ImageIO.read(new File("resources/rabbit.png")));
			mapImage.put("rabbitscared", ImageIO.read(new File("resources/rabbitscared.png")));
			mapImage.put("coin", ImageIO.read(new File("resources/coin.png")));			
			mapImage.put("seed1", ImageIO.read(new File("resources/seed1.png")));	
			mapImage.put("seed2", ImageIO.read(new File("resources/seed2.png")));	
			mapImage.put("seed3", ImageIO.read(new File("resources/seed3.png")));	
			mapImage.put("flower1", ImageIO.read(new File("resources/flowertype1.png")));	
			mapImage.put("flower2", ImageIO.read(new File("resources/flowertype2.png")));	
			mapImage.put("flower3", ImageIO.read(new File("resources/flowertype3.png")));	
			mapImage.put("time", ImageIO.read(new File("resources/time.png")));	
			mapImage.put("message", ImageIO.read(new File("resources/message.png")));	
			mapImage.put("messagecongratulations", ImageIO.read(new File("resources/messagecongratulation.png")));	
			mapImage.put("messagegameover", ImageIO.read(new File("resources/messagegameover.png")));	

			fontGame = Font.createFont(Font.PLAIN, new File("resources/zorque.regular.ttf")).deriveFont(15f);
			
		} catch (IOException | FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne la ressource ayant le nom désiré
	 * @param name
	 * @return
	 */
	public static Image getResources(String name) {
		return mapImage.get(name);
	}
	
	/**
	 * Génère l'image du terrain
	 * @param scase
	 * @param w
	 * @param h
	 */
	public static void generateTerrain(int scase, int w, int h) {
		BufferedImage imgTerrain = new BufferedImage(scase*w, scase*h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)imgTerrain.getGraphics();
		
		g.setColor(darkCaseColor);
		g.fillRect(0, 0, w*scase, h*scase);
		for (int i = 0; i<w; i++) {
			for (int j = 0; j<h; j++) {
				if ((i+j)%2==0) {
					g.setColor(lightCaseColor);
					g.fillRect(i*scase, j*scase, scase, scase);	
				}
			}
		}
		mapImage.put("terrain",imgTerrain);
		
	}
}
