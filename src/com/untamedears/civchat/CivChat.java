package com.untamedears.civchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Coded by Rourke750 & ibbignerd
 */
public class CivChat extends JavaPlugin implements Listener {

    private ChatManager chat = null;
    private ChatListener cl = null;
    private FileConfiguration config = null;

    public void onEnable() {
        config = getConfig();
        initConfig();
        this.saveConfig();
        chat = new ChatManager(this);
        cl = new ChatListener(chat);
        registerEvents();
        Commands commands = new Commands(chat, this);

        for (String command : getDescription().getCommands().keySet()) {
            getCommand(command).setExecutor(commands);
        }
    }

    public void initConfig() {
        config.options().header(" Authors: Rourke750, ibbignerd\n Last updated on 06/15/13\n This plugin was designed for use by Civcraft\n\n garblevariation: the range added to the garble to make it more random\n defaultcolor: Default color of chat. Must use a ChatColor.COLOR option\n greyscale: as the reciever is farther away, the chat changes colors\n yvariation: The higher you are, the farther you can talk.\n     maxrange += yvariation amount * y of sender;\n yvariation amount: Multiplier amount of above\n range distance: amount subtracted from maxrange\n\n Below 0 range is no garble\n range garble: amount as a whole percent that the text should be garbled\n range color: if greyscale is true, color of chat based on range\n maxrange: distance a player can be heard");
        if (!config.contains("chat.garblevariation")) {
            config.set("chat.variation", 5);
        }
        if (!config.contains("chat.defaultcolor")) {
            config.set("chat.defaultcolor", "WHITE");
        }
        if (!config.contains("chat.greyscale")) {
            config.set("chat.greyscale", false);
        }
        if (!config.contains("chat.yvariation.enabled")) {
            config.set("chat.yvariation.enabled", true);
        }
        if (!config.contains("chat.yvariation.noGarbLevel")) {
            config.set("chat.yvariation.noGarbLevel", 70);
        }
        if (!config.contains("chat.shout.enabled")) {
            config.set("chat.shout.enabled", true);
        }
        if (!config.contains("chat.shout.char")) {
            config.set("chat.shout.char", "!");
        }
        if (!config.contains("chat.shout.distanceAdded")) {
            config.set("chat.shout.distanceAdded", 100);
        }
        if (!config.contains("chat.shout.hungerreduced")) {
            config.set("chat.shout.hungerreduced", 2);
        }
        if (!config.contains("chat.whisper.enabled")) {
            config.set("chat.whisper.enabled", true);
        }
        if (!config.contains("chat.whisper.char")) {
            config.set("chat.whisper.char", "#");
        }
        if (!config.contains("chat.whisper.distance")) {
            config.set("chat.whisper.distance", 50);
        }
        if (!config.contains("chat.whisper.color")) {
            config.set("chat.whisper.color", "WHITE");
        }
        if (!config.contains("chat.range.0.distance")) {
            config.set("chat.range.0.distance", 500);
        }
        if (!config.contains("chat.range.0.garble")) {
            config.set("chat.range.0.garble", 5);
        }
        if (!config.contains("chat.range.0.color")) {
            config.set("chat.range.0.color", "GRAY");
        }
        if (!config.contains("chat.range.1.distance")) {
            config.set("chat.range.1.distance", 350);
        }
        if (!config.contains("chat.range.1.garble")) {
            config.set("chat.range.1.garble", 15);
        }
        if (!config.contains("chat.range.1.color")) {
            config.set("chat.range.1.color", "GRAY");
        }
        if (!config.contains("chat.range.2.distance")) {
            config.set("chat.range.2.distance", 200);
        }
        if (!config.contains("chat.range.2.garble")) {
            config.set("chat.range.2.garble", 35);
        }
        if (!config.contains("chat.range.2.color")) {
            config.set("chat.range.2.color", "DARK_GRAY");
        }
        if (!config.contains("chat.range.maxrange")) {
            config.set("chat.range.maxrange", 1000);
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(cl, this);
    }
}
