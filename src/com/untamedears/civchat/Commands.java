package com.untamedears.civchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
	Channel ch;
	CivChat chat= new CivChat();
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
			sender.sendMessage(ChatColor.RED+"To "+playerreciever.getDisplayName()+": "+ argsmessage);
			playerreciever.sendMessage(ChatColor.RED+"From "+sender.getName()+": "+ argsmessage);
			ch.setChannel(sender.getName(), playerreciever.getName(), true);
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



