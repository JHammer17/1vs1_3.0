package de.onevsone.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;

/**
 * Der Code ist von JHammer
 *
 * 24.11.2017 um 20:40:20 Uhr
 *
 */
public class OneVsOnePlayer {

    boolean in1vs1 = true;
    boolean editMode = false;
    boolean inQueue = false;
    boolean wasInQueue = false;
    boolean doubleJumpUsed = false;

    Location pos1;
    Location pos2;
    Location pos3;

    String arena;

    Player enemy;

    int position;

    PlayerState pState = PlayerState.UNKNOWN;

    ArrayList<UUID> challangedBy = new ArrayList<UUID>();
    ArrayList<UUID> challanged = new ArrayList<UUID>();

    String arenaView;
    String specator;

    int preferencesInv;

    ItemStack[] playerInv;
    ItemStack[] playerArmor;

    int playerLvl;
    float playerXP;

    Scoreboard oldBoard;

    UUID playertournament;

    boolean hasKit = false;
    
    UUID playerUUID;
    

//	ArenaTeamPlayer team;

    ArrayList<UUID> teamInvitedBy = new ArrayList<UUID>();
    ArrayList<UUID> teamInvited = new ArrayList<UUID>();

	OneVsOneTeam playerTeam;

    String kitLoaded;

    HashMap<String, Object> dataBaseData = new HashMap<String, Object>();
    HashMap<String, Object> dataBaseDataName = new HashMap<String, Object>();

    Player player;
    
    boolean input = false;
    String inputReason = "";
	private PotionEffectType blackDealerEffect;
	
	private boolean acceptChallanges = true;
	private long acceptChallangesTimer = 0;
	
	private long inventoryCooldown = 0;
	
	private ArrayList<Integer> hologramIds = new ArrayList<>();
	
	
	private HashMap<String, String> prefetchedData = new HashMap<>();
	private int holoStatsType = 1;
	
	private int skullModeTimed = -1;
	private int skullModePlace = -1;
	
	private int statsMenuOffset = 0;
	private UUID statsMenuOwner;
	private boolean statsMenuShowMaps = true;
	private boolean useFightsMenu = false;
	
    /**
	 * @return the inputKitName
	 */
	public String getInputReason() {
		return inputReason;
	}

	/**
	 * @return the hasKit
	 */
	public boolean isHasKit() {
		return hasKit;
	}

	/**
	 * @param hasKit the hasKit to set
	 */
	public void setHasKit(boolean hasKit) {
		this.hasKit = hasKit;
	}

	/**
	 * @param inputKitName the inputKitName to set
	 */
	public void setInputReason(String inputReasonName) {
		this.inputReason = inputReasonName;
	}

	public OneVsOnePlayer(Player p) {
		if(p != null) {
			 this.player = p;
		        this.playerUUID = p.getUniqueId();
		}
       
    }

    public void init() {
        in1vs1 = true;
        pState = PlayerState.INLOBBY;
        kitLoaded = this.player.getUniqueId() + ":" + Main.ins.database.getSelectedKit(this.player.getUniqueId());
        Main.ins.database.prefetchAllData(getUUID());
    }


    public boolean isIn1vs1() {
        return in1vs1;
    }

