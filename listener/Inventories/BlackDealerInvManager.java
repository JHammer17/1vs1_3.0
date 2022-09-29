package de.onevsone.listener.Inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import de.onevsone.Main;
import de.onevsone.methods.BlackDealerInvCreator;



public class BlackDealerInvManager implements Listener {

	
	
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
		 Player p = (Player) e.getWhoClicked();
		 if(Main.ins.isInOneVsOnePlayers(p.getUniqueId())) {
			
		  if(e.getInventory().getTitle().equalsIgnoreCase("Schwarzhändler")) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) return;
			
			
			
		   if(e.getClickedInventory().getTitle().equalsIgnoreCase("Schwarzhändler")) {
			if(e.getCurrentItem() != null) {
				
				if(!Main.ins.getOneVsOnePlayer(p).allowInventoryClick()) {
					return;
				}
				Main.ins.getOneVsOnePlayer(p).setInventoryCoolDown(System.currentTimeMillis());
				
				
				
			 if(e.getSlot() == 4) {
			   if(e.isShiftClick()) {
				  p.getItemInHand().setAmount(p.getItemInHand().getAmount()+5);
				   if(p.getItemInHand().getAmount() > 64) {
					  p.getItemInHand().setAmount(64);
				   }
			      } else {
			    	  p.getItemInHand().setAmount(p.getItemInHand().getAmount()+1);
				  if(p.getItemInHand().getAmount() > 64) {
					  p.getItemInHand().setAmount(64);
				  }
				  
			     }
			   BlackDealerInvCreator.createInv(p);
			   } else if(e.getSlot() == 4+9+9) {
				   
				   if(e.isShiftClick()) {
					   if(p.getItemInHand().getAmount() == 5) {
						   p.getItemInHand().setAmount(1);
						   BlackDealerInvCreator.createInv(p);
						   return;
					   }
						  p.getItemInHand().setAmount(p.getItemInHand().getAmount()-5);
						   if(p.getItemInHand().getAmount() <= 1) {
							  p.getItemInHand().setAmount(1);
						   }
					      } else {
					    	  p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
						  if(p.getItemInHand().getAmount() < 1) {
							  p.getItemInHand().setAmount(1);
						  }
					     }  
				 BlackDealerInvCreator.createInv(p);
			   } else if (e.getSlot() == 15) {
				   if(e.getCurrentItem().getType() == Material.ANVIL) {
					   BlackDealerInvCreator.createInvDur(p);
				   } else if(e.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
					   BlackDealerInvCreator.openAppleInv(p);
				   } else if(e.getCurrentItem().getType() == Material.POTION) {
					   BlackDealerInvCreator.openPotionInv(p);
				   }
			   } else if (e.getSlot() == 16) {
				   if(e.getCurrentItem().getType() == Material.DIAMOND) {
					   ItemStack item = p.getItemInHand();
					   ItemMeta iMeta = item.getItemMeta();
					   iMeta.spigot().setUnbreakable(!iMeta.spigot().isUnbreakable());
					   p.getInventory().getItemInHand().setItemMeta(iMeta);
					   BlackDealerInvCreator.createInv(p);
				   } 
			   } else if(e.getSlot() == 14) {
				   BlackDealerInvCreator.createEnchantInv(p);
			   }
			}
		   }
		  } else if(e.getInventory().getTitle().equalsIgnoreCase("Haltbarkeit verändern")) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase("Haltbarkeit verändern")) {
				if(e.getCurrentItem() != null) {
					if(e.getSlot() == 6-1) {
						if(e.isShiftClick()) {
							int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
							if(curr == 0) curr += p.getItemInHand().getType().getMaxDurability()/100*17.3444;
							curr = (int) (curr*1.1);
							
							if(curr >= p.getItemInHand().getType().getMaxDurability()) {
								curr = p.getItemInHand().getType().getMaxDurability();
							}
							int setter = p.getItemInHand().getType().getMaxDurability()-curr;
							p.getItemInHand().setDurability((short) setter);
							BlackDealerInvCreator.createInvDur(p);
							return;
						} else {
							int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
							curr++;
							if(curr >= p.getItemInHand().getType().getMaxDurability()) {
								curr = p.getItemInHand().getType().getMaxDurability();
							}
							int setter = p.getItemInHand().getType().getMaxDurability()-curr;
							p.getItemInHand().setDurability((short) setter);
						}
						BlackDealerInvCreator.createInvDur(p);
					} else if(e.getSlot() == 6+9*2-1) {
						if(e.isShiftClick()) {
							if(e.isShiftClick()) {
								int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
								curr = curr/100*90;
								if(curr <= 0) curr = 0;
								int setter = p.getItemInHand().getType().getMaxDurability()-curr;
								p.getItemInHand().setDurability((short) setter);
							}
						} else {
							int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
							curr--;
							if(curr <= 0) curr = 0;
							int setter = p.getItemInHand().getType().getMaxDurability()-curr;
							p.getItemInHand().setDurability((short) setter);
						}
						BlackDealerInvCreator.createInvDur(p);
					}
				}
			   }
		  } else if(e.getInventory().getTitle().equalsIgnoreCase("Item Verzaubern")) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase("Item Verzaubern")) {
				 
				 if(e.getCurrentItem() != null) {
					int slot = e.getSlot();
					
					if(slot == 3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_EXPLOSIONS);
					if(slot == 4) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_PROJECTILE);
					if(slot == 5) BlackDealerInvCreator.addEnchantInv(p, Enchantment.OXYGEN);
					if(slot == 6) BlackDealerInvCreator.addEnchantInv(p, Enchantment.WATER_WORKER);
					if(slot == 7) BlackDealerInvCreator.addEnchantInv(p, Enchantment.THORNS);
					if(slot == 8) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DEPTH_STRIDER);
				
					if(slot == 3+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.KNOCKBACK);
					if(slot == 4+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.FIRE_ASPECT);
					if(slot == 5+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LOOT_BONUS_MOBS);
					if(slot == 6+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_DAMAGE);
					if(slot == 7+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_FIRE);
					if(slot == 8+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_KNOCKBACK);
					
					if(slot == 3+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DURABILITY);
					if(slot == 4+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LOOT_BONUS_BLOCKS);
					if(slot == 5+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LURE);
					if(slot == 6+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LUCK);
					if(slot == 7+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_INFINITE);
					if(slot == 8+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DAMAGE_ALL);
					
					if(slot == 3+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DAMAGE_ARTHROPODS);
					if(slot == 4+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_FIRE);
					if(slot == 5+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DAMAGE_UNDEAD);
					if(slot == 6+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.SILK_TOUCH);
					if(slot == 7+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_ENVIRONMENTAL);
					if(slot == 8+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DIG_SPEED);
					
					if(slot == 3+9*4) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_FALL);
				}
			   }
		   } else if(e.getInventory().getTitle().equalsIgnoreCase("Verzaubern")) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createEnchantInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase("Verzaubern")) {
				if(e.getCurrentItem() != null) {
					if(e.getSlot() == 6-1) {
						if(e.isShiftClick()) {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							Main.ins.utils.applyEnchant(p.getItemInHand(), ench, Main.ins.utils.getEnchLevel(p.getItemInHand(), ench)+5, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						} else {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							Main.ins.utils.applyEnchant(p.getItemInHand(), ench, Main.ins.utils.getEnchLevel(p.getItemInHand(), ench)+1, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						}
					} else if(e.getSlot() == 6+9+9-1) {
						if(e.isShiftClick()) {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							Main.ins.utils.applyEnchant(p.getItemInHand(), ench, Main.ins.utils.getEnchLevel(p.getItemInHand(), ench)-5, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						} else {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							Main.ins.utils.applyEnchant(p.getItemInHand(), ench, Main.ins.utils.getEnchLevel(p.getItemInHand(), ench)-1, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						}
						
					}
				}
			   }
		   } else if(e.getInventory().getTitle().equalsIgnoreCase("GOLDEN-HEAD")) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase("GOLDEN-HEAD")) {
				if(e.getCurrentItem() != null) {
				 if(e.getSlot() == 15 || e.getSlot() == 14) {
					 if(p.getItemInHand().getType() == Material.GOLDEN_APPLE && p.getItemInHand().hasItemMeta() &&
						p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
						 p.setItemInHand(Main.ins.utils.createItem(Material.GOLDEN_APPLE, 0, p.getItemInHand().getAmount(), null, null));
					 } else {
						 p.setItemInHand(Main.ins.utils.createItem(Material.GOLDEN_APPLE, 0, p.getItemInHand().getAmount(), "§6Golden Head", null));
					 }
					 BlackDealerInvCreator.openAppleInv(p);
					 
				 }
				} 
			   }
		   } else if(e.getInventory().getTitle().equalsIgnoreCase("Trank Modifizieren§a")) {
			   e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase("Trank Modifizieren§a")) {
				if(e.getCurrentItem() != null) {
				 
					int slot = e.getSlot();
					
					if(slot == 3) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.HEAL, e.getCurrentItem());
					if(slot == 4) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.REGENERATION, e.getCurrentItem());
					if(slot == 5) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.INCREASE_DAMAGE, e.getCurrentItem());
					if(slot == 6) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.FIRE_RESISTANCE, e.getCurrentItem());
					if(slot == 7) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.SPEED, e.getCurrentItem());
					if(slot == 8) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.JUMP, e.getCurrentItem());
					
					if(slot == 12) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.INVISIBILITY, e.getCurrentItem());
					if(slot == 13) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.NIGHT_VISION, e.getCurrentItem());
					if(slot == 14) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.WATER_BREATHING, e.getCurrentItem());
					if(slot == 15) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.HEALTH_BOOST, e.getCurrentItem());
					if(slot == 16) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.ABSORPTION, e.getCurrentItem());
					if(slot == 17) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.DAMAGE_RESISTANCE, e.getCurrentItem());
					
					if(slot == 21) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.SATURATION, e.getCurrentItem());
					if(slot == 22) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.FAST_DIGGING, e.getCurrentItem());
					if(slot == 23) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.HARM, e.getCurrentItem());
					if(slot == 24) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.POISON, e.getCurrentItem());
					if(slot == 25) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.SLOW, e.getCurrentItem());
					if(slot == 26) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.WEAKNESS, e.getCurrentItem());
					
					if(slot == 30) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.WITHER, e.getCurrentItem());
					if(slot == 31) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.BLINDNESS, e.getCurrentItem());
					if(slot == 32) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.CONFUSION, e.getCurrentItem());
					if(slot == 33) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.HUNGER, e.getCurrentItem());
					if(slot == 34) BlackDealerInvCreator.createPotionEditorInv(p, PotionEffectType.SLOW_DIGGING, e.getCurrentItem());
					
					
				} 
			   } 
			   
		   } else if(e.getInventory().getTitle().equalsIgnoreCase("Trank Modifizieren§b")) {
			   e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.openPotionInv(p);;
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase("Trank Modifizieren§b")) {
				if(e.getCurrentItem() != null) {
				 
					if(e.getSlot() == 5 || e.getSlot() == 23 || e.getSlot() == 6 || e.getSlot() == 24) {
						
					
						if(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect() == null) {
							p.closeInventory();
							p.sendMessage(Main.ins.prefixRed + "§cEs ist ein Fehler aufgetreten. Bitte öffne das Inventar erneut");
							return;
						}
					
						int beforeLevel = 0;
						int beforeTicks = 0;
						
						
						boolean hasAsBaseEffect = 
						Potion.fromItemStack(p.getItemInHand()).getType().getEffectType().equals(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect());
						
						if(hasAsBaseEffect) {
							beforeLevel = Potion.fromItemStack(p.getItemInHand()).getEffects().iterator().next().getAmplifier();
							beforeTicks = Potion.fromItemStack(p.getItemInHand()).getEffects().iterator().next().getDuration();
						}
					
						PotionMeta meta = (PotionMeta) p.getItemInHand().getItemMeta();
						
						
						boolean found = false;
						for(PotionEffect potionEffect : meta.getCustomEffects()) {
							if(found) break;
							if(potionEffect.getType().getName().equalsIgnoreCase(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect().getName())) {
								found = true;
								
								if(e.getSlot() == 5) {
									int level = potionEffect.getAmplifier();
									if(e.isShiftClick()) {
										level+=5;
										if(level >= 25) level = 24;
									} else {
										level++;
									}
										
									if(level < 25) {
										meta.removeCustomEffect(potionEffect.getType());
										
										meta.addCustomEffect(new PotionEffect(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), potionEffect.getDuration(), level), true);
										
										ItemStack handStack = p.getItemInHand();
										handStack.setItemMeta(meta);
										
										
										p.setItemInHand(handStack);
										p.updateInventory();
										
										BlackDealerInvCreator.createPotionEditorInv(p, Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), null);
										
									}
								} else if(e.getSlot() == 23) {
									int level = potionEffect.getAmplifier();
									if(e.isShiftClick()) {
										level-=5;
										if(level <= 0) level = -1;
									} else {
										level--;
									}
										
									
									meta.removeCustomEffect(potionEffect.getType());
										
									if(level >= 0) meta.addCustomEffect(new PotionEffect(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), potionEffect.getDuration(), level), true);
										
									ItemStack handStack = p.getItemInHand();
									handStack.setItemMeta(meta);
										
										
									p.setItemInHand(handStack);
									p.updateInventory();
										
									BlackDealerInvCreator.createPotionEditorInv(p, Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), null);
									
								} else if(e.getSlot() == 6) {
									int duration = potionEffect.getDuration();
									if(e.isShiftClick()) {
										duration+=20;//+1 Sekunde
										if(duration > 20*60*15) duration = 20*60*15;
									} else {
										duration += 200;//+10 Sekunden
									}
										
									if(duration <= 20*60*15) {
										meta.removeCustomEffect(potionEffect.getType());
										
										meta.addCustomEffect(new PotionEffect(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), duration, potionEffect.getAmplifier()), true);
										
										ItemStack handStack = p.getItemInHand();
										handStack.setItemMeta(meta);
										
										
										p.setItemInHand(handStack);
										p.updateInventory();
										
										BlackDealerInvCreator.createPotionEditorInv(p, Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), null);
										
									}
								} else if(e.getSlot() == 24) {
									int duration = potionEffect.getDuration();
									if(e.isShiftClick()) {
										duration-=20;//-1 Sekunde
										if(duration <= 20) duration = 20;
									} else {
										duration -= 200;//-10 Sekunden
									}
									
									if(duration >= 20) {
										meta.removeCustomEffect(potionEffect.getType());
										
										meta.addCustomEffect(new PotionEffect(Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), duration, potionEffect.getAmplifier()), true);
											
										ItemStack handStack = p.getItemInHand();
										handStack.setItemMeta(meta);
											
											
										p.setItemInHand(handStack);
										p.updateInventory();
											
										BlackDealerInvCreator.createPotionEditorInv(p, Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), null);
										
									}
									
									
								}
								
								
								
							}
						}
						if(!found && e.getSlot() != 23) {
							
							int level = beforeLevel;
							if(hasAsBaseEffect) {
								if(e.isShiftClick()) {
									level+=5;
									if(level >= 25) level = 24;
								} else {
									level++;
								}
							}
							
							if(hasAsBaseEffect) 
								meta.addCustomEffect(new PotionEffect(
										Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), 
										beforeTicks, level), true);
							else 
								meta.addCustomEffect(new PotionEffect(
										Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), 
										20*10, level), true);
								
							
							
							p.getItemInHand().setItemMeta(meta);
							p.updateInventory();
							BlackDealerInvCreator.createPotionEditorInv(p, Main.ins.getOneVsOnePlayer(p).getBlackDealerEffect(), null);
						}
						
					}
				} 
			   }
		   }
		 
		 }
		}
		
		
	}

	
	
}
