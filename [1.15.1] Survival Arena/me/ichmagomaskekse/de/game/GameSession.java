package me.ichmagomaskekse.de.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.SurvivalArena;
import me.ichmagomaskekse.de.gamelobby.GameLobby;

public class GameSession implements Listener  {
	
	public int id = 0;
	public Random random = new Random();
	public GameLobby lobby = null;
	public int max_rounds = 5;
	public Location spawn_location = new Location(Bukkit.getWorld("world"), -12, 65, -180);
	public Wave wave = null;
	public SessionRules rules = new SessionRules();

	public HashMap<Player, PlayerProfile> profiles = new HashMap<Player, PlayerProfile>();
	public ConcurrentHashMap<Player, Integer> players = new ConcurrentHashMap<Player, Integer>();
	
	public GameSession() {
		this.id = random.nextInt(100);
		Bukkit.getServer().getPluginManager().registerEvents(this, SurvivalArena.getInstance());
		nextRound();
	}
	
	public boolean transmitPlayers(ArrayList<Player> p_list) {
		for(Player p : p_list) {
			if(p != null && p.isOnline()) {
				players.put(p, 0);
				profiles.put(p, loadProfile(p));
			}
		}
		return true;
	}
	
	public void setLobby(GameLobby lobby) {
		this.lobby = lobby;
	}
	
	public GameSession sendArenaMessage(Player p, String...strings) {
		for(String s : strings) {
			p.sendMessage("  §bArena §8> §b"+s);
		}
		return this;
	}
	public GameSession sendArenaAlert(String...strings) {
		for(String s : strings) {
			for(Player p : players.keySet()) p.sendMessage("  §bArena §8> §b"+s);
		}
		return this;
	}
	
	public PlayerProfile loadProfile(Player p) {
		return new PlayerProfile(p.getUniqueId());
	}
	
	public void addKill(Player p, int kills) {
		profiles.get(p).kills+=kills;
	}
	public void addScore(Player p, int score) {
		profiles.get(p).score+=score;
	}
	public void addCoins(Player p, int coins) {
		profiles.get(p).coins+=coins;
	}
	
	public class PlayerProfile {
		
		public int kills = 0;
		public int score = 0;
		public int coins = 0;
		
		public PlayerProfile(UUID uuid) {
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public void endgame() {
		for(Player p : players.keySet()) {
			p.teleport(spawn_location);
			lobby.playPlayerSound(p, Sound.BLOCK_NOTE_BLOCK_GUITAR, p.getLocation(), 1f, 4f);
			p.sendMessage("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n ");
		}
		players.clear();
		for(PlayerProfile pr : profiles.values()) {
//			pr.save();
		}
	}
	
	public int countAllKills() {
		int k = 0;
		for(PlayerProfile pr : profiles.values()) k += pr.kills;
		return k;
	}
	
	public void nextRound() {
		wave = new Wave((runde+1));
		wave.start();
		runde++;
	}
	
	public void stopBecausNoPlayers() {
		wave.clear();
		players.clear();
		SurvivalArena.sessionManager.sessions.remove(this);
	}
	
	int runde = -1;
	int teleport_to_lobby_in = 5;
	boolean allOffline = true;
	public void tick() {
		if(players.isEmpty() == false) {			
			for(Player p : players.keySet()) {
				if(p != null && players.isEmpty() == false) {				
					if(p.isOnline()) {
						allOffline = false;
					}else{
						players.remove(p);
						sendArenaAlert(p.getName()+" ist ausgeschieden");
					}
				}else allOffline = true;
			}
		}else allOffline = true;
		if(allOffline) {
			stopBecausNoPlayers();
		}else {			
			if(runde == max_rounds && wave.isCleared()) {
				if(teleport_to_lobby_in == 5) {
					sendArenaAlert("§5Das Spiel ist vorbei!");
					sendArenaAlert("§dTeleport in §f5");
				}else if(teleport_to_lobby_in == 4) {
					sendArenaAlert("§f4 §dSekunden...");
				}else if(teleport_to_lobby_in == 3) {
					sendArenaAlert("§f3 §dSekunden...");
				}else if(teleport_to_lobby_in == 2) {
					sendArenaAlert("§f2 §dSekunden...");
				}else if(teleport_to_lobby_in == 1) {
					sendArenaAlert("§f1 §dSekunden...");
				}else if(teleport_to_lobby_in == 0) {
					endgame();
				}
				teleport_to_lobby_in--;
			}else {
				if(runde > -1 && wave != null && wave.isCleared()) {
					nextRound();
					sendArenaAlert("Runde "+getRoundString());				
				}
			}
		}
	}
	
	public String getRoundString() {
		return "§f"+runde+"§7/§f"+max_rounds;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller() != null && players.containsKey(e.getEntity().getKiller())) {			
			if(e.getEntity() instanceof Monster) {
				addKill(e.getEntity().getKiller(), 1);
				sendArenaMessage(e.getEntity().getKiller(), "§a+1 Kill(§f"+profiles.get(e.getEntity().getKiller()).kills+"§a)");
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(players.containsKey(p)) {
				if(e.getDamage() > p.getHealth()) {
					e.setCancelled(true);
					p.damage(0.00000000000000000001d);
					p.teleport(lobby.arena_spawn);
					p.setHealth(20d);
					p.setFoodLevel(20);
					p.getActivePotionEffects().clear();
				}
			}
		}
	}
	
	public class SessionRules {
		
		public int max_kills = 3;
		
	}
	
	public class WaveManager {
		
	}
	
	public class Wave {
		
		public int wave_number = 0;
		public ArrayList<Monster> monsters = new ArrayList<Monster>();
		public ArrayList<Location> spawns = new ArrayList<Location>();
		
		public Wave(int wave_number) {
			this.wave_number = wave_number;
			amount_of_monsters = wave_number * 2 * players.size();
			spawns.add(new Location(Bukkit.getWorld("world"), -4,  82, -184,   41, 1));
			spawns.add(new Location(Bukkit.getWorld("world"), -4,  82, -173,  146, 1));
			spawns.add(new Location(Bukkit.getWorld("world"), -14, 82, -174, -134, 1));
			spawns.add(new Location(Bukkit.getWorld("world"), -14, 82, -184,  -45, 1));
		}
		
		int amount_of_monsters = 0;
		int ran_index = 0;
		public void start() {// 8 12
			for(int i = 0; i != amount_of_monsters; i++) {
				ran_index = random.nextInt(spawns.size());
				Zombie zombie = spawns.get(ran_index).getWorld().spawn(spawns.get(ran_index), Zombie.class);
				zombie.setCustomName("§eVon Session §f"+id);
				zombie.setCustomNameVisible(true);
				zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
				monsters.add(zombie);
			}
		}
		
		public boolean isCleared() {
			for(Monster mon : monsters) if(mon.isDead() == false) return false;
			
			return true;
		}
		
		public void setTargets() {
			if(players.isEmpty() == false) {				
				for(Monster m : monsters) {
					if(m.getTarget() == null) {
						m.setTarget((LivingEntity) players.keySet().toArray()[random.nextInt(players.size())]);
					}else if(players.containsKey(m.getTarget()) == false) {
						m.setTarget((LivingEntity) players.keySet().toArray()[random.nextInt(players.size())]);				
					}
				}
			}
		}
		
		public void clear() {
			for(Monster m : monsters) m.remove();
		}
		
	}
	
	
}
