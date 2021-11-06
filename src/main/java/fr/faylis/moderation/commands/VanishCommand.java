package fr.faylis.moderation.commands;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import fr.faylis.moderation.utils.StringList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements TabExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            StringList stringList = new StringList();
            if(player.hasPermission("moderation.vanish.command")){
                switch (args.length){
                    case 0 -> {
                        if(PlayerManager.isVanished(player)){
                            Moderation.getInstance().vanishList.remove(player.getUniqueId());
                            player.sendMessage(stringList.prefix.toString() + stringList.vanish_turnOff.player(player).toString());
                            for(Player players : Bukkit.getOnlinePlayers()){ players.showPlayer(Moderation.getInstance(), player); }
                            if(PlayerManager.isInModeratorMode(player)){ PlayerManager.getFromPlayer(player).giveModeratorItems(); }
                        } else {
                            Moderation.getInstance().vanishList.add(player.getUniqueId());
                            player.sendMessage(stringList.prefix.toString() + stringList.vanish_turnOn.player(player).toString());
                            for(Player players : Bukkit.getOnlinePlayers()){ players.hidePlayer(Moderation.getInstance(), player); }
                            if(PlayerManager.isInModeratorMode(player)){ PlayerManager.getFromPlayer(player).giveModeratorItems(); }
                        }

                    }
                    case 1 -> {
                        if(!player.hasPermission("moderation.vanish.command.others")){ player.sendMessage("Don't have permissions"); }
                        if(!PlayerManager.isOnlineAndPlayer(args[0])){ player.sendMessage("Target not online nor exist"); }
                        Player target = Bukkit.getPlayer(args[0]);
                        if(PlayerManager.isVanished(target)){
                            Moderation.getInstance().vanishList.remove(target.getUniqueId());
                            target.sendMessage(stringList.prefix.toString() + stringList.vanish_turnOff_byothers.player(player).target(target).toString());
                            player.sendMessage(stringList.prefix.toString() + stringList.vanish_turnOff_others.player(player).target(target).toString());
                            for(Player players : Bukkit.getOnlinePlayers()){ players.showPlayer(Moderation.getInstance(), target); }
                            if(PlayerManager.isInModeratorMode(target)){ PlayerManager.getFromPlayer(target).giveModeratorItems(); }
                        } else {
                            Moderation.getInstance().vanishList.add(target.getUniqueId());
                            target.sendMessage(stringList.prefix.toString() + stringList.vanish_turnOn_byothers.player(player).target(target).toString());
                            player.sendMessage(stringList.prefix.toString() + stringList.vanish_turnOn_others.player(player).target(target).toString());
                            for(Player players : Bukkit.getOnlinePlayers()){ players.hidePlayer(Moderation.getInstance(), target); }
                            if(PlayerManager.isInModeratorMode(target)){ PlayerManager.getFromPlayer(target).giveModeratorItems(); }
                        }
                    }
                    default -> {
                        player.sendMessage(stringList.prefix.toString() + stringList.vanish_command.translateHexColorCodes().toString());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length > 1){ return new ArrayList<String>(); }
        return null;
    }
}
