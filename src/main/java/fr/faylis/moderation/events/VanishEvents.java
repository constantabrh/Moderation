package fr.faylis.moderation.events;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.UUID;

public class VanishEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        ArrayList<UUID> vanishList = Moderation.getInstance().vanishList;
        for(UUID uuid : vanishList){
            player.hidePlayer(Moderation.getInstance(), Bukkit.getPlayer(uuid));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(PlayerManager.isVanished(player)){
            Moderation.getInstance().vanishList.remove(player.getUniqueId());
        }
        for(Player players : Bukkit.getOnlinePlayers()){
            players.showPlayer(Moderation.getInstance(), player);
        }
    }

}
