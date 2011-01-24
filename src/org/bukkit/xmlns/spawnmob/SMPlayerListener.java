package org.bukkit.xmlns.spawnmob;

import net.minecraft.server.EntitySlime;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.entity.CraftEntity;


/**
 * Handle events for all Player related events
 * @author xmlns
 */
public class SMPlayerListener extends org.bukkit.event.player.PlayerListener {
    private final SpawnMob plugin;

    public SMPlayerListener(SpawnMob instance) {
        plugin = instance;
    }

    public void onPlayerCommand(org.bukkit.event.player.PlayerChatEvent event){
    	String[] split = event.getMessage().split(" ");
    	if(!split[0].equalsIgnoreCase("/spawnmob")) return;
    	if(1 < split.length && split.length < 4 ){
    		String[] split1 = split[1].split(":");
    		if(split1.length == 2){
    			split[1] = split1[0] + "";
    		}
    		for(Mob mob : Mob.values()){
    			if(mob.name.equalsIgnoreCase(split[1])){
    		    	if(!(SpawnMob.playerCanUse(event.getPlayer(), "spawnmob." + mob.name.toLowerCase()) || SpawnMob.playerCanUse(event.getPlayer(), "spawnmob." + mob.type.type))){
    		    		event.getPlayer().sendMessage("You can't use this command.");
    		    		return;
    		    	}
    				World world = ((org.bukkit.craftbukkit.CraftWorld)event.getPlayer().getWorld()).getHandle();
    				CraftEntity spawned = mob.spawn(event.getPlayer(), plugin);
    				if(spawned == null) return;
    				spawned.teleportTo(event.getPlayer());
    				if(split1.length == 2 && mob.name == "Slime"){
    					try{
    						((EntitySlime) spawned.getHandle()).a(Integer.parseInt(split1[1]));
    	    				world.a(spawned.getHandle());
    					}catch(Exception e){
    						event.getPlayer().sendMessage("Malformed size.");
    						return;
    					}
    				}else{
        				world.a(spawned.getHandle());
    				}
    				if(split.length == 3){
    					try{
	    					for(int i = 1; i < Integer.parseInt(split[2]);i++){
	    	    				spawned = mob.spawn(event.getPlayer(), plugin);
	    	    				spawned.teleportTo(event.getPlayer());
	    	    				if(split1.length > 1 && mob.name == "Slime"){
	    	    					try{
	    	    						((EntitySlime) spawned.getHandle()).a(Integer.parseInt(split1[1]));
	    	    					}catch(Exception e){
	    	    						event.getPlayer().sendMessage("Malformed size.");
	    	    						return;
	    	    					}
	    	    				}
	    	    				world.a(spawned.getHandle());
	    					}
	    					event.getPlayer().sendMessage(split[2] + " " + mob.name.toLowerCase() + mob.s + " spawned.");
    					}catch(Exception e){
    						event.getPlayer().sendMessage("Malformed integer.");
    						return;
    					}
    				}else{
    					event.getPlayer().sendMessage(mob.name + " spawned.");
    				}
    				return;
    			}
    		}
    		event.getPlayer().sendMessage("Mob not found.");
    		return;
    	}
    	event.getPlayer().sendMessage("Correct usage is: /spawnmob <Mob Name> (Amount)");
    }
}
