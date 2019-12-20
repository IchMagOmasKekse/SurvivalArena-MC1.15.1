package me.ichmagomaskekse.de.gamelobby;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.SurvivalArena;
import net.md_5.bungee.api.ChatColor;

public class GameLobby extends BukkitRunnable {
	
	public int id = 0;
	public int slots = 4;
	public int required_players = 1;
	public int min_level = 0;
	public int min_price = 0;
	private int default_countdown = 3;
	private int countdown = default_countdown;
	private int default_player_required_countdown = 30;
	private int player_required_countdown = 0;
	private int default_error_while_starting_countdown = 10;
	private int error_while_starting_countdown = default_error_while_starting_countdown;
	public boolean isStarting = false;
	public Random r = new Random();
	public Location arena_spawn = new Location(Bukkit.getWorld("world"), -9.5, 83, -179, (float)r.nextInt(360), 8);
	public Location spawn_location = new Location(Bukkit.getWorld("world"), -12, 65, -180);
	public ArrayList<Player> players = new ArrayList<Player>();
	
	public GameLobby() {
		this.runTaskTimer(SurvivalArena.getInstance(), 0, 20);
	}
	
	public boolean isFull() {
		if(players.size() == slots) return true;
		else return false;
	}
	
	public boolean joinPlayer(Player p, boolean sendMessage) {
		if(p == null) return false;
		if(isFull() == false && isAlreadyJoined(p) == false) {
			if(sendMessage)sendLobbyAlert("§f"+p.getName()+getPrimaryChatcolor()+" ist der Lobby beigetreten");
			players.add(p);
			if(sendMessage)sendLobbyMessage(p, "Du bist der Lobby §f"+id+getPrimaryChatcolor()+" beigetreten!"+getLobbySlotsAsString());
		}
		else return false;
		return true;
	}
	
	public boolean leavePlayer(Player p, boolean sendMessage) {
		if(p == null) return false;
		if(isAlreadyJoined(p)) {
			if(sendMessage)sendLobbyAlert("§f"+p.getName()+getPrimaryChatcolor()+" hat die Lobby verlassen");
			if(sendMessage)sendLobbyMessage(p, "Du hast die Lobby §f"+id+getPrimaryChatcolor()+" verlassen!"+getLobbySlotsAsString());
			players.remove(p);
		}
		else return false;
		return true;
	}
	public boolean joinPlayer(Player p) {
		return joinPlayer(p, true);
	}
	
	public boolean leavePlayer(Player p) {
		return leavePlayer(p, true);
	}
	
	public String getLobbySlotsAsString() {
		return "(§f"+players.size()+getPrimaryChatcolor()+"/§f"+slots+getPrimaryChatcolor()+")";
	}
	
	public boolean isAlreadyJoined(Player p) {
		return players.contains(p);
	}
	
	public GameLobby sendLobbyMessage(Player p, String...strings) {
		for(String s : strings) {
			p.sendMessage("  "+getPrimaryChatcolor()+"LOBBY §8> "+getPrimaryChatcolor()+""+s);
		}
		return this;
	}
	public GameLobby sendLobbyAlert(String...strings) {
		for(String s : strings) {
			for(Player p : players) p.sendMessage("  "+ getPrimaryChatcolor()+"LOBBY §8> "+ getPrimaryChatcolor()+""+s);
		}
		return this;
	}
	public GameLobby playLobbySound(Sound sound, Location location, float vol, float pitch) {
		for(Player p : players) p.playSound(location, sound, vol, pitch);
		return this;
	}
	public GameLobby playPlayerSound(Player p, Sound sound, Location location, float vol, float pitch) {
		p.playSound(location, sound, vol, pitch);
		return this;
	}
	
	public ChatColor getPrimaryChatcolor() {
		return ChatColor.YELLOW;
	}
	
	public void resetLobby() {
//		if(players.isEmpty() == false) for(Player p : players) leavePlayer(p, false);
		players.clear();
		error_while_starting_countdown = default_error_while_starting_countdown;
		error_while_starting_countdown = 0;player_required_countdown = 0;
		countdown = default_countdown;
		isStarting = false;
	}
	
	public GameLobby teleportPlayersToArena() {
		if(arena_spawn == null) return this;
		for(Player p : players) {
			p.teleport(arena_spawn);
			p.sendMessage("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n ");
		}
		sendLobbyAlert("§5VIEL ERFOLG !");
		return this;
	}

	@Override
	public void run() {
		if(isStarting == false) {			
			if(players.size() >= required_players)  {
				switch(countdown) {
				case 60: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f); break;
				case 45: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, arena_spawn, 5f, 0f);  break;
				case 30: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, arena_spawn, 5f, 0f);  break;
				case 15: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case 10: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  9: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  8: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  7: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  6: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  5: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  4: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  3: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  2: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  1: sendLobbyAlert("Spiel startet in §e"+countdown+getPrimaryChatcolor()+" Sekunden"); playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 0f);  break;
				case  0:
					sendLobbyAlert("Lade Spiel...");
					isStarting = true;
					playLobbySound(Sound.BLOCK_NOTE_BLOCK_BASS, arena_spawn, 5f, 6f);
					SurvivalArena.sessionManager.createNewSession(this);
					break;
				}
				countdown--;
			}else {
				countdown = default_countdown;
				if(player_required_countdown == 0) {
					sendLobbyAlert("Es fehlen §f"+(required_players-players.size())+getPrimaryChatcolor()+" Spieler zum Starten");
					player_required_countdown = default_player_required_countdown;
				}else player_required_countdown--;
			}
		}else {
			if(error_while_starting_countdown == 0 && players.isEmpty() == false) {
				sendLobbyAlert("§cFehler beim Starten des Spiels. Erneuter Versuch folgt...");
				error_while_starting_countdown = default_error_while_starting_countdown;
			}else error_while_starting_countdown--;
		}
	}
	
	
}
