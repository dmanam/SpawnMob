package org.bukkit.xmlns.spawnmob;

import java.lang.reflect.Constructor;
import net.minecraft.server.Entity;
import net.minecraft.server.WorldServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.CraftServer;

public enum Mob {
	CHICKEN	("Chicken", Enemies.FRIENDLY),
	COW		("Cow", Enemies.FRIENDLY),
	CREEPER	("Creeper", Enemies.ENEMY),
	GHAST	("Ghast", Enemies.ENEMY),
	GIANT	("Giant","ZombieSimple", Enemies.ENEMY),
	PIG		("Pig", Enemies.FRIENDLY),
	PIGZOMB	("PigZombie", Enemies.NEUTRAL),
	SHEEP	(false, "Sheep", Enemies.FRIENDLY),
	SKELETON("Skeleton", Enemies.ENEMY),
	SLIME	("Slime", Enemies.ENEMY),
	SPIDER	("Spider", Enemies.ENEMY),
	SQUID	("Squid", Enemies.FRIENDLY),
	ZOMBIE	("Zombie", Enemies.ENEMY);
	
	private Mob(boolean b, String n, Enemies en){
		this.s = "";
		name = n;
		craftClass = n;
		entityClass = n;
		this.type = en;
	}
	private Mob(String n, Enemies en){
		this.name = n;
		this.craftClass = n;
		this.entityClass = n;
		this.type = en;
	}
	private Mob(String n, String ec, Enemies en){
		this.name = n;
		this.craftClass = n;
		this.entityClass = ec;
		this.type = en;
	}
	private Mob(String n, String ec, String cc, Enemies en){
		this.name = n;
		this.entityClass = ec;
		this.craftClass = cc;
		this.type = en;
	}
	
	public String s = "s";
	public String name;
	public Enemies type;
	private String entityClass;
	private String craftClass;
	
	@SuppressWarnings("unchecked")
	public CraftEntity spawn(org.bukkit.entity.Player player, SpawnMob plugin){
		try {
			WorldServer world = ((org.bukkit.craftbukkit.CraftWorld) player.getWorld()).getHandle();
			Constructor<CraftEntity> craft = (Constructor<CraftEntity>) ClassLoader.getSystemClassLoader().loadClass("org.bukkit.craftbukkit.entity.Craft" + craftClass).getConstructors()[0];
			Constructor<Entity> entity = (Constructor<Entity>) ClassLoader.getSystemClassLoader().loadClass("net.minecraft.server.Entity" + entityClass).getConstructors()[0];
			return craft.newInstance((CraftServer) plugin.getServer(), entity.newInstance( world ) );
		} catch (Exception e) {
			plugin.log.log(java.util.logging.Level.SEVERE,"Unable to spawn mob. Error: ");
			e.printStackTrace();
			return null;
		}
	}
	
	public enum Enemies{
		FRIENDLY("friendly"),
		NEUTRAL	("neutral"),
		ENEMY	("enemy");
		
		private Enemies(String t){
			this.type = t;
		}
		
		protected String type;
	}
}
