package com.untamedears.civchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
	CivChat civ;
	ChatManager chatManager;
	public Commands (ChatManager chatManagerInstance){
	chatManager = chatManagerInstance;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
             
		if (label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("message")){
			if (!(sender instanceof Player))
			{
			sender.sendMessage("You have to be a player to use that command!");
			}
			Player player = (Player) sender;
			
			if (args.length < 1) {
				if (chatManager.getChannel(player)==null){
					player.sendMessage("Provide a player name");
				}
				else{
				chatManager.removeChannel(player);
					player.sendMessage("In Global Channel");
				}
                return true;
        }
			else if(args.length == 1){
				Player playerreciever= Bukkit.getPlayerExact(args[0]);
				if (playerreciever==null){
					player.sendMessage("Player is offline");
					return true;
			}
				else{
					chatManager.addChannel(player, playerreciever);
					player.sendMessage("In Channel with "+ playerreciever.getDisplayName());
					return true;
					}
			}
			
			if (args.length>1){
				Player playerreciever= Bukkit.getPlayerExact(args[0]);
				
				if (playerreciever==null){
					sender.sendMessage("Player is offline");
					return true;
				}
				else{
				StringBuilder argsmessage= new StringBuilder();
				for (int i= 1;i<args.length;i++){
					argsmessage.append(args[i]);
					argsmessage.append(" ");
				}
				
				
				chatManager.PrivateMessageHandler(player, playerreciever, argsmessage.toString());
			
			return true;
				}
			}
			return true;
		}
		if (label.equalsIgnoreCase("civchat")){
			if (args[0]=="save"){
				sender.sendMessage("saved config");
				civ.saveConfig();
				return true;
			}
			if (args[0]=="reload"){
				sender.sendMessage("reloaded config");
				civ.ReloadConfig();
				return true;
			}
			else{ sender.sendMessage("Incorrect arg");}
			return true;
		}
		return true;
	}
	
}



