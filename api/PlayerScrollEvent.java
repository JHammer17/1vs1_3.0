package de.onevsone.api;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;

public class PlayerScrollEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	
	static {
		HashMap<UUID, Integer> selected = new HashMap<>();
		new BukkitRunnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
						continue;
					}
					if(selected.containsKey(p.getUniqueId())) {
						if(selected.get(p.getUniqueId()) != p.getInventory().getHeldItemSlot()) {
							Main.ins.getServer().getPluginManager().callEvent(new PlayerScrollEvent(p, selected.get(p.getUniqueId()), p.getInventory().getHeldItemSlot()));
							selected.remove(p.getUniqueId());
							selected.put(p.getUniqueId(), p.getInventory().getHeldItemSlot());
						}
					} else {
						selected.put(p.getUniqueId(), p.getInventory().getHeldItemSlot());
						
					}
					
				}
				
				for(UUID uuid : ((HashMap<UUID, Integer>) selected.clone()).keySet()) {
					if(Bukkit.getPlayer(uuid) == null) selected.remove(uuid);
				}
				
				
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 2);
		
	}

	

	private Player player;
	private int before;
	private int now;
	
	public PlayerScrollEvent(Player player, int before, int now) {
		this.player = player;
		this.before = before;
		this.now = now;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getBefore() {
		return before;
	}
	
	public int getNow() {
		return now;
	}
	
	
	public boolean nextSelected() {
		if(before == 8 && now == 0) return true;
		if(before == 0 && now == 8) return false;
		return before < now;
	}
	
	
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}


	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return HANDLERS_LIST;
	}
	
	
	
	

}
