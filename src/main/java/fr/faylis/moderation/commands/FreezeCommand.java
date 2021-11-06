package fr.faylis.moderation.commands;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import fr.faylis.moderation.utils.Particles;
import fr.faylis.moderation.utils.StringList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class FreezeCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            StringList stringList = new StringList();
            if(!player.hasPermission("moderation.freeze.command")){ player.sendMessage(stringList.prefix.toString() + stringList.no_permissions); return true; }
            if(args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){ player.sendMessage(stringList.prefix.toString() + stringList.target_offline.targetString(args[0])); return false; }
                if(PlayerManager.isFrozen(target)){
                    Moderation.getInstance().frozenList.remove(target.getUniqueId());
                    player.sendMessage(stringList.prefix.toString() + stringList.freeze_off_others.player(player).target(target).toString());
                    if(!player.equals(target)){ target.sendMessage(stringList.prefix.toString() + stringList.freeze_off_byothers.player(player).target(target).toString()); }
                    target.removePotionEffect(PotionEffectType.SLOW);
                } else {
                    Moderation.getInstance().frozenList.add(target.getUniqueId());
                    Particles particles = Moderation.getInstance().playerParticles.get(target.getUniqueId());
                    particles.summonRotatingHelix(ParticleEffect.FALLING_DUST);
                    player.sendMessage(stringList.prefix.toString() + stringList.freeze_on_others.player(player).target(target).toString());
                    if(!player.equals(target)){ target.sendMessage(stringList.prefix.toString() + stringList.freeze_on_byothers.player(player).target(target).toString()); }
                    PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOW, 1000000, 255, false, false);
                    target.addPotionEffect(slownessEffect);
                }
                return true;
            }
            player.sendMessage(stringList.prefix.toString() + stringList.freeze_command.toString());
            return false;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> playerList = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        for(Player players : Bukkit.getOnlinePlayers()){ playerList.add(players.getName()); }
        if(args.length == 0){ return playerList; }
        else if(args.length == 1){
            for(String str : playerList){
                if(str.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(str);
                }
            }
            return result;
        } else if(args.length > 1){
            return new ArrayList<String>();
        }
        return null;
    }
}
