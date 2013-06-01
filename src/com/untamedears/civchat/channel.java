package com.untamedears.civchat;

import org.bukkit.entity.Player;

public class channel {
	private Boolean inchannel;
	private String player1;
	private String player2;
	
	public void setChannel(String one, String two, Boolean channel){
		player1= one;
		player2= two;
		inchannel=channel;
	}
	public Boolean getInChannel(Player player){
		if(player.getName()==player1){
			return inchannel;
		}
		else{
		return false;
	}
	}
	public String getPlayer2(){
		return player2;
	}
}
