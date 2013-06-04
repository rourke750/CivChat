package com.untamedears.civchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
	
	ChatManager chatManager;
	public Commands (ChatManager chatManagerInstance){
	chatManager = chatManagerInstance;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
             
		if (label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("message")){
			
			if (args.length < 1) {
                sender.sendMessage("Provide a player name");
                return true;
        }
			if (args.length==1){
				
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
				argsmessage.toString();
				chatManager.PrivateMessageHandler(sender.getName(), playerreciever, argsmessage);
			
			return true;
				}
			}
			return true;
		}
		if (label.equalsIgnoreCase("civchat")){
			if (args[0]=="save"){
				sender.sendMessage("saved config");
				chat.saveConfig();
				return true;
			}
			if (args[0]=="reload"){
				sender.sendMessage("reloaded config");
				chat.ReloadConfig();
				return true;
			}
			else{ sender.sendMessage("Incorrect arg");}
			return true;
		}
		return true;
	}
	
}



