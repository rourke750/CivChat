package com.untamedears.civchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CivChat extends JavaPlugin implements Listener{
	ChatManager chat = new ChatManager(getConfig());
ChatListener cl= new ChatListener(chat);
FileConfiguration config_= getConfig();
	public void onEnable(){
		registerEvents();
		this.saveDefaultConfig();
		chat = new ChatManager(getConfig());
	    initConfig();
		Commands commands=new Commands(chat);;
		for (String command : getDescription().getCommands().keySet()) {
		getCommand(command).setExecutor(commands);
		}
	}
	public void onDisable(){
		
	}
	public void initConfig(){

		int chatrange=config_.getInt("chat.range", 1000);
		
	}
	
	public void saveConfig(){
		config_.options().copyDefaults(true);
	}
	public void ReloadConfig(){
	    this.reloadConfig();
	}
	
	private void registerEvents() {
	    getServer().getPluginManager().registerEvents(cl, this);
	    return;
	  }
	
	
	
}
