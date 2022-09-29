package de.onevsone;

import de.onevsone.arenas.ArenaEvents;
import de.onevsone.arenas.KillMgr;
import de.onevsone.arenas.SpectateMgr;
import de.onevsone.listener.ChallangeMgr;
import de.onevsone.listener.JoinLeaveEvent;
import de.onevsone.listener.KitEditorEvents;
import de.onevsone.listener.KitStandListener;
import de.onevsone.listener.LobbyEvents;
import de.onevsone.listener.LobbySettingsItemMgr;
import de.onevsone.listener.TeamMgr;
import de.onevsone.listener.WandListener;
import de.onevsone.listener.Inventories.BlackDealerInvManager;
import de.onevsone.listener.Inventories.MainInv;
import de.onevsone.listener.Inventories.SpectatorInv;
import de.onevsone.listener.Inventories.TournamentInfoInv;
import de.onevsone.listener.Inventories.TournamentInv;
import de.onevsone.listener.Inventories.subInvs.ColorSettingsInv;
import de.onevsone.listener.Inventories.subInvs.KitMainInv;
import de.onevsone.listener.Inventories.subInvs.KitSubInv;
import de.onevsone.listener.Inventories.subInvs.MapsInv;
import de.onevsone.listener.Inventories.subInvs.QueueInv;
import de.onevsone.listener.statsmgr.SignMgr;
import de.onevsone.listener.statsmgr.SkullMgr;
import de.onevsone.methods.StatsMenu;
import de.onevsone.methods.entities.BlackDealer;
import de.onevsone.methods.entities.QueueZombie;

public class Register {

	public Register() {
		Main.ins.getServer().getPluginManager().registerEvents(new LobbyEvents(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new WandListener(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new KitEditorEvents(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new ChallangeMgr(), Main.ins);
		
		Main.ins.getServer().getPluginManager().registerEvents(new SignMgr(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new SkullMgr(), Main.ins);
		
		Main.ins.getServer().getPluginManager().registerEvents(new LobbySettingsItemMgr(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new MainInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new KitSubInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new KitMainInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new QueueInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new MapsInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new SpectateMgr(), Main.ins);
		
		Main.ins.getServer().getPluginManager().registerEvents(new ColorSettingsInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new SpectatorInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new ArenaEvents(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new KillMgr(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new TeamMgr(), Main.ins);
		
		Main.ins.getServer().getPluginManager().registerEvents(new KitStandListener(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new TournamentInv(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new TournamentInfoInv(), Main.ins);
		
		Main.ins.getServer().getPluginManager().registerEvents(new QueueZombie(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new BlackDealer(), Main.ins);
		Main.ins.getServer().getPluginManager().registerEvents(new BlackDealerInvManager(), Main.ins);
		
		Main.ins.getServer().getPluginManager().registerEvents(new StatsMenu(), Main.ins);
	
	}
	
	
}
