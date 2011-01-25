package org.bukkit.xmlns.spawnmob;

import net.minecraft.server.EntitySlime;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.MobType;
import org.bukkit.xmlns.spawnmob.Mob.MobException;


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
    	if(split[0].equalsIgnoreCase("/spawnmob")){
	    	if(1 < split.length && split.length < 4 ){
	    		String[] split1 = split[1].split(":");
	    		String[] split0 = null;
	    		CraftEntity spawned1 = null;
				Mob mob2 = null;
	    		if(split1.length == 1 && !split1[0].equalsIgnoreCase("Slime")){
	    			split0 = split[1].split(";");
	    			split1[0] = split0[0];
	    		}
	    		if(split1.length == 2){
	    			split[1] = split1[0] + "";
	    		}
	    		Mob mob = Mob.fromName(capitalCase(split1[0]));
	    		if(mob == null){
		    		event.getPlayer().sendMessage("Invalid mob type.");
		    		return;
	    		}
		    	if(!(SpawnMob.playerCanUse(event.getPlayer(), "spawnmob.spawnmob." + mob.name.toLowerCase()) || SpawnMob.playerCanUse(event.getPlayer(), "spawnmob." + mob.type.type))){
		    		event.getPlayer().sendMessage("You can't use this command.");
		    		return;
		    	}
				World world = ((org.bukkit.craftbukkit.CraftWorld)event.getPlayer().getWorld()).getHandle();
				CraftEntity spawned = null;
				try {
					spawned = mob.spawn(event.getPlayer(), plugin);
				} catch (MobException e) {
					event.getPlayer().sendMessage("Unable to spawn mob.");
					return;
				}
				org.bukkit.Location loc = event.getPlayer().getLocation();
				loc.setX(loc.getX() + 1);
				loc.setY(loc.getY() + 1);
				loc.setZ(loc.getZ() + 1);
				spawned.teleportTo(loc);
				world.a(spawned.getHandle());
				if(split0.length == 2){
					mob2 = Mob.fromName(capitalCase(split0[1]));
		    		if(mob2 == null){
			    		event.getPlayer().sendMessage("Invalid mob type.");
			    		return;
		    		}
					try {
						spawned1 = mob2.spawn(event.getPlayer(), plugin);
					} catch (MobException e) {
						event.getPlayer().sendMessage("Unable to spawn mob.");
						return;
					}
					spawned1.teleportTo(spawned);
					spawned1.getHandle().setPassengerOf(spawned.getHandle());
					world.a(spawned1.getHandle());
				}
				if(split1.length == 2 && mob.name == "Slime"){
					try{
						((EntitySlime) spawned.getHandle()).a(Integer.parseInt(split1[1]));
					}catch(Exception e){
						event.getPlayer().sendMessage("Malformed size.");
						return;
					}
				}
				if(split.length == 3){
					try{
    					for(int i = 1; i < Integer.parseInt(split[2]);i++){
    	    				spawned = mob.spawn(event.getPlayer(), plugin);
    	    				spawned.teleportTo(loc);
    	    				if(split1.length > 1 && mob.name == "Slime"){
    	    					try{
    	    						((EntitySlime) spawned.getHandle()).a(Integer.parseInt(split1[1]));
    	    					}catch(Exception e){
    	    						event.getPlayer().sendMessage("Malformed size.");
    	    						return;
    	    					}
    	    				}
    	    				world.a(spawned.getHandle());
    	    				if(split0.length == 2){
    	    		    		if(mob2 == null){
    	    			    		event.getPlayer().sendMessage("Invalid mob type.");
    	    			    		return;
    	    		    		}
    	    					try {
    	    						spawned1 = mob2.spawn(event.getPlayer(), plugin);
    	    					} catch (MobException e) {
    	    						event.getPlayer().sendMessage("Unable to spawn mob.");
    	    						return;
    	    					}
    	    					spawned1.teleportTo(spawned);
    	    					spawned1.getHandle().setPassengerOf(spawned.getHandle());
    	    					world.a(spawned1.getHandle());
    	    				}
    					}
    					event.getPlayer().sendMessage(split[2] + " " + mob.name.toLowerCase() + mob.s + " spawned.");
					}catch(MobException e1){
						event.getPlayer().sendMessage("Unable to spawn mobs.");
						return;
					}catch(java.lang.NumberFormatException e2){
						event.getPlayer().sendMessage("Malformed integer.");
						return;
					}
				}else{
					event.getPlayer().sendMessage(mob.name + " spawned.");
				}
				return;
			}
	    	event.getPlayer().sendMessage("Correct usage is: /spawnmob <Mob Name> (Amount)");
    		return;
    	}else if(split[0].equalsIgnoreCase("/mspawn")){
    		MobType mt = MobType.fromName(capitalCase(split[1]));
    		org.bukkit.block.Block blk = (new TargetBlock(event.getPlayer())).getTargetBlock();
    		if(mt == null){
    			event.getPlayer().sendMessage("Invalid mob type.");
    			return;
    		}
    		if(!SpawnMob.playerCanUse(event.getPlayer(), "spawnmob.mspawn." + mt.name().toLowerCase())){
    			event.getPlayer().sendMessage("You are not authorized to use that command.");
    			return;
    		}
    		if(split.length != 2){
    			event.getPlayer().sendMessage("Correct usage is: /mspawn <Mob Name>");
    			return;
    		}
    		if(blk == null){
    			event.getPlayer().sendMessage("You must be looking at a Mob Spawner.");
    			return;
    		}
    		if(blk.getTypeId() != 52){
    			event.getPlayer().sendMessage("You must be looking at a Mob Spawner.");
    			return;
    		}
    		((org.bukkit.block.MobSpawner) blk.getState()).setMobType(mt);
    		event.getPlayer().sendMessage("Mob spawner set as " + mt.getName().toLowerCase() + ".");
    	}
    }
    
    private String capitalCase(String s){
    	return s.toUpperCase().charAt(0) + s.toLowerCase().substring(1);
    }
}
