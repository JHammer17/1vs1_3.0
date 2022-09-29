package de.onevsone.arenas.builder;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.onevsone.Main;
import de.onevsone.arenas.builder.worldReset.WorldResetMgr;

public class BlockMapReset {

	private Location to;

	
	private Location fromMin;
	private Location fromMax;
	

	private String arena;
	
	private int placed = 0;
	private int max = 0;
	private int placedBlocks = 0;
	private boolean worldLayout = false;


	private Player creator;
	
	
	public BlockMapReset(Location fromPos1, Location fromPos2, Location to, String arena, int maxBlocks, boolean worldLayout) {
		this.fromMax = new Location(fromPos1.getWorld(), Math.max(fromPos1.getX(), fromPos2.getX()), Math.max(fromPos1.getY(), fromPos2.getY()), Math.max(fromPos1.getZ(), fromPos2.getZ()));
		this.fromMin= new Location(fromPos1.getWorld(), Math.min(fromPos1.getX(), fromPos2.getX()), Math.min(fromPos1.getY(), fromPos2.getY()), Math.min(fromPos1.getZ(), fromPos2.getZ()));
		this.worldLayout = worldLayout;
		
		double Z = fromMax.getZ()-fromMin.getZ();
		
		
		this.to = to.add(0,0,Z);
		
		this.arena = arena;
		
		this.maxBlocks = maxBlocks;
	}
	
	public BlockMapReset(Location fromPos1, Location fromPos2, Location to, String arena, int maxBlocks, boolean worldLayout, Player creator) {
		this.fromMax = new Location(fromPos1.getWorld(), Math.max(fromPos1.getX(), fromPos2.getX()), Math.max(fromPos1.getY(), fromPos2.getY()), Math.max(fromPos1.getZ(), fromPos2.getZ()));
		this.fromMin= new Location(fromPos1.getWorld(), Math.min(fromPos1.getX(), fromPos2.getX()), Math.min(fromPos1.getY(), fromPos2.getY()), Math.min(fromPos1.getZ(), fromPos2.getZ()));
		this.worldLayout = worldLayout;
		
		double Z = fromMax.getZ()-fromMin.getZ();
		
		
		this.to = to.add(0,0,Z);
		
		this.arena = arena;
		
		this.maxBlocks = maxBlocks;
		this.creator = creator;
	}
	
	int runnable = 0;
	
	
	int oldX = 0;
	int oldZ = 0;
	
	
	
	int maxOffsetX = 0;
	int maxOffsetZ = 0;
	
	Location Ecke1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	Location Ecke2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	
	int maxBlocks = 500;
	
	int acX = 0;
	int acY = 0;
	int acZ = 0;
	
	int offsetX = 0;
	int offsetZ = 0;
	int offsetY = 0;
	
	boolean readyX = false;
	boolean readyY = false;
	boolean readyZ = false;
	
	boolean wasCancel = false;
	
	boolean finished = false;
	

	
	
