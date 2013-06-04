package com.untamedears.civchat;


import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileManager.Location;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;


public class ChatListener implements Listener{
	List<String> listeners= new ArrayList<String>();
	Locations ln= new Locations();
	channel ch= new channel();
	ChatManager chat= new ChatManager();
	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerChatEvent(AsyncPlayerChatEvent event){
		event.setCancelled(true);
		String message= event.getMessage();
		Player player=event.getPlayer();
		
		if (ch.getInChannel(player)==true){
			chat.PrivateMessageHandler(player, message); // Private Channel chat
		}
		
		int X= player.getLocation().getBlockX();
		int Y= player.getLocation().getBlockY();
		int Z= player.getLocation().getBlockZ();
		for (Player name: event.getRecipients()){
			listeners.add(name.getName());
		}
		ln.setPlayerLocation(X,Y,Z);
		ln.SetPlayerlistners(listeners);
		chat.PlayerBroadcast(message);
		
	}
	
}
