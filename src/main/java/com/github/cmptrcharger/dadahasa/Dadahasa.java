package com.github.cmptrcharger.dadahasa;


import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Dadahasa extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getLogger().info("Hello, plugin has started up correctly!");

        WorldCreator wc = new WorldCreator("ArcadeMap");
        Bukkit.createWorld(wc);
        WorldCreator wc2 = new WorldCreator("Superflat");
        Bukkit.createWorld(wc2);
        WorldCreator wc3 = new WorldCreator("Random");
        Bukkit.createWorld(wc3);
    }

    @Override
    public void onDisable() {

    }

    public static ArrayList<Player> ps = new ArrayList<>();
    public static boolean canJoin = true;
    public static boolean gameStarted = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            World w1 = getServer().getWorld("Superflat");
            World w2 = getServer().getWorld("Arcade");
            Location loc = new Location(w1, 0.5, 22, 0.5);
            Location loc2 = new Location(w2, 0.5, 22, 0.5);
            if (cmd.getName().equalsIgnoreCase("hub")) {
                p.teleport(loc);
                return true;
            } else if (cmd.getName().equalsIgnoreCase("join")) {
                int length = args.length;
                if (length > 0) {
                    if (args[0].equalsIgnoreCase("ctk")) {
                        if (canJoin) {
                            ps.add(p);
                            p.teleport(loc2);
                            if (ps.size() == 10) {
                                canJoin = false;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "This Game Is Full, Please Try Again In A Bit");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "The Correct Usage Is\n/join ctk");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "The Correct Usage Is\n/join ctk");
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase("forcestart")) {
                if (p.hasPermission("mc.mod")) {
                    canJoin = false;
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have sufficient permissions");
                }
                return true;
            } else if (cmd.getName().equalsIgnoreCase("stopspam")) {
                ps.removeAll(ps);
                canJoin = true;
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You Must Be A Player");
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location loc = new Location(Bukkit.getWorld("Superflat"), 0.5, 22, 0.5);
        player.teleport(loc);
        event.setJoinMessage("");


    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Location loc = new Location(p.getWorld(), 0.5, 22, 0.5);
        e.setRespawnLocation(loc);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + p.getName() + " died.");
        e.setDeathMessage("");
    }

    @EventHandler
    public void onBlock(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (canJoin) {
            if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_LAMP_ON) {
                Location loc = new Location(Bukkit.getWorld("ArcadeMap"), 0.5, 22, 0.5);
                p.teleport(loc);
                p.sendMessage(ChatColor.GREEN + "You Have Joined \"Catch The Killer\"");
                ps.add(p);
                if (ps.size() == 9) {
                    canJoin = false;
                }
            }
        } else {
            p.sendMessage(ChatColor.RED + "This Game Is Full, Please Try Again In A Bit");
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity damaged = e.getEntity();
        Location loc = new Location(damaged.getWorld(), 0.5, 22, 0.5);
        if (damaged instanceof Player) {
            Player dmgd = (Player) damaged;
            if (damager instanceof Player) {
                Player dmgr = (Player) damager;
                if (dmgr.getItemInHand().getType() == Material.IRON_SWORD) {
                    dmgd.teleport(loc);
                    e.setCancelled(true);
                }
            } else if (damager instanceof Arrow) {
                dmgd.teleport(loc);
                e.setCancelled(true);
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onFallDamage(final EntityDamageEvent e) {
        if (e.getCause() == DamageCause.FALL && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFishingEvent(PlayerFishEvent event) {
        Player p = event.getPlayer();
        p.setHealth(0);
    }
}