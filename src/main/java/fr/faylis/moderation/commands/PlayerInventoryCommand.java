package fr.faylis.moderation.commands;

import fr.faylis.moderation.gui.PlayerInventoryMenu;
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

public class PlayerInventoryCommand implements TabExecutor {
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
            if(args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){ player.sendMessage(stringList.prefix.toString() + stringList.target_offline.targetString(args[0]).toString()); return false; }
                PlayerInventoryMenu.open(player, target);
            } else { player.sendMessage(stringList.prefix.toString() + stringList.inventory_command.toString()); }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length > 1){ return new ArrayList<String>(); }
        return null;
    }
}
