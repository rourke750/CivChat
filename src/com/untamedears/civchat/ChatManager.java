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
import org.bukkit.Material;

/*
 * Coded by ibbignerd & Rourke750
 */
public class ChatManager {

    private CivChat plugin = null;
    private FileConfiguration config;
    private double chatmax;
    private boolean garbleEnabled;
    private double chatDist1;
    private int garble1;
    private double chatDist2;
    private int garble2;
    private int garblevar;
    private boolean greyscale;
    private String defaultcolor;
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
    private HashMap<String, Faction> groupchat = new HashMap<>();
    private HashMap<String, String> channels = new HashMap<>();
    private String replacement = "abcdefghijklmnopqrstuvwxyz";

    public ChatManager(CivChat pluginInstance) {
        plugin = pluginInstance;
        config = plugin.getConfig();
        chatmax = config.getDouble("chat.maxrange", 1000);
        garblevar = config.getInt("chat.garblevariation", 5);
        yvar = config.getBoolean("chat.yvariation.enabled", false);
        ynogarb = config.getInt("chat.yvariation.noGarbLevel", 70);
        shout = config.getBoolean("chat.shout.enabled", false);
        shoutChar = config.getString("chat.shout.char", "!");
        shoutDist = config.getInt("chat.shout.distanceAdded", 100);
        shoutHunger = config.getInt("chat.shout.hungerreduced", 1);
        whisper = config.getBoolean("chat.whisper.enabled", true);
        whisperChar = config.getString("chat.whisper.char", "#");
        whisperDist = config.getInt("chat.whisper.distance", 50);
        whisperColor = config.getString("chat.whisper.color", "ITALIC");
        defaultcolor = config.getString("chat.defaultcolor", "WHITE");
        greyscale = config.getBoolean("chat.greyscale", true);
        garbleEnabled = config.getBoolean("chat.range.garbleEnabled", false);
        chatDist1 = chatmax - config.getDouble("chat.range.1.distance", 100);
        garble1 = config.getInt("chat.range.1.garble", 0);
        color1 = config.getString("chat.range.1.color", "GRAY");
        chatDist2 = chatmax - config.getDouble("chat.range.2.distance", 50);
        garble2 = config.getInt("chat.range.2.garble", 0);
        color2 = config.getString("color.range.2.color", "DARK_GRAY");

    }

    public void sendPrivateMessage(Player from, Player to, String message) {
        from.sendMessage(ChatColor.DARK_AQUA + "To " + to.getDisplayName() + ": " + message);
        to.sendMessage(ChatColor.DARK_AQUA + "From " + from.getName() + ": " + message);
    }

    public void sendPlayerBroadcast(Player player, String message, Set<Player> receivers) {
        Location location = player.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        double chatdist = 0;
        boolean whispering = false;
        double chatrange = chatmax;
        double added = 0;

        if (yvar && !message.startsWith(whisperChar) && y > ynogarb) {
            added = Math.pow(1.1, (y - ynogarb) / 14) * (y - ynogarb);
            chatrange += added;
        }
        
        String shoutsub = message.subSequence(0, 3).toString();
        if (shout && message.startsWith(shoutChar)) {
            for (int i = 0; i < StringUtils.countMatches(shoutsub, "!"); i++) {
                
                chatrange += shoutDist;
            }
            player.sendMessage("number of !'s " + StringUtils.countMatches(shoutsub, "!"));
            player.sendMessage("Chat range: " + chatrange);
        }

        for (Player receiver : receivers) {
            double garble = 0;
            String chat = message;
            Random rand = new Random();
            double randGarble = 0;
            ChatColor color = ChatColor.valueOf(defaultcolor);

            int rx = receiver.getLocation().getBlockX();
            int ry = receiver.getLocation().getBlockY();
            int rz = receiver.getLocation().getBlockZ();

            chatdist = Math.sqrt(Math.pow(x - rx, 2) + Math.pow(y - ry, 2) + Math.pow(z - rz, 2));

            whispering = whisper && message.startsWith(whisperChar);
            if (whispering) {
                chatrange = whisperDist;
                color = ChatColor.valueOf(whisperColor);
                randGarble = 0;
            } else if (chatdist <= chatDist2 && chatdist > chatDist1) {
                player.sendMessage(receiver.getDisplayName() + "is within range1");
                if (garbleEnabled) {
                    randGarble = rand.nextInt(garblevar) + garble1;
                }
                if (greyscale) {
                    color = ChatColor.valueOf(color1);
                }
            } else if (chatdist <= chatrange && chatdist > chatDist2) {
                player.sendMessage(receiver.getDisplayName() + " is within range2");
                if (garbleEnabled) {
                    randGarble = rand.nextInt(garblevar) + garble2;
                }
                if (greyscale) {
                    color = ChatColor.valueOf(color2);
                }
            }
            if (garbleEnabled) {
                garble = chat.length() * (randGarble / 100);
                chat = shuffle(chat, garble);
            } else {
                chat = message;
            }
            if (chatdist <= chatrange) {
                if (whispering) {
                    receiver.sendMessage(color + player.getDisplayName() + " whispered: " + chat.substring(1));
                } else {
                    receiver.sendMessage(color + player.getDisplayName() + ": " + chat);
                }
            }
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

    public void GroupChat(Faction group, StringBuilder message, String player) {
        Player player1 = Bukkit.getPlayer(player);
        Collection<Player> players = Citadel.getMemberManager().getOnlinePlayers();
        String chat = message.toString();
        player1.sendMessage(ChatColor.DARK_AQUA + "To group" + group.getName() + ": " + chat);
        for (Player reciever : players) {
            if (group.isMember(reciever.getName())
                    && group.isFounder(reciever.getName())
                    && group.isModerator(reciever.getName())) {
                continue;
            } else {
                if (reciever.getName() == player1.getName()) {
                    continue;
                } else {
                    reciever.sendMessage(ChatColor.DARK_AQUA + "Group " + group.getName() + ", from " + player + ": " + chat);
                }
            }
        }

    }

    public void PrivateGroupChat(Faction group, String message, String player) {
        Player player1 = Bukkit.getPlayer(player);
        Collection<Player> players = Citadel.getMemberManager().getOnlinePlayers();
        String chat = message.toString();
        player1.sendMessage(ChatColor.DARK_AQUA + "To group" + group.getName() + ": " + chat);
        for (Player reciever : players) {
            if (!group.isMember(reciever.getName())
                    && !group.isFounder(reciever.getName())
                    && !group.isModerator(reciever.getName())) {
                continue;
            } else {
                if (reciever.getName() == player1.getName()) {
                    return;
                }
                reciever.sendMessage(ChatColor.DARK_AQUA + "Group " + group.getName() + ", from " + player + ": " + chat);
            }
        }
    }

    public void addGroupTalk(String player, Faction group) {
        if (getGroupTalk(player) != null) {
            removeGroupTalk(player);
            groupchat.put(player, group);
        } else {
            groupchat.put(player, group);
        }

    }

    public Faction getGroupTalk(String player) {
        if (groupchat.containsKey(player)) {
            return groupchat.get(player);
        } else {
            return null;
        }

    }

    public void removeGroupTalk(String player) {
        if (groupchat.containsKey(player)) {
            groupchat.remove(player);
        }
    }
}