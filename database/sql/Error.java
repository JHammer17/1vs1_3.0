package de.onevsone.database.sql;

import java.util.logging.Level;

import de.onevsone.Main;


public class Error {
	
	public static void execute(Main plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(Main plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
