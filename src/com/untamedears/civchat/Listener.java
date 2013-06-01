package com.untamedears.civchat;


import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileManager.Location;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;


public class Listener {
	List<String> listeners= new ArrayList<String>();
	Locations ln= new Locations();
	channel ch= new channel();
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void PlayerChatEvent(AsyncPlayerChatEvent event){
		String message= event.getMessage();
		Player player=event.getPlayer();
		if (ch.getInChannel(player)==true){
			PrivateMessageHandler(message);
		}
		int X= player.getLocation().getBlockX();
		int Y= player.getLocation().getBlockY();
		int Z= player.getLocation().getBlockZ();
		for (Player name: event.getRecipients()){
			listeners.add(name.getName());
		}
		ln.setPlayerLocation(X,Y,Z);
		ln.SetPlayerlistners(listeners);
		
	}
	public void PrivateMessageHandler(String message){
		Player player2=Bukkit.getPlayerExact(ch.getPlayer2());
		player2.sendMessage(message);
	}
}
