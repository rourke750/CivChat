package com.untamedears.civchat;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.untamedears.citadel.Citadel;
import com.untamedears.citadel.entity.Faction;

/*
 * Coded by ibbignerd and Rourke750
 */
public class ChatManager {

    private CivChat plugin = null;
    private FileConfiguration config;
    private double chatmax;
    private double chatDist0;
    private int garble0;
    private double chatDist1;
    private int garble1;
    private double chatDist2;
    private int garble2;
    private int garblevar;
    private boolean greyscale;
    private String defaultcolor;
    private String color0;
    private String color1;
    private String color2;
    private boolean yvar;
    private int ynogarb;
    private boolean shout;
    private String shoutChar;
    private int shoutDist;
    private int shoutHunger;
    private boolean whisper;
    private String whisperChar;
    private int whisperDist;
    private String whisperColor;
    private HashMap<String, Faction> groupchat= new HashMap<>();
    private HashMap<String, String> channels = new HashMap<>();
    private String replacement = "abcdefghijklmnopqrstuvwxyz";

    public ChatManager(CivChat pluginInstance) {
        plugin = pluginInstance;
        config = plugin.getConfig();
        chatmax = config.getDouble("chat.maxrange", 1000);
        garblevar = config.getInt("chat.garblevariation", 5);
        yvar = config.getBoolean("chat.yvariation", true);
        ynogarb = config.getInt("chat.yvariation", 70);
        shout = config.getBoolean("chat.shout.enabled", true);
        shoutChar = config.getString("chat.shout.char", "!");
        shoutDist = config.getInt("chat.shout.distanceAdded", 100);
        shoutHunger = config.getInt("chat.shout.hungerreduced", 2);
        whisper = config.getBoolean("chat.whisper.enabled", true);
        whisperChar = config.getString("chat.whisper.char", "#");
        whisperDist = config.getInt("chat.whisper.distance", 50);
        whisperColor = config.getString("chat.whisper.color", "WHITE");
        defaultcolor = config.getString("chat.defaultcolor", "WHITE");
        greyscale = config.getBoolean("chat.greyscale", false);
        chatDist0 = chatmax - config.getDouble("chat.range.0.distance", 500);
        garble0 = config.getInt("chat.range.0.garble", 5);
        color0 = config.getString("chat.range.0.color", "GRAY");
        chatDist1 = chatmax - config.getDouble("chat.range.1.distance", 350);
        garble1 = config.getInt("chat.range.1.garble", 15);
        color1 = config.getString("chat.range.1.color", "GRAY");
        chatDist2 = chatmax - config.getDouble("chat.range.2.distance", 200);
        garble2 = config.getInt("chat.range.2.garble", 35);
        color2 = config.getString("color.range.1.color", "DARK_GRAY");

    }

    public void sendPrivateMessage(Player from, Player to, String message) {
        from.sendMessage(ChatColor.DARK_AQUA+ "To " + to.getDisplayName() + ": " + message);
        to.sendMessage(ChatColor.DARK_AQUA+ "From " + from.getName() + ": " + message);
    }

