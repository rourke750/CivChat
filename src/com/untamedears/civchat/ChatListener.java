package com.untamedears.civchat;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {
	private ChatManager chat;
	
	public ChatListener(ChatManager instance) {
		chat = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerChatEvent(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		
		String message = event.getMessage();
		Player player = event.getPlayer();
		String channel = chat.getChannel(player.getName());
		
		if(channel != null) {
			Player to = Bukkit.getPlayerExact(channel);
			
			if(to != null) {
				chat.sendPrivateMessage(player, to, message);
				return;
			}
			else {
				chat.removeChannel(player.getName());
				player.sendMessage(ChatColor.GOLD + "The player you were chatting with has gone offline. You are now in regular chat.");
			}
		}
		
		chat.sendPlayerBroadcast(player, message, event.getRecipients());
	}
}
