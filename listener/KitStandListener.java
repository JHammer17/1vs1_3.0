/**
 * 
 */
package de.onevsone.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import de.onevsone.Main;
import de.onevsone.objects.KitStand;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 30.03.2018 20:35:53					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class KitStandListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
			Player p = e.getPlayer();
			
			if(Main.ins.kitStandUUIDs.contains(e.getRightClicked().getUniqueId())) {
			 e.setCancelled(true);
			 
			 if(Main.ins.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			  KitStand ks = Main.ins.kitStands.get(e.getRightClicked().getUniqueId());
			  
			  if(ks.getSubID() != 1) {
				p.chat("/kit " + ks.getKit() + ":" + ks.getSubID());
			  } else {
				p.chat("/kit " + ks.getKit());
			  }
			 }
			 
			 return;
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e) {
		if(e.getEntityType().equals(EntityType.ARMOR_STAND)) 
		 if(Main.ins.kitStandUUIDs.contains(e.getEntity().getUniqueId())) e.setCancelled(true);
			
	}
	
	
	
	
	
}