    public void sendPlayerBroadcast(Player player, String message, Set<Player> receivers) {
        Location location = player.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        double chatrange = 0;
        boolean whispering = false;


        if (yvar && !message.startsWith(whisperChar)) {
            if (y > ynogarb) {
                chatmax += Math.pow(1.1, (y - ynogarb) / 8.6) * y;
            }
        }
        String shoutsub = message.subSequence(0, 2).toString();
        if (shout && message.startsWith(shoutChar)) {
            for (int i = 0; i <= StringUtils.countMatches(shoutsub, "!"); i++) {
                player.setFoodLevel(player.getFoodLevel() - shoutHunger);
                chatmax += shoutDist;
            }

        }
        if (whisper && message.startsWith(whisperChar)) {
            chatmax = whisperDist;
            whispering = true;
        }
        for (Player receiver : receivers) {
            double garble = 0;
            String chat = message;
            Random rand = new Random();
            double randGarble = 1;
            ChatColor color = ChatColor.valueOf(defaultcolor);

            int rx = receiver.getLocation().getBlockX();
            int ry = receiver.getLocation().getBlockY();
            int rz = receiver.getLocation().getBlockZ();

            chatrange = Math.sqrt(Math.pow(x - rx, 2) + Math.pow(y - ry, 2) + Math.pow(z - rz, 2));

            if (whispering && chatrange >= whisperDist) {
                color = ChatColor.valueOf(whisperColor);
                randGarble = 0;
            } else if (chatrange <= chatDist0) {
                randGarble = 0;
            } else if (chatrange <= chatDist1 && chatrange > chatDist0) {
                randGarble = rand.nextInt(garblevar) + garble0;
                if (greyscale) {
                    color = ChatColor.valueOf(color0);
                }
            } else if (chatrange <= chatDist2 && chatrange > chatDist1) {
                randGarble = rand.nextInt(garblevar) + garble1;
                if (greyscale) {
                    color = ChatColor.valueOf(color1);
                }
            } else if (chatrange <= chatmax && chatrange > chatDist2) {
                randGarble = rand.nextInt(garblevar) + garble2;
                if (greyscale) {
                    color = ChatColor.valueOf(color2);
                }
            }
            garble = chat.length() * (randGarble / 100);
            chat = shuffle(chat, garble);
            receiver.sendMessage(color + player.getDisplayName() + ": " + chat);
        }
    }

    public String shuffle(String input, double a) {
        int times = (int) a;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(input);
        for (int i = 0; i < times; i++) {
            int rand = random.nextInt(input.length());
            int replaceRand = random.nextInt(replacement.length());
            switch (sb.charAt(rand)) {
                case ' ':
                case '.':
                case ',':
                case '!':
                    continue;
            }
            sb.setCharAt(rand, replacement.charAt(replaceRand));
        }
        String output = new String(sb);
        return output;
    }

    public void addChannel(String player1, String player2) {
        if (getChannel(player1) != null) {
            removeChannel(player1);
            channels.put(player1, player2);
        } else {
            channels.put(player1, player2);
        }
    }

    public String getChannel(String player) {
        if (channels.containsKey(player)) {
            return channels.get(player);
        } else {
            return null;
        }
    }

    public void removeChannel(String player) {
        if (channels.containsKey(player)) {
            channels.remove(player);
        }
    }
    public void GroupChat(Faction group, StringBuilder message, String player){
        Player player1= Bukkit.getPlayer(player);
    	Collection<Player> players=Citadel.getMemberManager().getOnlinePlayers();
    	String chat=message.toString();
    	for (Player reciever: players){
    		if (group.isMember(reciever.getName())
    			&& group.isFounder(reciever.getName())
    			&& group.isModerator(reciever.getName())){
    			continue;
    		}
    		player1.sendMessage(ChatColor.GOLD+"To group"+group+": "+chat);
    		reciever.sendMessage(ChatColor.GOLD+"Group "+group+", from "+player+": "+chat);
    	}
    	
    }
    public void PrivateGroupChat(Faction group, String message, String player){
    	Player player1= Bukkit.getPlayer(player);
    	Collection<Player> players=Citadel.getMemberManager().getOnlinePlayers();
    	String chat=message.toString();
    	for (Player reciever: players){
    		if (!group.isMember(reciever.getName())
    			&& !group.isFounder(reciever.getName())
    			&& !group.isModerator(reciever.getName())){
    			continue;
    		}
    		else{
    		player1.sendMessage(ChatColor.DARK_AQUA+"To group"+group+": "+chat);
    		reciever.sendMessage(ChatColor.DARK_AQUA+"Group "+group+", from "+player+": "+chat);
    		}
    		}
    }
    public void addGroupTalk(String player, Faction group){
    	if (getGroupTalk(player) != null) {
            removeGroupTalk(player);
            groupchat.put(player, group);
        } else {
            groupchat.put(player, group);
        }
    	
    }
    public Faction getGroupTalk(String player){
    	 if (groupchat.containsKey(player)) {
             return groupchat.get(player);
         } else {
             return null;
         }
    	
    }
    public void removeGroupTalk(String player){
    	if (groupchat.containsKey(player)) {
            groupchat.remove(player);
        }
    }
}
