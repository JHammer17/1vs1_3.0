/**
 * 
 */
package de.onevsone.objects;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 30.03.2018 18:01:26					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class KitStand {

	private ArmorStand stand;
	private String kit;
	private Location loc;
	private int subID;
	private String name;
	private int timed;
	
	
	public KitStand(ArmorStand stand, String kit, int subID, Location loc, String name, int timed) {
		this.stand = stand;
		this.kit = kit;
		this.loc = loc;	
		this.subID = subID;
		this.name = name;
		this.timed = timed;
	}

	/**
	 * @return the stand
	 */
	public ArmorStand getStand() {
		return stand;
	}

	/**
	 * @param stand the stand to set
	 */
	public void setStand(ArmorStand stand) {
		this.stand = stand;
	}

	/**
	 * @return the kit
	 */
	public String getKit() {
		return kit;
	}

	/**
	 * @param kit the kit to set
	 */
	public void setKit(String kit) {
		this.kit = kit;
	}

	/**
	 * @return the loc
	 */
	public Location getLoc() {
		return loc;
	}

	/**
	 * @param loc the loc to set
	 */
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	public void setSubID(int subID) {
		this.subID = subID;
	}
	
	public int getSubID() {
		return this.subID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTimed() {
		return this.timed;
	}
	
	
	
}
