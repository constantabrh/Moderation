package fr.faylis.moderation.events;

import fr.faylis.moderation.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ModerationCancelEvents implements Listener {


    @EventHandler
    public void moderatorDropEvent(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(PlayerManager.isInModeratorMode(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void moderatorPickUpEvent(EntityPickupItemEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(PlayerManager.isInModeratorMode(player)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void moderatorInventoryMovement(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player player = ((Player) event.getWhoClicked()).getPlayer();
            boolean bool = (event.getClickedInventory() == player.getInventory());
            if(event.getCurrentItem() != null && PlayerManager.isInModeratorMode(player) && bool){
                player.sendMessage("§cYou cannot interact with your inventory while in moderation mode!");
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void moderatorPlaceEvent(BlockPlaceEvent event){
        if(event.getPlayer() instanceof Player){
            Player player = event.getPlayer();
            if(PlayerManager.isInModeratorMode(player)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void damageEvent(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(PlayerManager.isInModeratorMode(player)){
                event.setCancelled(true);
            }
        }
    }

}
