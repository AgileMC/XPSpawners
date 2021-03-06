package com.github.soerxpso.xpspawners;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.soerxpso.xpspawners.manager.ConfigManager;

import vg.civcraft.mc.civmodcore.locations.QTBox;

public class Spawner implements QTBox, Comparable<Spawner> {
	private CreatureSpawner block;
	private int xpAmountPerHarvest;
	
	public Spawner(CreatureSpawner block) {
		this.block = block;
		xpAmountPerHarvest = (int) (ConfigManager.getBaseXpPerHour() / 60f / 60f / 20f 
				* ConfigManager.getHarvestInterval());
	}
	
	public Player findNearestPlayer() {
		Collection<Entity> nearbyEntities = 
				block.getWorld().getNearbyEntities(block.getLocation(), 33, 33, 33);
		Player nearest = null;
		double nearestDistance = Double.MAX_VALUE;
		for(Entity e : nearbyEntities) {
			if(e.getType() == EntityType.PLAYER) {
				double eDist = e.getLocation().distance(block.getLocation());
				if(eDist < nearestDistance) {
					nearest = (Player)e;
					nearestDistance = eDist;
				}
			}
		}
		return nearest;
	}
	
	public void giveXP(Player p) {
		block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation(), 1, 1, 1, 15);
		int oldLevel = p.getLevel();
		int xpToGive = xpAmountPerHarvest + (int)(Math.random() * 20 - 10);
		p.giveExp(xpToGive);
		// gotta do this check so the sounds don't sync up awkwardly
		if(!(oldLevel < p.getLevel() && p.getLevel() % 5 == 0)) {
			p.playSound(block.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1);
		}
		XPSpawners.getPlugin().getLogger().log(Level.INFO, "Gave " + xpToGive + " XP to " + p);
	}
	
	public Location getLocation() {
		return block.getLocation();
	}
	
	@Override
	public int qtXMax() {
		return getLocation().getBlockX() + 16;
	}

	@Override
	public int qtXMid() {
		return getLocation().getBlockX();
	}

	@Override
	public int qtXMin() {
		return getLocation().getBlockX() - 16;
	}

	@Override
	public int qtZMax() {
		return getLocation().getBlockZ() + 16;
	}

	@Override
	public int qtZMid() {
		return getLocation().getBlockZ();
	}

	@Override
	public int qtZMin() {
		return getLocation().getBlockZ() - 16;
	}

	@Override
	public int compareTo(Spawner o) {
		UUID thisWorld = getLocation().getWorld().getUID();
		UUID otherWorld = o.getLocation().getWorld().getUID();
		int worldCompare = thisWorld.compareTo(otherWorld);
		if (worldCompare != 0) {
			return worldCompare;
		}

		int thisX = getLocation().getBlockX();
		int thisY = getLocation().getBlockY();
		int thisZ = getLocation().getBlockZ();
		
		int otherX = o.getLocation().getBlockX();
		int otherY = o.getLocation().getBlockY();
		int otherZ = o.getLocation().getBlockZ();
		
		if (thisX < otherX) {
			return -1;
		}
		if (thisX > otherX) {
			return 1;
		}

		if (thisY < otherY) {
			return -1;
		}
		if (thisY > otherY) {
			return 1;
		}
		
		if (thisZ < otherZ) {
			return -1;
		}
		if (thisZ > otherZ) {
			return 1;
		}

		return 0;
	}
}