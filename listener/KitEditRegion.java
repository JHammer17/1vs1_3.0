package de.onevsone.listener;

import java.util.HashMap;
import java.util.UUID;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.AsyncCatcher;

import de.onevsone.Main;
import de.onevsone.enums.PlayerState;
import de.onevsone.methods.KitMgr;
import de.onevsone.methods.SoundMgr.JSound;
import de.onevsone.objects.OneVsOnePlayer;

public class KitEditRegion {

	public KitEditRegion() {
		startChecker();
	}

	public static void startChecker() {

		final HashMap<UUID, Location> playerLocs = new HashMap<>();
		new BukkitRunnable() {

			@Override
			public void run() {

				for (final OneVsOnePlayer p : Main.ins.getOneVsOnePlayersCopy().values()) {
					if (p == null || playerLocs == null)
						continue;
					


					
					
					
					if (playerLocs.containsKey(p.getPlayer().getUniqueId())
							&& playerLocs.get(p.getPlayer().getUniqueId()).getWorld().getName()
									.equals(p.getPlayer().getWorld().getName()))

						if (playerLocs.get(p.getPlayer().getUniqueId()).distance(p.getPlayer().getLocation()) > 0) {
							onMove(p.getPlayer());
						}
				}

				playerLocs.clear();

				for (final OneVsOnePlayer p : Main.ins.getOneVsOnePlayersCopy().values()) {
					playerLocs.put(p.getPlayer().getUniqueId(), p.getPlayer().getLocation());
				}
				
				
				
				

			}
		}.runTaskTimerAsynchronously(Main.ins, 0, 1);
	}

	public static void onMove(final Player p) {
		if (Main.ins.getOneVsOnePlayer(p).isIn1vs1()) {
			if (Main.ins.utils.checkRegion(p.getLocation(), Main.ins.utils.getMinKitEdit(),
					Main.ins.utils.getMaxKitEdit())) {
				if (Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INLOBBY) {
					Main.ins.getOneVsOnePlayer(p).setpState(PlayerState.INKITEDIT);
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					new BukkitRunnable() {

						@Override
						public void run() {

							p.setGameMode(GameMode.CREATIVE);
							p.setAllowFlight(false);
							p.setFlying(false);

						}
					}.runTask(Main.ins);

					new BukkitRunnable() {

						@Override
						public void run() {
							KitMgr.loadKit(p, p.getUniqueId(), false, Main.ins.database.getSelectedKit(p.getUniqueId()));
							KitMgr.loadKit(p, p.getUniqueId(), true, Main.ins.database.getSelectedKit(p.getUniqueId()));
						}
					}.runTaskAsynchronously(Main.ins);

				}

			} else {
				if (Main.ins.getOneVsOnePlayer(p).getpState() == PlayerState.INKITEDIT) {
					ItemStack[] contents = p.getInventory().getContents().clone();
					ItemStack[] armorContent = p.getInventory().getArmorContents().clone();
					new BukkitRunnable() {

						@Override
						public void run() {
							KitMgr.saveKit(p.getUniqueId(), contents.clone(), false, Main.ins.database.getSelectedKit(p.getUniqueId()));
							KitMgr.saveKit(p.getUniqueId(), armorContent.clone(), true, Main.ins.database.getSelectedKit(p.getUniqueId()));
						}
					}.runTaskAsynchronously(Main.ins);

					p.getInventory().setArmorContents(null);
					Main.ins.utils.giveLobbyItems(p);
					Main.ins.getOneVsOnePlayer(p).setpState(PlayerState.INLOBBY);
					p.sendMessage(Main.ins.prefixBlue + "Dein Kit wurde gespeichert!");
					Main.ins.utils.getSoundMgr().playSound(p, JSound.ANVIL_USE, 3f, 1f);
					
					for(PotionEffect effect : p.getActivePotionEffects()) p.removePotionEffect(effect.getType());
					
					new BukkitRunnable() {

						@Override
						public void run() {
							AsyncCatcher.enabled = false;
							p.setGameMode(GameMode.ADVENTURE);
							AsyncCatcher.enabled = true;

						}
					}.runTask(Main.ins);

				}

			}
		}
	}
	

}
