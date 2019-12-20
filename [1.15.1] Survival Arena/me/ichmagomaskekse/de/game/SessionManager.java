package me.ichmagomaskekse.de.game;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.SurvivalArena;
import me.ichmagomaskekse.de.gamelobby.GameLobby;

public class SessionManager extends BukkitRunnable {
	
	public ConcurrentHashMap<GameSession, GameSession> sessions = new ConcurrentHashMap<GameSession, GameSession>();
	
	public SessionManager() {
		this.runTaskTimer(SurvivalArena.getInstance(), 0, 1);
	}
	
	public void createNewSession(GameLobby lobby) {
		GameSession session = new GameSession();
		session.setLobby(lobby);
		session.transmitPlayers(lobby.players);
		lobby.teleportPlayersToArena();
		lobby.resetLobby();
		sessions.put(session, session);
	}
	
	public void cancelAll() {
		for(GameSession s : sessions.keySet()) s.stopBecausNoPlayers();
	}
	
	
	private int max_ticks = 20;
	private int tick = 0;
	@Override
	public void run() {
		if(tick == max_ticks) {
			for(GameSession s : sessions.keySet()) s.tick();
			tick = 0;
		}else tick++;
		for(GameSession s : sessions.keySet()) s.wave.setTargets();		
	}
	
}
