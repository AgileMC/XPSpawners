package com.github.soerxpso.xpspawners;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.soerxpso.xpspawners.listeners.BlockListener;
import com.github.soerxpso.xpspawners.manager.SpawnerManager;

public class XPSpawners extends JavaPlugin {

	private static XPSpawners plugin;
	private SpawnerManager spawnerManager;
	
	public void onEnable() {
		plugin = this;
		spawnerManager = new SpawnerManager();
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
	}
	
	public static XPSpawners getPlugin() {
		return plugin;
	}
	
	public SpawnerManager getSpawnerManager() {
		return spawnerManager;
	}
}
