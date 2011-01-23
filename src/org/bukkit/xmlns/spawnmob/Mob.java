package org.bukkit.xmlns.spawnmob;

import java.lang.reflect.Constructor;
import net.minecraft.server.Entity;
import net.minecraft.server.WorldServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.CraftServer;

public enum Mob {
	CHICKEN	("Chicken"),
	COW		("Cow"),
	CREEPER	("Creeper"),
	GHAST	("Ghast"),
	GIANT	("Giant","ZombieSimple"),
	PIG		("Pig"),
	PIGZOMB	("PigZombie"),
	SHEEP	("Sheep"),
	SKELETON("Skeleton"),
	SLIME	("Slime"),
	SPIDER	("Spider"),
	SQUID	("Squid"),
	ZOMBIE	("Zombie");
	
	
	private Mob(String n){
		this.name = n;
		this.entityClass = n;
	}
	private Mob(String n, String ec){
		this.name = n;
		this.entityClass = ec;
	}
	
	public String name;
	private String entityClass;
	
	@SuppressWarnings("unchecked")
	public CraftEntity spawn(org.bukkit.entity.Player player, SpawnMob plugin){
		try {
			WorldServer world = ((org.bukkit.craftbukkit.CraftWorld) player.getWorld()).getHandle();
			Constructor<CraftEntity> craft = (Constructor<CraftEntity>) ClassLoader.getSystemClassLoader().loadClass("org.bukkit.craftbukkit.entity.Craft" + name).getConstructors()[0];
			Constructor<Entity> entity = (Constructor<Entity>) ClassLoader.getSystemClassLoader().loadClass("net.minecraft.server.Entity" + entityClass).getConstructors()[0];
			return craft.newInstance((CraftServer) plugin.getServer(),entity.newInstance( world ) );
		} catch (Exception e) {
			plugin.log.log(java.util.logging.Level.SEVERE,"Unable to spawn mob. Error: ");
			e.printStackTrace();
			return null;
		}
	}
}
