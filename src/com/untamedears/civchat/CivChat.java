package com.untamedears.civchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CivChat extends JavaPlugin implements Listener {
	private ChatManager chat = new ChatManager(getConfig());
	private ChatListener cl = new ChatListener(chat);
	private FileConfiguration config = getConfig();
	
	public void onEnable() {
		registerEvents();
		this.saveDefaultConfig();
		chat = new ChatManager(getConfig());
		initConfig();
		Commands commands=new Commands(chat);
		
		for (String command : getDescription().getCommands().keySet()) {
			getCommand(command).setExecutor(commands);
		}
	}
	
	public void onDisable() {

	}
	
	public void initConfig() {
		if(!config.contains("chat.range"))
			config.set("chat.range", 1000);
	}

	public void saveConfig() {
		config.options().copyDefaults(true);
	}
	
	public void reloadConfig() {
		this.reloadConfig();
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(cl, this);
		return;
	}
}
