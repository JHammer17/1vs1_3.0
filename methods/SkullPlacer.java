package de.onevsone.methods;
 
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class SkullPlacer {

	
	/**
	 * 
	 * Sets the Skin of the Skull to texture
	 * 
	 * @param skinURL Skin texture
	 * @param skull Block, where the skull is
	 */
	@SuppressWarnings("deprecation")
	public void setSkullTexture(String texture, Block skull) {
        if (skull.getType() != Material.SKULL) skull.setType(Material.SKULL);
        if(texture == null || skull == null || texture.equalsIgnoreCase("")) {
        	setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM2MmRhYTQxYTEyNTRiMmVkNmU5ZmQzYTAxNWI0ZDdhNDE5YmEyOTIwZGUxMGI0NzgyNTFmNDlhYWNhMTkifX19", skull);
        	return;
        }
        try {
        	
        	Constructor<?> blockPosConst = getNMS("BlockPosition").getConstructor(int.class, int.class, int.class);
            Object blockPos = blockPosConst.newInstance(skull.getX(), skull.getY(), skull.getZ());
            
            Object cWorld = skull.getWorld().getClass().getMethod("getHandle").invoke(skull.getWorld());
            
            
            Method getTileEntity = cWorld.getClass().getMethod("getTileEntity", blockPos.getClass());
            Object tileEntity = getTileEntity.invoke(cWorld, blockPos);
            
            Method setGP = tileEntity.getClass().getMethod("setGameProfile", GameProfile.class);
            setGP.invoke(tileEntity, getGP(texture));
            
            skull.getWorld().refreshChunk(skull.getChunk().getX(), skull.getChunk().getZ());
        } catch (Exception e) {
			e.printStackTrace();
		}    
    }
	
	/**
	 * 
	 * Creates a Gameprofile with a Custom Texture
	 * 
	 * @param texture Skin texture
	 * @return Gameprofile with texture
	 */
	private GameProfile getGP(String texture) {
		GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(),  null);
		
		
		newSkinProfile.getProperties().put("textures", new Property("textures", texture));
		return newSkinProfile;
	}
	
	/**
	 * 
	 * Gets the NMS classes
	 * 
	 * @param name A Class Name
	 * @return Returns a Class from net.minecraft.server.VERSION
	 */
	private Class<?> getNMS(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "net.minecraft.server." + version + name;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	
}