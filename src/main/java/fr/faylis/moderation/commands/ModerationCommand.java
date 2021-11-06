package fr.faylis.moderation.commands;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import fr.faylis.moderation.utils.StringList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModerationCommand implements TabExecutor {
    private Moderation instance = Moderation.getInstance();
    private StringList stringList = instance.getStringList();
    private ArrayList<UUID> modsList = instance.getModsList();

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
        try{
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(label.equalsIgnoreCase("moderation") || label.equalsIgnoreCase("mod")){
                    if(!player.hasPermission("moderation.moderation.command")){
                        player.sendMessage(stringList.prefix.toString() + stringList.no_permissions.toString());
                        return false;
                    }
                    if(PlayerManager.isInModeratorMode(player)){
                        PlayerManager.getFromPlayer(player).destroy();
                        return true;
                    } else {
                        new PlayerManager(player).init();
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception exception){
            Moderation.getInstance().getLogger().warning("§cException " + exception);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length > 0){ return new ArrayList<String>(); }
        return null;
    }
}
