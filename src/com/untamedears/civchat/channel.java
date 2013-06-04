package com.untamedears.civchat;

import org.bukkit.entity.Player;

public class Channel {
	private Player player1;
	private Player player2;
	
	public Channel (Player instance1, Player instance2)
	{
	player1 = instance1;
	player2 = instance2;
	}
	public Player getReciever(Player sender)
	{
	if (player1 == sender)
	{
	return player2;
	}
	else if (player2 == sender)
	{
	return player1;
	}
	return null;
	}
	

}
