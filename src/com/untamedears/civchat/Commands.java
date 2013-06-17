package com.untamedears.civchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Coded by Rourke750 & ibbignerd
 */

public class Commands implements CommandExecutor {

    private CivChat civ;
    private ChatManager chatManager;

    public Commands(ChatManager chatManagerInstance, CivChat instance) {
        chatManager = chatManagerInstance;
        civ = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tell")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You have to be a player to use that command!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length < 1) {
                if (chatManager.getChannel(player.getName()) == null) {
                    player.sendMessage(ChatColor.RED + "Usage: /tell <player>");
                } else {
                    chatManager.removeChannel(player.getName());
                    player.sendMessage(ChatColor.YELLOW + "You have moved to regular chat.");
                }
                return true;
            } else if (args.length == 1) {
                Player receiver = Bukkit.getPlayerExact(args[0]);
                if (receiver == null) {
                    player.sendMessage(ChatColor.RED + "Error: Player is offline.");
                    return true;
                } else {
                    chatManager.addChannel(player.getName(), receiver.getName());
                    player.sendMessage(ChatColor.YELLOW + "You are now chatting with " + receiver.getDisplayName() + ".");
                    return true;
                }
            }

            if (args.length > 1) {
                Player receiver = Bukkit.getPlayerExact(args[0]);

                if (receiver == null) {
                    sender.sendMessage(ChatColor.RED + "Error: Player is offline.");

                    return true;
                } else {
                    StringBuilder message = new StringBuilder();

                    for (int i = 1; i < args.length; i++) {
                        message.append(args[i]);

                        if (i < args.length - 1) {
                            message.append(" ");
                        }
                    }
                    chatManager.sendPrivateMessage(player, receiver, message.toString());
                    return true;
                }
            }
            return true;
        }
        if (label.equalsIgnoreCase("exit")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You have to be a player to use that command!");
                return true;
            }

            Player player = (Player) sender;
            chatManager.removeChannel(player.getName());
            player.sendMessage(ChatColor.YELLOW + "You have moved to regular chat.");
            return true;
        }

        if (label.equalsIgnoreCase("civchat")) {
            if (sender.hasPermission("civchat.admin")) {
                if (args.length < 1) {
                    sender.sendMessage("Usage: /civchat <save/reload>");
                    return true;
                }

                if (args[0].equalsIgnoreCase("save")) {
                    sender.sendMessage("Saved configuration.");
                    civ.saveConfig();

                    return true;
                }
                if (args[0].equals("reload")) {
                    sender.sendMessage("Reloaded configuration.");
                    civ.reloadConfig();

                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You Do not have Permissions civchat.admin");
            }
        }
        return true;
    }
}
