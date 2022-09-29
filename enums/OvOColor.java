/**
 * 
 */
package de.onevsone.enums;

import org.bukkit.Color;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 25.03.2018 18:09:21					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public enum OvOColor {
	
	BLACK("Schwarz", 0, "§0", Color.BLACK),
	RED("Rot", 1, "§4", Color.RED),
	GREEN("Grün", 2, "§2", Color.GREEN),
	BROWN("Braun", 3, "§c", Color.OLIVE),
	BLUE("Blau", 4, "§1", Color.BLUE),
	VIOLET("Violett", 5, "§5", Color.PURPLE),
	TURQUOISE("Türkis", 6, "§3", Color.TEAL),
	LIGHTGRAY("Hellgrau", 7, "§7", Color.SILVER),
	GRAY("Grau", 8, "§8", Color.GRAY),
	PINK("Rosa", 9, "§9", Color.PURPLE),
	LIGHTGREEN("Hellgrün", 10, "§a", Color.LIME),
	YELLOW("Gelb", 11, "§e", Color.YELLOW),
	LIGHTBLUE("Hellblau", 12, "§b", Color.AQUA),
	MAGENTA("Magenta", 13, "§d", Color.FUCHSIA),
	ORANGE("Orange", 14, "§6", Color.ORANGE),
	WHITE("Weiß", 15, "§f", Color.WHITE),
	RANDOM("Zufällig", 16, "§f", Color.WHITE);
	
	
	private String name;
	private int subID;
	private String colorKey;
	private Color bukkitColor;

	private OvOColor(String name, int subID, String colorKey, Color bukkitColor) {
		this.name = name;
		this.subID = subID;
		this.colorKey = colorKey;
		this.bukkitColor = bukkitColor;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSubID() {
		return subID;
	}
	
	public String colorKey() {
		return colorKey;
	}
	
	public Color getColor() {
		return bukkitColor;
	}
	
	public static OvOColor resolveBySubID(int subID) {
		switch (subID) {
		case 0:
			return OvOColor.BLACK;
			
		case 1:
			return OvOColor.RED;
		
		case 2:
			return OvOColor.GREEN;
			
		case 3:
			return OvOColor.BROWN;
		
		case 4:
			return OvOColor.BLUE;
			
		case 5:
			return OvOColor.VIOLET;
		
		case 6:
			return OvOColor.TURQUOISE;
		
		case 7:
			return OvOColor.LIGHTGRAY;
			
		case 8:
			return OvOColor.GRAY;
			
		case 9:
			return OvOColor.PINK;
			
		case 10:
			return OvOColor.LIGHTGREEN;
			
		case 11:
			return OvOColor.YELLOW;
			
		case 12:
			return OvOColor.LIGHTBLUE;
			
		case 13:
			return OvOColor.MAGENTA;
			
		case 14:
			return OvOColor.ORANGE;
			
		case 15:
			return OvOColor.WHITE;

		default:
			return OvOColor.RANDOM;
		}
	}
	
}