	public void copy() {
		acX = fromMin.getBlockX();
		acY = fromMin.getBlockY();
		acZ = fromMax.getBlockZ();
		
		runnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.ins, new Runnable() {
			
			@SuppressWarnings({ "deprecation", "static-access" })
			@Override
			public void run() {
				
				
				int minDistanzWalls = Main.ins.minArenaBuildDistanceWalls+1;
				int minDistanzTop = Main.ins.minArenaBuildDistanceTop;
				int minDistanzBottom = Main.ins.minArenaBuildDistanceBottom+1;
				
				
				
				Location acLoc = null;
				
				double Blocks = 0;
				
				
				
				for(int y = acY; y <= fromMax.getBlockY(); y++) {
					if(!wasCancel) {
						offsetY++;
						offsetX = 0;
					}
					for(int x = acX; x <= fromMax.getBlockX(); x++) {
						if(!wasCancel) {
							offsetX++;
							offsetZ = 0;
						}
						
						for(int z = acZ; z >= fromMin.getBlockZ(); z--) {
							if(!wasCancel) {
							 offsetZ++;
							} else {
								wasCancel = false;
							}
							
							Location loc = to.clone().add(-1,-1,-1).add(offsetX,offsetY,offsetZ*-1+2);
							
							Blocks++;
							
							
							Block from = new Location(fromMin.getWorld(), x, y, z).getBlock();
						
							Location newLoc = loc;
						 	
							Block b = from;
						 		
							if(newLoc.getBlock().getType() == Material.CHEST || newLoc.getBlock().getType() == Material.TRAPPED_CHEST) {
								 Chest c = (Chest) newLoc.getBlock().getState();
								 c.getInventory().clear();
								}
								
								if(newLoc.getBlock().getType() == Material.DISPENSER) {
								 Dispenser d = (Dispenser) newLoc.getBlock().getState();
								 d.getInventory().clear();
								}
								
								if(newLoc.getBlock().getType() == Material.DROPPER) {
								 Dropper d = (Dropper) newLoc.getBlock().getState();
								 d.getInventory().clear();
								}
								
								if(newLoc.getBlock().getType() == Material.FURNACE) {
								 Furnace f = (Furnace) newLoc.getBlock().getState();
								 f.getInventory().clear();
								}
								
								if(newLoc.getBlock().getType() == Material.BREWING_STAND) {
								 BrewingStand br = (BrewingStand) newLoc.getBlock().getState();
								 br.getInventory().clear();
								}
								
								if(newLoc.getBlock().getType() == Material.HOPPER) {
								 Hopper h = (Hopper) newLoc.getBlock().getState();
								 h.getInventory().clear();
								}
							
							acLoc = newLoc;
								
							if(b.getType() == Material.TORCH 
									|| b.getType() == Material.REDSTONE_TORCH_OFF 
									|| b.getType() == Material.REDSTONE_TORCH_ON 
									|| b.getType() == Material.TRAP_DOOR 
									|| b.getType() == Material.IRON_TRAPDOOR 
									|| b.getType() == Material.BED_BLOCK 
									|| b.getType() == Material.WOOD_BUTTON 
									|| b.getType() == Material.STONE_BUTTON 
									|| b.getType() == Material.LEVER 
									|| b.getType() == Material.LADDER 
									|| b.getType() == Material.WALL_SIGN
									|| b.getType() == Material.WALL_BANNER
									|| b.getType() == Material.STANDING_BANNER
									|| b.getType() == Material.VINE) {
								
								doLater(newLoc, b);
							} else {
								newLoc.getBlock().setType(b.getType());
							    newLoc.getBlock().setData(b.getData());
							    
							    if(b.getType() == Material.SIGN_POST && newLoc.getBlock().getType() == Material.SIGN_POST) {
							    	
							    	Sign copy = (Sign) b.getState();
							    	Sign paste = (Sign) newLoc.getBlock().getState();
							    	
							    	paste.setLine(0, copy.getLine(0));
							    	paste.setLine(1, copy.getLine(1));
							    	paste.setLine(2, copy.getLine(2));
							    	paste.setLine(3, copy.getLine(3));
							    	
							    	paste.update();
							    	
							    }
							    
							    if(b.getType() == Material.SKULL && newLoc.getBlock().getType() == Material.SKULL) {
							    	Skull copy = (Skull) b.getState();
							    	Skull paste = (Skull) newLoc.getBlock().getState();
							    	
							    	paste.setSkullType(copy.getSkullType());
							    	paste.setOwner(copy.getOwner());
							    	paste.setRotation(copy.getRotation());
							    	paste.update();
							    	
							    	
							    }
							}
							
							    acX = x;
							    acY = y;
							    acZ = z;
							    
							    if(offsetX == minDistanzWalls && offsetZ == minDistanzWalls && minDistanzBottom == offsetY) {
								   	Ecke1 = loc;
								   
								 }
							    
								 if(offsetX == -1*minDistanzWalls && offsetZ == minDistanzWalls && minDistanzBottom == offsetY) {
									Ecke1 = loc;
									
								 }
								 
								 if(offsetX == minDistanzWalls && offsetZ == -1*minDistanzWalls && minDistanzBottom == offsetY) {
									Ecke1 = loc;
									
								 }
								 
								 if(offsetX == -1*minDistanzWalls && offsetZ == -1*minDistanzWalls && minDistanzBottom == offsetY) {
									Ecke1 = loc;
								 }
								 
								 placed++;
									
									
								
								 
									
								 	Bukkit.getScheduler().runTaskAsynchronously(Main.ins, new Runnable() {
										
										@Override
										public void run() {
											
											
											int minX = fromMin.getBlockX();
											int maxX = fromMax.getBlockX();
											
											int minY = fromMin.getBlockY();
											int maxY = fromMax.getBlockY();
											
											int minZ = fromMin.getBlockZ();
											int maxZ = fromMax.getBlockZ();
											
											int blocks = 0;
											
											int disX = maxX-minX;
											int disY = maxY-minY;
											int disZ = maxZ-minZ;
											
											blocks = (disX+1)*(disY+1)*(disZ+1);
											max = blocks;
											 placedBlocks = placed;
											
										}
									});
									
									
								if(Blocks >= maxBlocks) {
									wasCancel = true;
									break;
								}
						}
						if(Blocks >= maxBlocks) {
							wasCancel = true;
							break;
						} else {
							acZ = fromMax.getBlockZ();
							readyZ = true;
						}
					}
					if(Blocks >= maxBlocks) {
						wasCancel = true;
						break;
					} else {
						acZ = fromMax.getBlockZ();
						acX = fromMin.getBlockX();
						readyX = true;
					}
					                         
				}
				if(!wasCancel) {
					readyY = true;
				}
				
				Blocks = 0;
				
				if(readyX && readyY && readyZ) {
					int XX = offsetX;
					int ZZ = offsetZ*-1+2;
					
					if(XX > 0) {
						XX = XX-minDistanzWalls+1;
					} else {
						XX = XX+minDistanzWalls+1;
					}
					
					if(ZZ > 0) {
						ZZ = ZZ-minDistanzWalls-1;
					} else {
						ZZ = ZZ+minDistanzWalls-1;
					}
					
					Location loc = to.clone().add(-1,-1,-1).add(XX,offsetY-minDistanzTop,ZZ);
					Ecke2 = loc;
					placeWoolTest(loc);
					placeWoolTest(Ecke1);
					Bukkit.getScheduler().cancelTask(runnable);	
					
					
					if(!worldLayout) {
						Main.ins.getOneVsOneArena(arena).setBuildCorner1(Ecke1);
						Main.ins.getOneVsOneArena(arena).setBuildCorner2(Ecke2);
						
						Main.ins.getOneVsOneArena(arena).setCorner1(to);
						Main.ins.getOneVsOneArena(arena).setCorner2(acLoc);
						
						Main.ins.getOneVsOneArena(arena).setReady(true);
							
						if(Main.ins.getOneVsOneArena(arena).isUsed()) {
							Main.ins.getOneVsOneArena(arena).startScheduler();
						}
					} else {
						
						File f = new File(arena + "/data.yml");
						if(!f.exists()) {
							try {
								f.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
						
						cfg.set("Data.finishedBuilding", true);
						
						cfg.set("Data.BuildCorner1.X", Ecke1.getBlockX());
						cfg.set("Data.BuildCorner1.Y", Ecke1.getBlockY());
						cfg.set("Data.BuildCorner1.Z", Ecke1.getBlockZ());
						
						cfg.set("Data.BuildCorner2.X", Ecke2.getBlockX());
						cfg.set("Data.BuildCorner2.Y", Ecke2.getBlockY());
						cfg.set("Data.BuildCorner2.Z", Ecke2.getBlockZ());
						
						cfg.set("Data.corner1.X", to.getBlockX());
						cfg.set("Data.corner1.Y", to.getBlockY());
						cfg.set("Data.corner1.Z", to.getBlockZ());
						
						cfg.set("Data.corner2.X", acLoc.getBlockX());
						cfg.set("Data.corner2.Y", acLoc.getBlockY());
						cfg.set("Data.corner2.Z", acLoc.getBlockZ());
						
						
						
						try {
							cfg.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						WorldResetMgr.finishLayoutCreation(arena, creator);
						
					}
					finished = true;
				}
				
			}
		}, 0, 2);
		
		
		
	}
	
	private void placeWoolTest(final Location loc) {
		//loc.getBlock().setType(Material.WOOL);
	}
	
	
	
	
	
	
		
	
	
	@SuppressWarnings("deprecation")
	private void doLater(final Location newLoc, final Block b) {
		newLoc.getBlock().setType(b.getType());
	    newLoc.getBlock().setData(b.getData());
	    
	    
	    if(b.getType() == Material.WALL_SIGN && newLoc.getBlock().getType() == Material.WALL_SIGN) {
	    	Sign copy = (Sign) b.getState();
	    	Sign paste = (Sign) newLoc.getBlock().getState();
	    	
	    	paste.setLine(0, copy.getLine(0));
	    	paste.setLine(1, copy.getLine(1));
	    	paste.setLine(2, copy.getLine(2));
	    	paste.setLine(3, copy.getLine(3));
	    	
	    	paste.update();
	    	
	    }
	    
	    if((b.getType() == Material.WALL_BANNER && newLoc.getBlock().getType() == Material.WALL_BANNER) || (b.getType() == Material.STANDING_BANNER && newLoc.getBlock().getType() == Material.STANDING_BANNER)) {
	    	
	    	Banner copy = (Banner) b.getState();
	    	Banner paste = (Banner) newLoc.getBlock().getState();
	    	
	    	paste.setBaseColor(copy.getBaseColor());
	    	paste.setPatterns(copy.getPatterns());
	    	paste.setData(copy.getData());
	    	
	    	
	    	paste.update();
	    	
	    }
	   
	    
	    if(b.getType() == Material.SKULL && newLoc.getBlock().getType() == Material.SKULL) {
	    	Skull copy = (Skull) b.getState();
	    	Skull paste = (Skull) newLoc.getBlock().getState();
	    	
	    	paste.setSkullType(copy.getSkullType());
	    	paste.setOwner(copy.getOwner());
	    	paste.setRotation(copy.getRotation());
	    	
	    	
	    	
	    	paste.update();
	    	
	    }
	    
	}
	
	public int getPlaced() {
		return this.placedBlocks;
	}
	
	public int getMax() {
		return this.max;
	}
	
	public double getPercent() {
		return (double) Math.round(((double)placedBlocks/(double)max)*(double)100);
	}
	
	
}

