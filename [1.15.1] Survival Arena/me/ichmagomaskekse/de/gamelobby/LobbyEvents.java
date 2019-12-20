package me.ichmagomaskekse.de.gamelobby;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;

import me.ichmagomaskekse.de.SurvivalArena;

public class LobbyEvents implements Listener{
	
	public LobbyEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(this, SurvivalArena.getInstance());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
//		SurvivalArena.getInstance().lobby.joinPlayer(e.getPlayer());
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		e.getPlayer().teleport(SurvivalArena.getInstance().lobby.spawn_location);
		SurvivalArena.getInstance().lobby.leavePlayer(e.getPlayer());
	}
	@EventHandler
	public void onLectern(PlayerTakeLecternBookEvent e) {
		SurvivalArena.getInstance().lobby.joinPlayer(e.getPlayer());
		e.setCancelled(true);
		e.getPlayer().closeInventory();
	}
	
	
}
