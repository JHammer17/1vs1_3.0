package de.onevsone.arenas;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.onevsone.Main;
import de.onevsone.enums.KitPrefs;
import de.onevsone.enums.PlayerState;
import de.onevsone.methods.SoundMgr.JSound;
import de.onevsone.objects.OneVsOneArena;
import de.onevsone.objects.OneVsOnePlayer;

public class ArenaEvents implements Listener {

	public ArenaEvents() {
		startChecker();
	}
	
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().getType() == EntityType.PLAYER) {
			if(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.getSettings().contains(KitPrefs.NOFRIENDLYFIRE)) {
					if(arenaObj.getPos1().contains(e.getEntity().getUniqueId()) &&
					   arenaObj.getPos1().contains(e.getDamager().getUniqueId())) {
						e.setDamage(0);
						e.setCancelled(true);
					}
					if(arenaObj.getPos2().contains(e.getEntity().getUniqueId()) &&
					   arenaObj.getPos2().contains(e.getDamager().getUniqueId())) {
						e.setDamage(0);
						e.setCancelled(true);
					}
				}
				
				
			}
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		if(e.getEntity().getType() == EntityType.PLAYER) {
			if(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				
				
				
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
					e.setDamage(0);
				}
				
				if(arenaObj.getSettings().contains(KitPrefs.NOFALLDMG)) {
					if(e.getCause() == DamageCause.FALL) {
						e.setCancelled(true);
						e.setDamage(0);
					}
				}
				
				if(arenaObj.getSettings().contains(KitPrefs.DOUBLEJUMP)) {
					if(e.getCause() == DamageCause.FALL && Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).isDoubleJumpUsed()) {
						
						e.setCancelled(true);
						e.setDamage(0);
					}
				}
				
				if(arenaObj.getSettings().contains(KitPrefs.NOHITDELAY)) {
					Player p = (Player) e.getEntity();
					p.setNoDamageTicks(0);
					p.setMaximumNoDamageTicks(0);
				}
				
			}
		}
	}
	
	@EventHandler
	public void onBuild(BlockPlaceEvent e) {
		
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
				}
				if(!Main.ins.utils.checkRegion(e.getBlock().getLocation(), arenaObj.getBuildCorner1(), arenaObj.getBuildCorner2())) {
					e.setCancelled(true);
				}
				
				if(arenaObj.getSettings().contains(KitPrefs.INSTANTTNT)) {
					if(e.getBlockPlaced().getType() == Material.TNT) {
						e.getBlockPlaced().setType(Material.AIR);
						TNTPrimed tnt = e.getBlockPlaced().getLocation().getWorld().spawn(e.getBlockPlaced().getLocation(), TNTPrimed.class);
						tnt.setFuseTicks(20*3);
					}
				}
				
				if(arenaObj.getSettings().contains(KitPrefs.NOBUILD)) {
					e.setCancelled(true);
				}
				
			}
		
	}
	
	@EventHandler
	public void onBuild(BlockBreakEvent e) {
		
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				
				if(!Main.ins.utils.checkRegion(e.getBlock().getLocation(), arenaObj.getBuildCorner1(), arenaObj.getBuildCorner2())) {
					e.setCancelled(true);
				}
				
				if(arenaObj.getSettings().contains(KitPrefs.NOBUILD)) {
					e.setCancelled(true);
				}
			}
		
	}
	
	@EventHandler
	public void onBuild(PlayerBucketEmptyEvent e) {
		
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					
					e.setCancelled(true);
				}
				if(!Main.ins.utils.checkRegion(e.getBlockClicked().getLocation(), arenaObj.getCorner1(), arenaObj.getCorner2())) {
					
					e.setCancelled(true);
				}
			}
		
	}
	
	@EventHandler
	public void onBuild(PlayerBucketFillEvent e) {
		
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					
					e.setCancelled(true);
				}
				
				if(!Main.ins.utils.checkRegion(e.getBlockClicked().getLocation(), arenaObj.getCorner1(), arenaObj.getCorner2())) {
					e.setCancelled(true);
				}
				
			}
		
	}
	
	@EventHandler
	public void onBuild(PlayerInteractEvent e) {
		
			if(Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getPlayer().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
				}
				
				if(arenaObj.isStarted() && !arenaObj.isEnded()) {
					if(arenaObj.getSettings().contains(KitPrefs.SOUPHEAL)) {
						if(e.getItem() != null) {
							if(e.getItem().getType() == Material.MUSHROOM_SOUP) {
							 if(e.getPlayer().getHealth()+7 < e.getPlayer().getMaxHealth()) {
								 e.getItem().setType(Material.BOWL);
									e.getPlayer().setHealth(e.getPlayer().getHealth()+7);
							 } else {
								 e.getPlayer().sendMessage(Main.ins.prefixRed + "§cDu musst momentan nicht geheilt werden!");;
							 }
							}
						}
					}
				}
				
			}
		
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		
		
		
		ArrayList<Block> blocks = new ArrayList<>();
		
		for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
			if(Main.ins.utils.checkRegion(e.getLocation(), arenas.getCorner1(), arenas.getCorner2())) {
				if(!arenas.isStarted() || arenas.isEnded()) {
					e.setCancelled(true);
				}
				
				if(arenas.getSettings().contains(KitPrefs.NOTNTDMG)) {
					e.setCancelled(true);
					break;
				}
				
			}
		}
		for(Block loc : e.blockList()) {
			for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
				if(Main.ins.utils.checkRegion(loc.getLocation(), arenas.getBuildCorner1(), arenas.getBuildCorner2())) {
					blocks.add(loc);
				} 
			}
			
		}
		
		
		ArrayList<Block> blocksInverted = new ArrayList<>();
		blocksInverted.addAll(e.blockList());
		blocksInverted.removeAll(blocks);
		e.blockList().removeAll(blocksInverted);
		
		
	}
	
	@EventHandler
	public void onChangeBlock(EntityChangeBlockEvent e) {
		
		
		for(OneVsOneArena arenas : Main.ins.getOneVsOneArenasCopy().values()) {
			if(Main.ins.utils.checkRegion(e.getBlock().getLocation(), arenas.getCorner1(), arenas.getCorner2())) {
				if(!arenas.isStarted() || arenas.isEnded()) {
					e.setCancelled(true);
				}
			}
			if(!Main.ins.utils.checkRegion(e.getBlock().getLocation(), arenas.getBuildCorner1(), arenas.getBuildCorner2())) {
				
				e.setCancelled(true);
			}
		}
		
		
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if(e.getEntity().getType() == EntityType.PLAYER) {
			if(Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getEntity().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
					e.setFoodLevel(20);
				}
				if(!arenaObj.getSettings().contains(KitPrefs.HUNGER)) {
					e.setCancelled(true);
					e.setFoodLevel(20);
				}
				
			}
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(e.getWhoClicked().getType() == EntityType.PLAYER) {
			if(Main.ins.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() == PlayerState.INARENA) {
			
				String arena = Main.ins.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
				}
				if(arenaObj.getSettings().contains(KitPrefs.NOCRAFTING)) {
					e.setCancelled(true);
				}
				
			}
		}
	}
	
	@EventHandler
	public void onArrow(ProjectileHitEvent e) {
		if(e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			if(Main.ins.getOneVsOnePlayer(p.getUniqueId()).getpState() == PlayerState.INARENA) {
				
				String arena = Main.ins.getOneVsOnePlayer(p.getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.getEntity().remove();
				}
				if(arenaObj.getSettings().contains(KitPrefs.NOARROWPICKUP)) {
					e.getEntity().remove();
				}
				
			}
			
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		
			Player p = e.getPlayer();
			if(Main.ins.getOneVsOnePlayer(p.getUniqueId()).getpState() == PlayerState.INARENA) {
				
				String arena = Main.ins.getOneVsOnePlayer(p.getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
				}
				if(arenaObj.getSettings().contains(KitPrefs.SOUPNOOB)) {
					if(e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD || 
					   e.getItemDrop().getItemStack().getType() == Material.GOLD_SWORD || 
					   e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD || 
					   e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD || 
					   e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD || 
					   e.getItemDrop().getItemStack().getType() == Material.MUSHROOM_SOUP)
					e.setCancelled(true);
				}
				
			}
			
		
	}
	
	@EventHandler
	public void onReg(EntityRegainHealthEvent e) {
		if(e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if(Main.ins.getOneVsOnePlayer(p.getUniqueId()).getpState() == PlayerState.INARENA) {
				
				String arena = Main.ins.getOneVsOnePlayer(p.getUniqueId()).getArena();
				
				OneVsOneArena arenaObj = Main.ins.getOneVsOneArena(arena);
				
				if(!arenaObj.isStarted() || arenaObj.isEnded()) {
					e.setCancelled(true);
				}
				if(arenaObj.getSettings().contains(KitPrefs.NOREGENERATION)) {
					
					if(e.getRegainReason() == RegainReason.REGEN) {
						e.setCancelled(true);
					}
				}
				
			}
		}
		
	}
	
	
	public static void startChecker() {

		
		new BukkitRunnable() {

			@Override
			public void run() {

				for (final OneVsOnePlayer p : Main.ins.getOneVsOnePlayersCopy().values()) {
					if (p == null)
						continue;
						onMove(p.getPlayer());	
				}
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 1);
		
		new BukkitRunnable() {

			@Override
			public void run() {

				for (final OneVsOnePlayer p : Main.ins.getOneVsOnePlayersCopy().values()) {
					if (p == null)
						continue;
					onMoveWater(p.getPlayer());
						
				}
				
			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 10);
	}
	
	public static void onMoveWater(Player p) {
		if (Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
			 if (Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INARENA) {
				OneVsOneArena arena = Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(p).getArena());
				
				if(arena != null && arena.isOk() && !arena.isEnded() && arena.isStarted()) {
					if((p.getLocation().getBlock().getType() == Material.WATER || 
						p.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
							&& arena.getSettings().contains(KitPrefs.WATERDMG)) {
						
						p.damage(1.5);
						
					}
					
					
					
				}
				
				
			 }
			}
	
	}

	@SuppressWarnings("deprecation")
	public static void onMove(final Player p) {
		if (Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
		 if (Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INARENA) {
			OneVsOneArena arena = Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(p).getArena());
			
			if(arena != null && arena.isOk() && !arena.isEnded() && arena.isStarted()) {
				
				if(p.isOnGround() && arena.getSettings().contains(KitPrefs.DOUBLEJUMP)) {
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							p.setAllowFlight(true);
							p.setFlying(false);
							Main.ins.getOneVsOnePlayer(p).setDoubleJumpUsed(false);
						}
					}.runTask(Main.ins);
					
				}
				
				if((p.getLocation().getBlock().getType() == Material.WATER || 
					p.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
						&& arena.getSettings().contains(KitPrefs.WATERDMG)) {
					
					p.damage(1.5);
					
				}
				
				
				
			}
			
			
		 }
		}
	}
	
	@EventHandler
	public void onDoubleJump(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if (Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
			 if (Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INARENA) {
				OneVsOneArena arena = Main.ins.getOneVsOneArena(Main.ins.getOneVsOnePlayer(p).getArena());
				
				if(arena != null && arena.isOk() && !arena.isEnded() && arena.isStarted()) {
					
					if(!Main.ins.getOneVsOnePlayer(p).isDoubleJumpUsed() && arena.getSettings().contains(KitPrefs.DOUBLEJUMP)) {
						e.setCancelled(true);
						p.setFlying(false);
						p.setAllowFlight(false);
						
						Main.ins.getOneVsOnePlayer(p).setDoubleJumpUsed(true);
						p.setVelocity(p.getLocation().getDirection().multiply(1.3D).setY(0.9D));
						Main.ins.utils.getSoundMgr().playSound(p, JSound.FIREWORK_LAUNCH, 10, 1);
					
					}
					
					
					
					
				}
				
				
			 }
			}
	}
	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e) {
		
	}
	
}
