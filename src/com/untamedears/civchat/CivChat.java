package com.untamedears.civchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CivChat extends JavaPlugin implements Listener{
ChatListener cl= new ChatListener();
	public void onEnable(){
		registerEvents();
	    initConfig();
		Commands commands = new Commands();
		for (String command : getDescription().getCommands().keySet()) {
		getCommand(command).setExecutor(commands);
		}
	}
	public void onDisable(){
		
	}
	public void initConfig(){

		double chatrange=500;
		this.getConfig().set("chatrange",chatrange);
		if (this.getConfig()==null){

			this.saveConfig();
		}
		return;
	}
	
	public void saveConfig(){
		this.getConfig().options().copyDefaults(true);
	}
	public void ReloadConfig(){
	    this.reloadConfig();
	}
	
	private void registerEvents() {
	    getServer().getPluginManager().registerEvents(cl, this);
	    return;
	  }
	
	
	
}
