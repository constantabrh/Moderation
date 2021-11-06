package fr.faylis.moderation.events;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ModerationEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(PlayerManager.isInModeratorMode(player)){ Moderation.getInstance().getPlayersList().put(player.getUniqueId(), new PlayerManager(player)); }
    }

}
