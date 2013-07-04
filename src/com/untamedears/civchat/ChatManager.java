package com.untamedears.civchat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private List<String> temp;
    private CivChat plugin = null;
    private FileConfiguration config;
    public double chatMax;
    public boolean garbleEnabled;
    private double chatDist1;
    private int garble1;
    private double chatDist2;
    private int garble2;
    private int garblevar;
    public boolean greyscale;
    private String defaultColor;
    private String color1;
    private String color2;
    public boolean yvar;
    public int ynogarb;
    public boolean shout;
    public String shoutChar;
    public int shoutDist;
    public boolean whisper;
    public String whisperChar;
    public int whisperDist;
    private String whisperColor;
    private HashMap<String, Faction> groupchat = new HashMap<>();
    private HashMap<String, String> channels = new HashMap<>();
    private String replacement = "abcdefghijklmnopqrstuvwxyz";
    private HashMap<Player, Long> shoutList = new HashMap<>();
    public long shoutCool;
    public int shoutHunger;
    private String shoutColor;
    private HashMap<String, List<String>> ignoreList = new HashMap<>();
    private List<String> removeplayers;
    private List<String> receivers;
    public ChatManager(CivChat pluginInstance) {
        plugin = pluginInstance;
        config = plugin.getConfig();
        chatMax = config.getDouble("chat.maxrange", 1000);
        garblevar = config.getInt("chat.garblevariation", 5);
        yvar = config.getBoolean("chat.yvariation.enabled", true);
        ynogarb = config.getInt("chat.yvariation.noGarbLevel", 70);
        shout = config.getBoolean("chat.shout.enabled", true);
        shoutChar = config.getString("chat.shout.char", "!");
        shoutDist = config.getInt("chat.shout.distanceAdded", 100);
        shoutColor = config.getString("chat.shout.color", "WHITE");
        shoutHunger = config.getInt("chat.shout.hungerreduced", 4);
        shoutCool = config.getLong("chat.shout.cooldown", 10) * 1000;
        whisper = config.getBoolean("chat.whisper.enabled", true);
        whisperChar = config.getString("chat.whisper.char", "#");
        whisperDist = config.getInt("chat.whisper.distance", 50);
        whisperColor = config.getString("chat.whisper.color", "ITALIC");
        defaultColor = config.getString("chat.defaultcolor", "WHITE");
        greyscale = config.getBoolean("chat.greyscale", true);
        garbleEnabled = config.getBoolean("chat.range.garbleEnabled", false);
        chatDist1 = chatMax - config.getDouble("chat.range.1.distance", 100);
        garble1 = config.getInt("chat.range.1.garble", 0);
        color1 = config.getString("chat.range.1.color", "GRAY");
        chatDist2 = chatMax - config.getDouble("chat.range.2.distance", 50);
        garble2 = config.getInt("chat.range.2.garble", 0);
        color2 = config.getString("color.range.2.color", "DARK_GRAY");
        
        BroadcastType.NORMAL.setChatRange(chatMax);
        BroadcastType.WHISPER.setChatRange(whisperDist);
        BroadcastType.SHOUT.setChatRange(shoutDist);
        
        BroadcastType.NORMAL.setChatPrefix(defaultColor);
        BroadcastType.WHISPER.setChatPrefix(whisperColor);
        BroadcastType.SHOUT.setChatPrefix(shoutColor);
    }

    public void sendPrivateMessage(Player from, Player to, String message) {
        if (isIgnoring(to.getName(), from.getName())) {
            from.sendMessage(ChatColor.YELLOW + to.getName() + ChatColor.RED + " has muted you.");
            return;
        }
        from.sendMessage(ChatColor.LIGHT_PURPLE + "To " + to.getName() + ": " + message);
        to.sendMessage(ChatColor.LIGHT_PURPLE + "From " + from.getName() + ": " + message);
    }

    public void sendPlayerBroadcast(Player player, String message, Set<Player> receivers) {
        tL(player, "Broadcast", message);

        Location location = player.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        double chatdist = 0;
        double added = 0;
        
        BroadcastType bType;
        
        if (shout && message.startsWith(shoutChar)) {
        	bType = BroadcastType.SHOUT;
        }
        else if (whisper && message.startsWith(whisperChar)) {
        	bType = BroadcastType.WHISPER;
        }
        else {
        	bType = BroadcastType.NORMAL;
        }
        
        double chatrange = bType.getChatRange();

        if (yvar && !message.startsWith(whisperChar) && y > ynogarb) {
            added = Math.pow(1.1, (y - ynogarb) / 14) * (y - ynogarb);
            chatrange += added;
        }
        
        if(bType == BroadcastType.SHOUT) {
        	if (shoutList.get(player) != null && System.currentTimeMillis() - shoutList.get(player) >= shoutCool) {
                shoutList.remove(player);
            }

            if (shoutList.get(player) == null) {
                float saturation = player.getSaturation();
                int food = player.getFoodLevel();
                
                if(saturation + food > shoutHunger) {
                	saturation -= shoutHunger;
                	
                	if(saturation < 0) {
                		food += saturation;
                		saturation = 0;
                	}
                	
                	player.setFoodLevel(food);
                	player.setSaturation(saturation);
                	
                	shoutList.put(player, System.currentTimeMillis());
                }
                else {
                	player.sendMessage(ChatColor.RED + "You are too hungry to shout!");
                	return;
                }
                
            } else {
                player.sendMessage(ChatColor.RED + "Shout under cooldown, please wait "
                        + ((((shoutList.get(player) - System.currentTimeMillis()) + shoutCool) / 1000) + 1) + " seconds");
            }
        }

        for (Player receiver : receivers) {
            double garble = 0;
            String chat = message;
            Random rand = new Random();
            double randGarble = 0;
            String prefix = bType.getChatPrefix();

            int rx = receiver.getLocation().getBlockX();
            int ry = receiver.getLocation().getBlockY();
            int rz = receiver.getLocation().getBlockZ();

            chatdist = Math.sqrt(Math.pow(x - rx, 2) + Math.pow(y - ry, 2) + Math.pow(z - rz, 2));

            if(bType == BroadcastType.WHISPER) {
                randGarble = 0;
            } else if (chatdist <= chatDist2 && chatdist > chatDist1) {
                if (garbleEnabled) {
                    randGarble = rand.nextInt(garblevar) + garble1;
                }
                if (greyscale) {
                    prefix = ChatColor.valueOf(color1).toString();
                }
            } else if (chatdist <= chatrange && chatdist > chatDist2) {
                if (garbleEnabled) {
                    randGarble = rand.nextInt(garblevar) + garble2;
                }
                if (greyscale) {
                    prefix = ChatColor.valueOf(color2).toString();
                }
            }
            if (garbleEnabled) {
                garble = chat.length() * (randGarble / 100);
                chat = shuffle(chat, garble);
            } else {
                chat = message;
            }
            if (chatdist <= chatrange) {
                if (bType == BroadcastType.WHISPER) {
                    receiver.sendMessage(prefix + player.getDisplayName() + " whispered: " + chat.substring(1));
                } else if (bType == BroadcastType.SHOUT) {
                    prefix = ChatColor.valueOf(shoutColor).toString();
                    receiver.sendMessage(prefix + player.getDisplayName() + " shouted: " + chat.substring(1));
                } else {
                    receiver.sendMessage(prefix + player.getDisplayName() + ": " + chat);
                }
            }
        }
    }

    private String shuffle(String input, double a) {
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
        player1.sendMessage(ChatColor.DARK_AQUA + "To group " + group.getName() + ": " + chat);
        for (Player reciever : players) {
            if (!group.isMember(reciever.getName())
                    && !group.isFounder(reciever.getName())
                    && !group.isModerator(reciever.getName())) {
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
        player1.sendMessage(ChatColor.DARK_AQUA + "To group: " + group.getName() + ": " + chat);
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

    public boolean isGroupTalk(String player) {
        if (groupchat.containsKey(player)) {
            return true;
        }
        return false;
    }

    public void removeGroupTalk(String player) {
        if (groupchat.containsKey(player)) {
            groupchat.remove(player);
        }
    }

    public void tL(Player sender, String type, String message) {
        String date = new SimpleDateFormat("dd-MM HH:mm:ss").format(new Date());
        String name = sender.getName();
        String loc = (int) sender.getLocation().getX() + ", " + (int) sender.getLocation().getY() + ", " + (int) sender.getLocation().getZ();
        String textLine = "[" + date + "] [" + loc + "] [" + type + "] [" + name + "] " + message;

        try {
            plugin.writer.write(textLine);
            plugin.writer.newLine();
            plugin.writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(CivChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String playerCheck(String player) {
        Player[] onlineList = Bukkit.getOnlinePlayers();
        for (Player check : onlineList) {
            if (check.getName().startsWith(player)) {
                return check.getName();
            }
        }
        return player;
    }

    public boolean isIgnoring(String muter, String muted) {
        try {
            if (ignoreList.containsKey(muter)) {
                temp = ignoreList.get(muter);
                Logger.getLogger(CivChat.class.getName()).log(Level.SEVERE, temp.toString(), "");
                if (temp.contains(muted)) {
                    Bukkit.getPlayer(muted).sendMessage(ChatColor.RED + Bukkit.getPlayer(muter).getName() + " has muted you.");
                    return true;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }
    public void setIgnoreList(String player, String reciever){
    	if(ignoreList.size()>=1){
    	receivers= ignoreList.get(player);
    	ignoreList.clear();
    	receivers.add(reciever);
    	ignoreList.put(player, receivers);
    	}
    	else {
    		 receivers.add(reciever);
    		ignoreList.put(player, receivers);
    	}
    	
    }
    public List<String> getIgnoreList(String player){
    	List<String> reciever;
    	reciever=ignoreList.get(player);
    	return reciever;
    }
    public void removeIgnore(String player, String reciever){
    	
    	for (String x: ignoreList.get(player)){
    		if (x!=reciever){
    			continue;
    		}
    		else{removeplayers.add(x);}
    	}
    	ignoreList.remove(player);
    	ignoreList.put(player, removeplayers);
    }
}