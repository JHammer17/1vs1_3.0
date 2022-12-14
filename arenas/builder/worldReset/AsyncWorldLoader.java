package de.onevsone.arenas.builder.worldReset;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

public class AsyncWorldLoader {
//    private static World ret = null;
//    private static boolean aborted = false;
////    private static boolean alreadyLoading = false;
//    private static ChunkGenerator generator;
    
    public static World createAsyncWorld(WorldCreator creator) {
    	
//    	return Bukkit.createWorld(creator);
		
    	try {
    		CraftServer cS = ((CraftServer)Bukkit.getServer());
    		World w = cS.createWorld(creator);
    		return w;
    	} catch (Exception e) {
    		return null;
		} 
    	
    	
    	
		

    }
    
//    public World createAsyncWorld1(WorldCreator creator) {
////      while (alreadyLoading) {
////      try {
////          Thread.sleep(50);
////      } catch (InterruptedException e) {
////          e.printStackTrace();
////      }
////  }
//  
//  
////  alreadyLoading = true;
//	
//	
//  aborted = false;
//  
//  generator = null;
//  ret = null;
//  Validate.notNull(creator, "Creator may not be null");
//
//  String name = creator.name();
//  generator = creator.generator();
//  File folder = new File(getWorldContainer(), name);
//  World world = getCraftServer().getWorld(name);
//  net.minecraft.server.v1_8_R3.WorldType type = net.minecraft.server.v1_8_R3.WorldType.getType(creator.type().getName());
//  boolean generateStructures = creator.generateStructures();
//
//  if (world != null) {
//      return world;
//  }
//
//  if ((folder.exists()) && (!folder.isDirectory())) {
//      throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
//  }
//
//  if (generator == null) {
//      generator = getGenerator(name);
//     
//  }
//  
//  
//  new BukkitRunnable() {
//      public void run() {
//          int dimension2 = 10 + getServer().worlds.size();
//          boolean used = false;
//          do
//              for (WorldServer server : getServer().worlds) {
//                  used = server.dimension == dimension2;
//                  if (used) {
//                      dimension2++;
//                      break;
//                  }
//              }
//          while (used);
//          
//          
//          boolean hardcore = false;
//          final int dimension = dimension2;
//          new Thread() {
//              public void run() {
//
//                  Object sdm = new ServerNBTManager(getWorldContainer(),
//                          name, true);
//                  WorldData worlddata = ((IDataManager) sdm)
//                          .getWorldData();
//                  if (worlddata == null) {
//                      @SuppressWarnings("deprecation")
//						WorldSettings worldSettings = new WorldSettings(
//                              creator.seed(),
//                              WorldSettings.EnumGamemode
//                                      .getById(getCraftServer()
//                                              .getDefaultGameMode()
//                                              .getValue()),
//                              generateStructures, hardcore, type);
//                      worldSettings.setGeneratorSettings(creator
//                              .generatorSettings());
//                      worlddata = new WorldData(worldSettings, name);
//                  }
//                  worlddata.checkName(name);
//                  
//                 
//                  WorldServer internal = (WorldServer) new WorldServer(
//                          getServer(), (IDataManager) sdm, worlddata,
//                          dimension, getServer().methodProfiler,
//                          creator.environment(), generator).b();
//                  new BukkitRunnable() {
//                      @SuppressWarnings("unchecked")
//						public void run() {
//                          try {
//                              Field w = CraftServer.class.getDeclaredField("worlds");
//                              w.setAccessible(true);
//                              if (!((Map<String, World>) w.get(getCraftServer())).containsKey(name.toLowerCase())) {
//                                  aborted = true;
//                                  return;
//                              }
//                          } catch (Exception e) {
//                              e.printStackTrace();
//                              aborted = true;
//                              return;
//                          }
//                          new Thread() {
//                              public void run() {
//                                  internal.scoreboard = getCraftServer()
//                                          .getScoreboardManager()
//                                          .getMainScoreboard()
//                                          .getHandle();
//                                  internal.tracker = new EntityTracker(
//                                          internal);
//                                  internal.addIWorldAccess(new WorldManager(
//                                          getServer(), internal));
//                                  internal.worldData
//                                          .setDifficulty(EnumDifficulty.EASY);
//                                  internal.setSpawnFlags(true, true);
//                                  getServer().worlds.add(internal);
//
//                                  if (generator != null) {
//                                      internal.getWorld()
//                                              .getPopulators()
//                                              .addAll(generator
//                                                      .getDefaultPopulators(internal
//                                                              .getWorld()));
//                                  }
//
//                                  new BukkitRunnable() {
//                                      public void run() {
//                                          Bukkit.getPluginManager()
//                                                  .callEvent(
//                                                          new WorldInitEvent(
//                                                                  internal.getWorld()));
//                                      }
//                                  }.runTask(Main.ins);
////                                  
////                                  new BukkitRunnable() {
////                                      public void run() {
////                                          Bukkit.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
////                                      }
////                                  }.runTask(Main.ins);
//                                  ret = internal.getWorld();
//                              }
//
//                              @SuppressWarnings("unused")
//								private Chunk getChunkAt(ChunkProviderServer cps, int i, int j) {
//                                  Runnable runnable = null;
//                                  cps.unloadQueue.remove(i, j);
//                                  Chunk chunk = (Chunk)cps.chunks.get(LongHash.toLong(i, j));
//                                  ChunkRegionLoader loader = null;
//                                  try{
//                                      Field f = ChunkProviderServer.class.getDeclaredField("chunkLoader");
//                                      f.setAccessible(true);
//                                      if ((f.get(cps) instanceof ChunkRegionLoader)) {
//                                          loader = (ChunkRegionLoader)f.get(cps);
//                                      }
//                                  }catch(Exception e){
//                                      e.printStackTrace();
//                                  }
//
////                                  if ((chunk == null) && (loader != null) && (loader.chunkExists(cps.world, i, j))) {
////                                      final ChunkRegionLoader loader1 = loader;
////                                      wait = null;
////                                      new BukkitRunnable(){public void run(){
////                                          wait = ChunkIOExecutor.syncChunkLoad(cps.world, loader1, cps, i, j);
////                                      }}.runTask(Main.ins);
////                                      while(wait==null){
////                                          try {
////                                              Thread.sleep(10);
////                                          } catch (InterruptedException e) {
////                                              e.printStackTrace();
////                                          }
////                                      }
////                                      chunk = wait;
////                                  }
////                                  else
//                                  if (chunk == null) {
//                                      chunk = originalGetChunkAt(cps, i, j);
//                                  }
//
//                                  if (runnable != null) {
//                                      runnable.run();
//                                  }
//
//                                  return chunk;
//                              }
//                              public Chunk originalGetChunkAt(ChunkProviderServer cps, int i, int j) {
//                                  cps.unloadQueue.remove(i, j);
//                                  Chunk chunk = (Chunk)cps.chunks.get(LongHash.toLong(i, j));
//                                  boolean newChunk = false;
//
//                                  if (chunk == null) {
//                                      cps.world.timings.syncChunkLoadTimer.startTiming();
//                                      chunk = cps.loadChunk(i, j);
//                                      if (chunk == null) {
//                                          if (cps.chunkProvider == null)
//                                              chunk = cps.emptyChunk;
//                                          else {
//                                              try {
//                                                  chunk = cps.chunkProvider.getOrCreateChunk(i, j);
//                                              } catch (Throwable throwable) {
//                                                  CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
//                                                  CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");
//
//                                                  crashreportsystemdetails.a("Location", String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
//                                                  crashreportsystemdetails.a("Position hash", Long.valueOf(LongHash.toLong(i, j)));
//                                                  crashreportsystemdetails.a("Generator", cps.chunkProvider.getName());
//                                                  throw new ReportedException(crashreport);
//                                              }
//                                          }
//                                          newChunk = true;
//                                      }
//
//                                      cps.chunks.put(LongHash.toLong(i, j), chunk);
//                                      final Chunk chunki = chunk;
//                                      final boolean newChunki = newChunk;
//                                      new BukkitRunnable(){public void run(){
//                                          chunki.addEntities();
//
//                                          Server server = cps.world.getServer();
//                                          if (server != null)
//                                          {
//                                              server.getPluginManager().callEvent(new ChunkLoadEvent(chunki.bukkitChunk, newChunki));
//                                          }
//                                      }}.runTask(Main.ins);
//
//                                      for (int x = -2; x < 3; x++) {
//                                          for (int z = -2; z < 3; z++) {
//                                              if ((x == 0) && (z == 0)) continue;
//
//                                              Chunk neighbor = cps.getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
//                                              if (neighbor != null) {
//                                                  neighbor.setNeighborLoaded(-x, -z);
//                                                  chunk.setNeighborLoaded(x, z);
//                                              }
//                                          }
//                                      }
//
//                                      loadNearby(chunk, cps, cps, i, j);
//                                      cps.world.timings.syncChunkLoadTimer.stopTiming();
//                                  }
//
//                                  return chunk;
//                              }
//                              public void loadNearby(Chunk c, IChunkProvider ichunkprovider, IChunkProvider ichunkprovider1, int i, int j) {
//                                  c.world.timings.syncChunkLoadPostTimer.startTiming();
//                                  boolean flag = ichunkprovider.isChunkLoaded(i, j - 1);
//                                  boolean flag1 = ichunkprovider.isChunkLoaded(i + 1, j);
//                                  boolean flag2 = ichunkprovider.isChunkLoaded(i, j + 1);
//                                  boolean flag3 = ichunkprovider.isChunkLoaded(i - 1, j);
//                                  boolean flag4 = ichunkprovider.isChunkLoaded(i - 1, j - 1);
//                                  boolean flag5 = ichunkprovider.isChunkLoaded(i + 1, j + 1);
//                                  boolean flag6 = ichunkprovider.isChunkLoaded(i - 1, j + 1);
//                                  boolean flag7 = ichunkprovider.isChunkLoaded(i + 1, j - 1);
//
//                                  if ((flag1) && (flag2) && (flag5)) {
//                                      if (!c.isDone())
//                                          getChunkAt((ChunkProviderServer) ichunkprovider1, i, j);
//                                      else {
//                                          ichunkprovider.a(ichunkprovider1, c, i, j);
//                                      }
//
//                                  }
//
//                                  if ((flag3) && (flag2) && (flag6)) {
//                                      Chunk chunk = getOrCreateChunk((ChunkProviderServer) ichunkprovider, i - 1, j);
//                                      if (!chunk.isDone())
//                                          getChunkAt((ChunkProviderServer) ichunkprovider1, i - 1, j);
//                                      else {
//                                          ichunkprovider.a(ichunkprovider1, chunk, i - 1, j);
//                                      }
//                                  }
//
//                                  if ((flag) && (flag1) && (flag7)) {
//                                      Chunk chunk = getOrCreateChunk((ChunkProviderServer) ichunkprovider, i, j - 1);
//                                      if (!chunk.isDone())
//                                          getChunkAt((ChunkProviderServer) ichunkprovider1, i, j - 1);
//                                      else {
//                                          ichunkprovider.a(ichunkprovider1, chunk, i, j - 1);
//                                      }
//                                  }
//
//                                  if ((flag4) && (flag) && (flag3)) {
//                                      Chunk chunk = getOrCreateChunk((ChunkProviderServer) ichunkprovider, i - 1, j - 1);
//                                      if (!chunk.isDone())
//                                          getChunkAt((ChunkProviderServer) ichunkprovider1, i - 1, j - 1);
//                                      else {
//                                          ichunkprovider.a(ichunkprovider1, chunk, i - 1, j - 1);
//                                      }
//                                  }
//
//                                  c.world.timings.syncChunkLoadPostTimer.stopTiming();
//                              }
//                              @SuppressWarnings("unused")
//								public boolean a(IChunkProvider ichunkprovider, Chunk chunk, int i, int j)
//                              {
//                                  if ((ichunkprovider != null) && (ichunkprovider.a(ichunkprovider, chunk, i, j))) {
//                                      Chunk chunk1 = getOrCreateChunk((ChunkProviderServer) ichunkprovider, i, j);
//
//                                      chunk1.e();
//                                      return true;
//                                  }
//                                  return false;
//                              }
//
//                              private Chunk getOrCreateChunk(
//                                      ChunkProviderServer ip,
//                                      int i, int j) {
//
//
//                                  Chunk chunk = (Chunk)ip.chunks.get(LongHash.toLong(i, j));
//
//                                  chunk = chunk == null ? getChunkAt(ip, i, j) : (!ip.world.ad()) && (!ip.forceChunkLoad) ? ip.emptyChunk : chunk;
//
//                                  if (chunk == ip.emptyChunk) return chunk;
//                                  if ((i != chunk.locX) || (j != chunk.locZ)) {
//                                      System.err.println("Chunk (" + chunk.locX + ", " + chunk.locZ + ") stored at  (" + i + ", " + j + ") in world '" + ip.world.getWorld().getName() + "'");
//                                      System.err.println(chunk.getClass().getName());
//                                      Throwable ex = new Throwable();
//                                      ex.fillInStackTrace();
//                                      ex.printStackTrace();
//                                  }
//
//                                  return chunk;
//
//                              }
//
//                          }.start();
//                      }
//                  }.runTask(Main.ins);
//              }
//          }.start();
//      }
//  }.runTask(Main.ins);
//  while (ret == null && !aborted) {
//      try {
//          Thread.sleep(50);
//      } catch (InterruptedException e) {
//          e.printStackTrace();
//      }
//  }
//  try {
//      Thread.sleep(1000);
//  } catch (InterruptedException e) {
//      e.printStackTrace();
//  }
////  alreadyLoading = false;
//  return ret;
//    }
//    
// 
//    private static File getWorldContainer() {
//        if (getServer().universe != null) {
//            return getServer().universe;
//        }
//        try {
//            Field container = CraftServer.class.getDeclaredField("container");
//            container.setAccessible(true);
//            Field settings = CraftServer.class.getDeclaredField("configuration");
//            settings.setAccessible(true);
//            File co = (File) container.get(getCraftServer());
//            if (co == null) container.set(getCraftServer(), new File(((YamlConfiguration) settings.get(getCraftServer())).getString("settings.world-container", ".")));
// 
//            return (File) container.get(getCraftServer());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
// 
//    private static MinecraftServer getServer() {
//        return getCraftServer().getServer();
//    }
// 
//    private static CraftServer getCraftServer() {
//        return ((CraftServer) Bukkit.getServer());
//    }
// 
//    private static ChunkGenerator getGenerator(String world) {
//        try {
//            Field settings = CraftServer.class.getDeclaredField("configuration");
//            settings.setAccessible(true);
//            ConfigurationSection section = ((YamlConfiguration) settings.get(getCraftServer())).getConfigurationSection("worlds");
//            ChunkGenerator result = null;
// 
//            if (section != null) {
//                section = section.getConfigurationSection(world);
// 
//                if (section != null) {
//                    String name = section.getString("generator");
// 
//                    if ((name != null) && (!name.equals(""))) {
//                        String[] split = name.split(":", 2);
//                        String id = split.length > 1 ? split[1] : null;
//                        Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);
// 
//                        if (plugin == null)
//                            Bukkit.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
//                        else if (!plugin.isEnabled())
//                            Bukkit.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
//                        else {
//                            try {
//                                result = plugin.getDefaultWorldGenerator(world, id);
//                                if (result == null)
//                                    Bukkit.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
//                            } catch (Throwable t) {
//                                plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
//                            }
//                        }
//                    }
//                }
//            }
// 
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}