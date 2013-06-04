package com.untamedears.civchat;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.tools.JavaFileManager.Location;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;


public class ChatListener implements Listener{
	
	ChatManager chat;
	public ChatListener(ChatManager instance){
		chat=instance;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerChatEvent(AsyncPlayerChatEvent event){
		event.setCancelled(true);
		String message= event.getMessage();
		Player player=event.getPlayer();
		
		if (chat.getChannel(player)!=null){
			chat.PrivateMessageHandler(player, chat.getChannel(player), message);
			return;
		}
		else{
			chat.PlayerBroadcast(player, message, event.getRecipients());
		}
		
	}
	
}
