package org.bukkit.xmlns.spawnmob;

import java.io.File;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import com.nijiko.Messaging;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * SpawnMob for Bukkit
 *
 * @author xmlns
 */
public class SpawnMob extends JavaPlugin {
	public java.util.logging.Logger log = java.util.logging.Logger.getLogger("Minecraft");
	public static Permissions permissions = null;
    private final SMPlayerListener playerListener = new SMPlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    public void setupPermissions() {
    	Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
    	if(SpawnMob.permissions == null) {
    	    if(test != null) {
    		SpawnMob.permissions = (Permissions)test;
    	    } else {
    	    	PluginDescriptionFile pdfFile = this.getDescription();
	    		log.info(Messaging.bracketize(pdfFile.getName()) + " Permission system not enabled. Using ops.txt.");
    	    }
    	}
    }

    public SpawnMob(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }
    
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND, playerListener , Priority.Normal, this);
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled." );
    }
    
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
    	log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled.");
    }
    
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public static boolean playerCanUse(Player p){
    	if(SpawnMob.permissions != null)
    	{
    		if(Permissions.Security.permission(p, "spawnmob.spawnmob")){
    			return true;
    		}
    	}else{
    		if(p.isOp()){
    			return true;
    		}
    	}
    	return false;
    }
}
