package com.untamedears.civchat;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ChatManager {
	private FileConfiguration config;
	private int chatmax = 1000;
	private HashMap<String, String> channels = new HashMap<String, String>();

	public ChatManager (FileConfiguration configInstance) {
		config = configInstance;
		config.getInt("chat.range", 1000);
	}

	public void sendPrivateMessage(Player from, Player to, String message) {
		from.sendMessage(ChatColor.DARK_PURPLE + "To " + to.getDisplayName() + ": " + message);
		to.sendMessage(ChatColor.DARK_PURPLE + "From " +from.getName() + ": " + message);
	}
	
	public void sendPlayerBroadcast(Player player, String message, Set<Player> receivers) {
		Location location = player.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		double chatrange = 0;
		
		boolean received = false;

		for (Player receiver : receivers) {
			
			int distX = x - receiver.getLocation().getBlockX();
			distX *= distX;
			int distZ = z - receiver.getLocation().getBlockZ();
			distZ *= distZ;
			
			long distance = Math.round(Math.sqrt((distX + distZ)));
			int height = y - receiver.getLocation().getBlockY();
			int extradistance = 0;
			int sign = 0;

			if (height < 0)
				sign = -1;
			if (height > 0)
				sign = 1;

			for (int i = 0; i <= Math.abs(height); i++) { 
				extradistance += 4;
			}
			
			if (sign == -1) {
				chatrange = distance + extradistance;
			}
			
			if (sign == 1) {
				chatrange = distance - extradistance;
			}
			
			if (chatrange <= chatmax) {
				receiver.sendMessage(player.getDisplayName() + ": " + message);
			}
			
			if(!received && !receiver.getName().equals(player.getName())) {
				received = true;
			}
			
			if(!received)
				player.sendMessage(ChatColor.YELLOW + "No one hears you.");
			// add when a a little bit above a certain range it removes letters
		}
	}
	public void addChannel(String player1, String player2) {
		if (getChannel(player1) != null) {
			removeChannel(player1);
			channels.put(player1, player2);
		}
		else {
			channels.put(player1, player2);
		}
	}
	
	public String getChannel(String player) {	 
		if (channels.containsKey(player)) {	 
			return channels.get(player);
		}
		else {
			return null;
		}
	}
	
	public void removeChannel(String player) {
		if (channels.containsKey(player)) {
			channels.remove(player);
		}
	}

}
