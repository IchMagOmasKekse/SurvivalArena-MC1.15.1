package me.ichmagomaskekse.de;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.game.SessionManager;
import me.ichmagomaskekse.de.gamelobby.GameLobby;
import me.ichmagomaskekse.de.gamelobby.LobbyEvents;

public class SurvivalArena extends JavaPlugin {
	
	private static SurvivalArena pl;
	public static SurvivalArena getInstance() { return pl; }
	
	public static SessionManager sessionManager = null;
	
	public GameLobby lobby = null;
	
	public SurvivalArena() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEnable() {
		pl = this;
		sessionManager = new SessionManager();
		new LobbyEvents();
		lobby = new GameLobby();
		for(Player p : Bukkit.getOnlinePlayers()) {
//			lobby.joinPlayer(p);
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			lobby.leavePlayer(p);
		}
		sessionManager.cancelAll();
		super.onDisable();
	}
	
}
