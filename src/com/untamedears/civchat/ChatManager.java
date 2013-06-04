package com.untamedears.civchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class ChatManager {
	channel ch= new channel();
	Locations lo= new Locations();
	Configuration config_;
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
				else if (chatrange<=500){
			player2.sendMessage(ChatColor.RED+message);
		}
	}
	}

}
