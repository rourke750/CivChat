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
        config.options().header("Authors: Rourke750, ibbignerd" +
            	"\n Last updated on 06/20/13" +
        		"\nThis plugin was designed for use by Civcraft" +
        		"\n \n " +
        		"variation: The range from 0-value added to the garble to make it more random" +
        		"\n defaultcolor: Default color of chat. Must use a ChatColor.COLOR option" +
        		"\n greyscale: Chat changes for reciever based on distance. Configured below" +
        		"\n yvariation: The higher the sender is, the farther the max chat range." +
        		"\n   noGarbLevel: At what y level (and above) will the yvariation come into effect?" +
        		"\n shout: Expand max range based on number of shout chars" +
        		"\n   char: character uesd at the beginning of the message" +
        		"\n   distanceAdded: Amount added to the max range to make chat go further" +
        		"\n   hungerreduced: Amount of hunger reduced per shout char." +
        		"\n whisper: Reduced chat range when whisper char is used" +
        		"\n   char: Character used at the beginning of the message" +
        		"\n   distance: set distance a whisper is heard" +
        		"\n   color: configurable color for whisper messages" +
        		"\n range: currently has 3 configurable distances" +
        		"\n   distance: subtraced from maxrange (e.g. maxrange = 1000, " +
        		"\n     distance = 100, from 900-1000 is the range)" +
        		"\n   garble: Integer as a percent. (garble: 5 = 5% of string will be garbled)" +
        		"\n   color: Color of chat for this range" +
        		"\n maxrange: Max distance a player can be heard");
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
