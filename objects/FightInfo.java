package de.onevsone.objects;

import java.util.ArrayList;
import java.util.UUID;

public class FightInfo {

	private UUID id;
	private ArrayList<UUID> pos1List;
	private ArrayList<UUID> pos2List;
	private ArrayList<String> pos1Names;
	private ArrayList<String> pos2Names;
	private boolean pos1Wins;
	private String colorPos1;
	private String colorPos2;
	private ArrayList<Float> pos1Health;
	private ArrayList<Float> pos2Health;
	private String arena;
	private String map;
	private boolean ranked;
	private UUID tournament;
	private String tName;
	private String itemName;
	private int subID;
	private String kit;
	private int duration;
	private ArrayList<Integer> kills1;
	private ArrayList<Integer> kills2;
	private long time;

	public FightInfo(long time, UUID id, 
					ArrayList<UUID> pos1List, ArrayList<UUID> pos2List,
					ArrayList<String> pos1Names, ArrayList<String> pos2Names,
					boolean pos1Wins, String colorPos1, String colorPos2,
					ArrayList<Float> pos1Health, ArrayList<Float> pos2Health, String arena,
					String map, boolean ranked, UUID tournament,
					String tName,  String itemName, int subID, String kit,
					int duration, 
					ArrayList<Integer> kills1, ArrayList<Integer> kills2) {
		this.time = time;
		this.id = id;
		this.pos1List = pos1List;
		this.pos2List = pos2List;
		this.pos1Names = pos1Names;
		this.pos2Names = pos2Names;
		this.pos1Wins = pos1Wins;
		this.colorPos1 = colorPos1;
		this.colorPos2 = colorPos2;
		this.pos1Health = pos1Health;
		this.pos2Health = pos2Health;
		this.arena = arena;
		this.map = map;
		this.ranked = ranked;
		this.tournament = tournament;
		this.tName = tName;
		this.itemName = itemName;
		this.subID = subID;
		this.kit = kit;
		this.duration = duration;
		this.kills1 = kills1;
		this.kills2 = kills2;
	}

	public UUID getId() {
		return id;
	}

	public ArrayList<UUID> getPos1List() {
		return pos1List;
	}

	public ArrayList<UUID> getPos2List() {
		return pos2List;
	}

	public ArrayList<String> getPos1Names() {
		return pos1Names;
	}

	public ArrayList<String> getPos2Names() {
		return pos2Names;
	}

	public boolean isPos1Wins() {
		return pos1Wins;
	}

	public String getColorPos1() {
		return colorPos1;
	}

	public String getColorPos2() {
		return colorPos2;
	}

	public ArrayList<Float> getPos1Health() {
		return pos1Health;
	}

	public ArrayList<Float> getPos2Health() {
		return pos2Health;
	}

	public String getArena() {
		return arena;
	}

	public String getMap() {
		return map;
	}

	public boolean isRanked() {
		return ranked;
	}

	public UUID getTournament() {
		return tournament;
	}

	public String gettName() {
		return tName;
	}

	public String getItemName() {
		return itemName;
	}

	public int getSubID() {
		return subID;
	}

	public String getKit() {
		return kit;
	}

	public int getDuration() {
		return duration;
	}

	public ArrayList<Integer> getKills1() {
		return kills1;
	}

	public ArrayList<Integer> getKills2() {
		return kills2;
	}
	
	public long getTime() {
		return this.time;
	}
	
	
	
}
