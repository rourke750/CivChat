package com.untamedears.civchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
	channel ch= new channel();
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player playerreciever= Bukkit.getPlayerExact(args[0]);
		if (label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("message")){
			if (playerreciever.isOnline()==false){
				sender.sendMessage("Player is offline");
				return false;
			}
			sender.sendMessage("To player: "+ args);
			playerreciever.sendMessage("From "+sender.getName()+": "+ args[+1]);
			ch.setChannel(sender.getName(), playerreciever.getName(), true);
			return true;
		}
		return true;
	}
	
}
