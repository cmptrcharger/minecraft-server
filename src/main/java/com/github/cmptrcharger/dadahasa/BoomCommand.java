package com.github.cmptrcharger.dadahasa;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BoomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            Location l = player.getLocation();
            World world = l.getWorld();
            int x = 0;
            if(args.length>0){
                try{
                    x = Integer.parseInt(args[0]);
                }catch(NumberFormatException nfe){
                    nfe.printStackTrace();
                }
            }
            world.createExplosion(l, x);
        }else{
            sender.sendMessage("You must be a player!");
        }



        return false;
    }
}
