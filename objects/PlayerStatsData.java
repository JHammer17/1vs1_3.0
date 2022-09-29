package de.onevsone.objects;

import java.util.UUID;

public class PlayerStatsData {
	
	private int place;
	private UUID uuid;
	private String name;
	private int wins;
	private int fights;
	private int wins30;
	private int fights30;
	private int wins24;
	private int fights24;

	public PlayerStatsData(int place, 
			UUID uuid, 
			String name, 
			int wins, int fights, 
			int wins30, int fights30,
			int wins24, int fights24) {
		this.place = place;
		this.uuid = uuid;
		this.name = name;
		this.wins = wins;
		this.fights = fights;
		this.wins30 = wins30;
	    this.fights30 = fights30;
	    this.wins24 = wins24;
	    this.fights24 = fights24;
	}

	/**
	 * @return the wins30
	 */
	public int getWins30() {
		return wins30;
	}

	/**
	 * @return the fights30
	 */
	public int getFights30() {
		return fights30;
	}

	/**
	 * @return the place
	 */
	public int getPlace() {
		return place;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the wins
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * @return the fights
	 */
	public int getFights() {
		return fights;
	}
	
	public int getFights24() {
		return fights24;
	}
	
	public int getWins24() {
		return wins24;
	}
	
}