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
	channel ch= new channel();
	Locations lo= new Locations();
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
	
	public void PrivateMessageHandler(Player player1, String message){
		Player player2=Bukkit.getPlayerExact(ch.getPlayer2());
		player2.sendMessage(ChatColor.RED+"From "+player1.getName()+": "+message);
	}
	public void PlayerBroadcast(String message){
		
		int x= lo.getPlayerXLocation();
		int y= lo.getPlayerYLocation();
		int z= lo.getPlayerZLocation();
		double chatrange=0;
		for (String player: lo.getPlayerListeners()){
			Player player2 = Bukkit.getPlayerExact(player);
			double distance = Math.sqrt(((x-player2.getLocation().getBlockX())^2 + (z-player2.getLocation().getBlockZ())^2));
			double height=y-player2.getLocation().getBlockY();
			int extradistance=0;
			int sign=0;
			if (height<0){sign=-1;}
			if (height>0){sign=+1;}
			
				for (int i=0; i<=Math.abs(height);i++){
					extradistance +=50;
				}
				if (sign==-1){
				chatrange= distance-extradistance;
			}
				if (sign==+1){
				chatrange=distance+extradistance;
				}
				else if (chatrange<=this.getConfig().getDouble("chatrange",chatrange)){
			player2.sendMessage(ChatColor.RED+message);
		}
	}
	}
	
}
