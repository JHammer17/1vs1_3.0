package de.onevsone;


import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 20.03.2018 20:32:59					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public class CommentYAML extends YamlConfiguration {

	private HashMap<String, String> comments = new HashMap<>();
	
	private static HashMap<String, YamlConfiguration> configurations = new HashMap<>();
	
	public void save(File file, boolean keepComments) throws IOException {
        Validate.notNull(file, "File cannot be null");

        Files.createParentDirs(file);
        
        String bukkitData = configurations.get(file.getPath()).saveToString();
        
        
        StringBuilder data = new StringBuilder();
        
        
        /*
         * Adding Header Comments (Path = "")
         * Füge Header Kommentare hinzu, wenn kein Pfad gegeben ist (Path = "")
         */
        for(String p : comments.keySet()) 
        	if(p.equalsIgnoreCase("")) 
        		data.append("#").append(comments.get(p).replaceAll("\n", "\n#")).append("\n");
        
        
        
       for(String c : comments.keySet()) {
    	   if(c.equalsIgnoreCase("")) continue;
    	   String withComment = implementComment(c, comments.get(c));
    	   bukkitData = bukkitData.replaceAll(getLast(c) + ":", withComment + ":");
    	   
       }
       data.append(bukkitData);
        
       
        /*
         * Write it to File (von Spigot/Bukkit)
         * In die File schreiben (von Spigot/Bukkit) 
         */
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
        
        
        
        try {
            writer.write(data.toString());
        } finally {
            writer.close();
        }
        
        
        /*
         * Reset Comments
         * Resete Kommentare
         */
        if(!keepComments) resetComments();
    }
	
	@Override
	public String saveToString() {
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(Main.ins.utils.getPluginFile("Lustiges File"));
     	return cfg.saveToString();
	}
	
	@Override
	public void save(File file) throws IOException {
		save(file, false);
	}
	
	/**
	 * 
	 * @param comment Your comment without a '#'
	 * @param path Your path to Token like config.setting
	 * 
	 * Sets a comment for a path
	 * 
	 */
	public void setComment(String path, String comment) {
		
		if(path.equalsIgnoreCase("")) comments.put("", comment);
		
		if(!path.contains(".")) return;
		
		StringBuilder builder = new StringBuilder();
		
		boolean first = true;
		
		for(String str : path.split("[.]")) {
			try {
				
				Integer.parseInt(str);
				if(!first) 
					builder.append(".'").append(str).append("'");
				else {
					builder.append("'").append(str).append("'");
					first = false;
				}
				
			} catch (NumberFormatException e) {
				if(!first) 
					builder.append(".").append(str);
				else {
					builder.append(str);
					first = false;
				}
			}
		}
		
		comments.put(builder.toString(), comment);
		
		
		
		
		
		return;
	}
	
	
	/**
	 * Resets all comments
	 */
	public void resetComments() {
		comments.clear();
	}
	
	
	/**
	 * 
	 * @param path Path with Spaces
	 * @return Normal path with '.' instead of Spaces
	 * 
	 * You don't need it!
	 * 
	 * Replaces all '  ' with one '.'
	 * 
	 */
	private String getNormalPath(String path) {
		path = path.replaceAll("  ", ".");
		while(path.contains("..")) 
			path = path.replaceAll("[.][.]", ".");
		
		
		return path;
	}
	
	
	
	private String getSpaces(String path) {
		int amount = path.split("[.]").length;
		if(amount == 1) return "";
		
		
		amount = path.split("[.]").length;
		
		
		String ret = "";
		while(amount > 1) {
			amount--;
			ret = ret + "  ";
		}
		
		return ret;
	}
	
	public static void loadFile(File file) {
		while(configurations.containsKey(file.getPath()))
			configurations.remove(file.getPath());
		
		configurations.put(file.getPath(), YamlConfiguration.loadConfiguration(file));
		
		
//		loadConfiguration(file);
	}
	
	public void set(File file, String path, Object obj) {
		if(configurations.containsKey(file.getPath())) 
			configurations.get(file.getPath()).set(path, obj);
		
	}
	
	
	public String unnormalize(String path) {
		String unnormalized = "";
		
		unnormalized = path.replaceAll("[.]", ":");
		unnormalized = path.replaceAll("[.]", ":\n" + getSpaces(path));
		
		return unnormalized;
	}
	
	public String implementComment(String path, String comment) {
		if(!path.contains("[.]")) path = getNormalPath(path);
		
		
		//Config.setting3
		if(path.contains(".")) {
			String[] sPath = path.split("[.]");
			String last = sPath[sPath.length-1];
			//setting3
			
			path = unnormalize(path);
			
			path = "#" + comment.replaceAll("\n", "\n" + getSpaces(getNormalPath(path)) + "#")  + "\n"+ getSpaces(getNormalPath(path)) + last;
		}
		
		return path;
	}
	
	public String getLast(String path) {
		if(!path.contains("[.]")) path = getNormalPath(path);
		if(path.contains(".")) {
			String[] sPath = path.split("[.]");
			String last = sPath[sPath.length-1];
			return last;
		} else return "";
	}
	
	public void setHeader(File file, String comment) {
		if(configurations.containsKey(file.getPath())) {
			configurations.get(file.getPath()).options().header(comment);
			configurations.get(file.getPath()).options().copyHeader(true);
		}
	}
	
	
//	/*
//     * Go through all Values found in Config
//     * Hier wird durch alle gefundenen Werte in der Config gegangen
//     */
//    for(String datas : bukkitData.split("\n")) {
//    	
//    	/* 
//    	 * Creating a copy to save the orginal datas String and remove all ':'
//    	 * Erstelle Kopie, um originalen Datenstring zu erhalten und entferne ':'
//    	 */
//    	String copy = datas;
//    	copy = copy.split(":")[0];
//    	
//    	HashMap<String, String> commentsCP = (HashMap<String, String>) comments.clone();
//    	
//    	
//    	/* 
//    	 * When it starts with one or more Space(s) its a Main Token
//    	 * Wenn der Wert mit einem Space startet, ist er ein Haupttoken
//    	 */
//    	if(!copy.startsWith(" ")) {
//    		/* 
//    		 * Save Copy as Main path
//    		 * Copy wird nun der Main Pfad
//    		 */
//    		path = copy;
//    	} else {
//    		/* 
//    		 * else: add to Main Path
//    		 * Andernfalls: füge es zum Hauptpfad dazu
//    		 */
//    		path = path + copy;
//    		
//    		
//    	
//    	
//    	
//    		/*
//    		 * Path gets normalized (x  y  z => x.y.z) 
//    		 * Der Pfad wird normalisiert (x  y  z => x.y.z)
//    		 */
//    		String normal = getNormalPath(path);
//    		
//    		/*
//    		 * Check if normal path is in the comments list
//    		 * Check, ob ein Kommentar eingefügt werden muss
//    		 */
//    		Bukkit.broadcastMessage("" + comments + " §b" + normal);
//    		if(comments.containsKey(normal)) {
//    			/* Comment Found => Appending it
//    			 * Kommentar gefunden => Hinzufügen
//    			 */
//    			data.append(getSpaces(path) + "#" + commentsCP.get(normal).replaceAll("\n", "\n" + getSpaces(path) + "#")).append("\n");
//    		}
//    	}
//    	
//    	/*
//    	 * Appending rest of data and a \n to sepperate Line
//    	 * Rest wird angefügt und ein \n um eine neue Zeile zu starten
//    	 */
//    	data.append(datas).append("\n");
//    }
	
}
