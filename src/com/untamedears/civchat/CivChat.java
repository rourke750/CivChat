package com.untamedears.civchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CivChat extends JavaPlugin implements Listener {
	private ChatManager chat = null;
	private ChatListener cl = null;
	private FileConfiguration config = null;
	
	public void onEnable() {
	    config = getConfig();
		initConfig();
		this.saveConfig();
	    chat = new ChatManager(this);
	    cl = new ChatListener(chat);
		registerEvents();
		Commands commands=new Commands(chat);
		
		for (String command : getDescription().getCommands().keySet()) {
			getCommand(command).setExecutor(commands);
		}
	}
	
	public void initConfig() {
		if(!config.contains("chat.range"))
			config.set("chat.range", 1000);
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(cl, this);
		return;
	}
}
