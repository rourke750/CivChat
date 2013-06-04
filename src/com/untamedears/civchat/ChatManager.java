package com.untamedears.civchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class ChatManager {
	private HashMap<Player,Player> channels = new HashMap<Player,Player>();
	Configuration config_;
	public void PrivateMessageHandler(Player player1, String message){
		Player player2=Bukkit.getPlayerExact(ch.getPlayer2());
		player2.sendMessage(ChatColor.RED+"From "+player1.getName()+": "+message);
	}
	public void PlayerBroadcast(Player player, String message, Set<Player> recievers){
		Location location = player.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		double chatrange=0;
		for (Player reciever: recievers){
			
			double distance = Math.sqrt(((x-reciever.getLocation().getBlockX())^2 + (z-reciever.getLocation().getBlockZ())^2));
			double height=y-reciever.getLocation().getBlockY();
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
			reciever.sendMessage(ChatColor.RED+message);
		}
	}
	}
	 public void addChannel(Player player1, Player player2){
	 
	 if (getChannel(player1) != null){
	 
	 removeChannel(player1);
	 channels.put(player1, player2);
	 }
	 else{
	 
	 channels.put(player1, player2);
	 	}
	 }
	 public Player getChannel(Player player){	 
		 if (channels.containsKey(player)){	 
			 return channels.get(player);
		 }
		 else{
			 return null;
		 }
	 }
	 public void removeChannel (Player player){
	 
	 if (channels.containsKey(player))
	 {
	 channels.remove(player);
	 }
	 }

}