    public void setIn1vs1(boolean in1vs1) {
        this.in1vs1 = in1vs1;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    public boolean isWasInQueue() {
        return wasInQueue;
    }

    public void setWasInQueue(boolean wasInQueue) {
        this.wasInQueue = wasInQueue;
    }

    public boolean isDoubleJumpUsed() {
        return doubleJumpUsed;
    }

    public void setDoubleJumpUsed(boolean doubleJumpUsed) {
        this.doubleJumpUsed = doubleJumpUsed;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getPos3() {
        return pos3;
    }

    public void setPos3(Location pos3) {
        this.pos3 = pos3;
    }

    public String getArena() {
        return arena;
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PlayerState getpState() {

        if(!isIn1vs1()) return PlayerState.UNKNOWN;

        return pState;
    }

    public void setpState(PlayerState pState) {
    	this.input = false;
        this.pState = pState;
    }

    /**
	 * @return the input
	 */
	public boolean isInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(boolean input) {
		this.input = input;
	}

	public ArrayList<UUID> getChallangedBy() {
        return this.challangedBy;
    }

    public void setChallangedBy(ArrayList<UUID> challangedBy) {
        this.challangedBy = challangedBy;
    }

    public ArrayList<UUID> getChallanged() {
        return challanged;
    }

    public void setChallanged(ArrayList<UUID> challanged) {
        this.challanged = challanged;
    }

    public String getArenaView() {
        return arenaView;
    }

    public void setArenaView(String arenaView) {
        this.arenaView = arenaView;
    }

    public String getSpecator() {
        return specator;
    }

    public void setSpecator(String specator) {
        this.specator = specator;
    }

    public int getPreferencesInv() {
        return preferencesInv;
    }

    public void setPreferencesInv(int preferencesInv) {
        this.preferencesInv = preferencesInv;
    }

    public ItemStack[] getPlayerInv() {
        return playerInv;
    }

    public void setPlayerInv(ItemStack[] playerInv) {
        this.playerInv = playerInv;
    }

    public ItemStack[] getPlayerArmor() {
        return playerArmor;
    }

    public void setPlayerArmor(ItemStack[] playerArmor) {
        this.playerArmor = playerArmor;
    }

    public int getPlayerLvl() {
        return playerLvl;
    }

    public void setPlayerLvl(int playerLvl) {
        this.playerLvl = playerLvl;
    }

    public float getPlayerXP() {
        return playerXP;
    }

    public void setPlayerXP(float playerXP) {
        this.playerXP = playerXP;
    }

    public UUID getPlayertournament() {
        return playertournament;
    }

    public void setPlayertournament(UUID playertournament) {
        this.playertournament = playertournament;
    }

    public Scoreboard getOldBoard() {
        return oldBoard;
    }

    public void setOldBoard(Scoreboard oldBoard) {
        this.oldBoard = oldBoard;
    }

//	public ArenaTeamPlayer getTeam() {
//		return team;
//	}
//
//	public void setTeam(ArenaTeamPlayer team) {
//		this.team = team;
//	}

    public ArrayList<UUID> getTeamInvitedBy() {
        return teamInvitedBy;
    }

    public void setTeamInvitedBy(ArrayList<UUID> teamInvitedBy) {
        this.teamInvitedBy = teamInvitedBy;
    }

    public ArrayList<UUID> getTeamInvited() {
        return teamInvited;
    }

    public void setTeamInvited(ArrayList<UUID> teamInvited) {
        this.teamInvited = teamInvited;
    }

//	public TeamPlayer getPlayerTeam() {
//		return playerTeam;
//	}
//
//	public void setPlayerTeam(TeamPlayer playerTeam) {
//		this.playerTeam = playerTeam;
//	}

    public String getKitLoaded() {
        return kitLoaded;
    }

    public void setKitLoaded(String kitLoaded) {
        this.kitLoaded = kitLoaded;
    }

    public HashMap<String, Object> getDataBaseData() {
        return dataBaseData;
    }

    public void setDataBaseData(HashMap<String, Object> dataBaseData) {
        this.dataBaseData = dataBaseData;
    }

    public HashMap<String, Object> getDataBaseDataName() {
        return dataBaseDataName;
    }

    public void setDataBaseDataName(HashMap<String, Object> dataBaseDataName) {
        this.dataBaseDataName = dataBaseDataName;
    }

    public Player getPlayer() {
        return player;
    }

    public void resetChallanged() {
    	this.challanged.clear();
    }
    
    public void resetChallangedBy() {
    	this.challangedBy.clear();
    }

    public void resetChallanges() {
    	resetChallanged();
    	resetChallangedBy();
    }
    
    public void resetTeamInvited() {
    	this.teamInvited.clear();
    }
    
    public void resetTeamInvitedBy() {
    	this.teamInvitedBy.clear();
    }
    
    
    public void resetTeamInvites() {
    	resetTeamInvited();
    	resetTeamInvitedBy();
    }
    
    public void setTeamObj(OneVsOneTeam team) {
    	this.playerTeam = team;
    }
    
    public OneVsOneTeam getTeamObj() {
    	return this.playerTeam;
    }
    
    public UUID getUUID() {
    	return playerUUID;
    }
    
    public void setBlackDealerEffect(PotionEffectType effect) {
    	this.blackDealerEffect = effect;
    }
    
    public PotionEffectType getBlackDealerEffect() {
    	return this.blackDealerEffect;
    }
    
    public boolean getAcceptChallanges() {
    	return this.acceptChallanges;
    }
    
    public void setAcceptChallanges(boolean accept) {
    	this.acceptChallanges = accept;
    }
    
    public long getAcceptChallangesTimer() {
    	return this.acceptChallangesTimer;
    }
    
    public void setAcceptChallangesTimer(long timer) {
    	this.acceptChallangesTimer = timer;
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<Integer> getHologramIds() {
    	return (ArrayList<Integer>) this.hologramIds.clone();
    }
    
   
    public void addHologramId(int id) {
    	if(!this.hologramIds.contains(id)) this.hologramIds.add(id);
    	
    }
    
    public void removeFromIds(int id) {
    	if(this.hologramIds.contains(id)) this.hologramIds.remove(id);
    }
    
    public String getPrefetchData(String key) {
    	if(this.prefetchedData.containsKey(key)) {
//    		if(!key.equalsIgnoreCase("SkinData")) Bukkit.broadcastMessage("§aVorhanden " + key);
    		return this.prefetchedData.get(key);
    	}
//    	if(!key.equalsIgnoreCase("SkinData")) Bukkit.broadcastMessage("§cNicht vorhanden " + key);
    	return null;
    }
    
    public void addPrefetchData(String key, String value) {
    	
    	
    	if(!this.prefetchedData.containsKey(key)) this.prefetchedData.put(key, value);
    	else {
    		this.prefetchedData.remove(key);
    		addPrefetchData(key, value);
    		return;
    	}
    	
//    	if(!key.equalsIgnoreCase("SkinData")) Bukkit.broadcastMessage("§eGeschrieben: " + key + " - " + value);
    } 
    
    public void removePrefetchData(String key) {
    	if(this.prefetchedData.containsKey(key)) this.prefetchedData.remove(key);
    }
    
    @SuppressWarnings("unchecked")
	public HashMap<String, String> getPrefetchDataList() {
    	return (HashMap<String, String>) this.prefetchedData.clone();
    }
    
    public long getInventoryCoolDown() {
    	return this.inventoryCooldown;
    }
    
    public void setInventoryCoolDown(long timer) {
    	this.inventoryCooldown = timer;
    }
    
    public boolean allowInventoryClick() {
    	return System.currentTimeMillis()-getInventoryCoolDown() > 250;
			
    }
    
    public void setHoloStatsType(int i) {
    	this.holoStatsType = i;
    }
   
    public int getHoloStatsType() {
    	if(this.holoStatsType > 3) return 1;
    	if(holoStatsType < 1) return 3;
    	return this.holoStatsType;
    }
    
    public void setSkullModeTimed(int timed) {
    	this.skullModeTimed = timed;
    }
    
    public int getSkullModeTimed() {
    	return this.skullModeTimed;
    }
    
    public void setSkullModePlace(int place) {
    	this.skullModePlace = place;
    }
    
    public int getSkullModePlace() {
    	return this.skullModePlace;
    }
    
    public int getStatsMenuOffset() {
    	return this.statsMenuOffset;
    }
    
    public void setStatsMenuOffset(int offset) {
    	this.statsMenuOffset = offset;
    }
    
    public UUID getStatsMenuOwner() {
    	return this.statsMenuOwner;
    }
    
    public void setStatsMenuOwner(UUID uuid) {
    	this.statsMenuOwner = uuid;
    }
    
    public boolean isStatsMenuShowMaps() {
    	return this.statsMenuShowMaps;
    }
    
    public void setStatsMenuShowMaps(boolean showMaps) {
    	this.statsMenuShowMaps = showMaps;
    }
    
    public boolean isFightMenu() {
    	return this.useFightsMenu;
    }
    
    public void setFightsMenu(boolean fightsMenu) {
    	this.useFightsMenu = fightsMenu;
    }
    
    public void resetStatMenuSettings() {
    	this.statsMenuOffset = 0;
    	this.statsMenuOwner = null;
    	this.statsMenuShowMaps = true;
    	this.useFightsMenu = false;
    }
    
}
