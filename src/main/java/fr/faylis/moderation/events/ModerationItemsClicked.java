package fr.faylis.moderation.events;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.gui.ModerationMenu;
import fr.faylis.moderation.managers.PlayerManager;
import fr.faylis.moderation.utils.StringList;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.scheduler.Scheduler;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.TimerTask;

public class ModerationItemsClicked implements Listener {

    @EventHandler
    public void moderatorClickItems(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack hold_items = event.getItem();
        ArrayList<ItemStack> list = PlayerManager.getListModerationItems(player);
        StringList stringList = new StringList();
        Config config = new Config("Moderation/config.yml");
        boolean action = (event.getAction().isRightClick() && event.getHand().equals(EquipmentSlot.HAND));
        if(hold_items != null && PlayerManager.isInModeratorMode(player) && list.contains(hold_items) && hold_items.getItemMeta().hasDisplayName() && action){
            try{
                Material moderation_menu = Material.valueOf(config.getString("items.moderation-menu.type"));
                Material random_tp = Material.valueOf(config.getString("items.random-tp.type"));
                Material disableVanish = Material.valueOf(config.getString("items.disableVanish.type"));
                Material enableVanish = Material.valueOf(config.getString("items.enableVanish.type"));
                if(list.contains(hold_items)){
                    if(hold_items.getType().toString().equalsIgnoreCase(moderation_menu.toString())){
                        ModerationMenu.open(player);
                    } else if(hold_items.getType().toString().equalsIgnoreCase(random_tp.toString())){
                        boolean teleport = false;
                        ArrayList<Player> my_list = Moderation.getInstance().onlinePlayerList;
                        my_list.removeIf(p -> p.hasPermission("moderation.bypass-random-tp") || p.equals(player));
                        if(my_list.size() > 0){
                            while(!teleport){
                                int rand = (int) (Math.random() * Moderation.getInstance().onlinePlayerList.size());
                                Player target = my_list.get(rand);
                                if(target != null && !target.equals(player) && !target.hasPermission("moderation.bypass-random-tp")){
                                    teleport = true;
                                    player.teleport(target);
                                    player.sendMessage(stringList.prefix.toString() + stringList.randomtp_teleport.target(target).toString());
                                }
                            }
                        } else {
                            player.sendMessage(stringList.prefix.toString() + stringList.randomtp_empty.toString());
                        }
                    } else if(hold_items.getType().toString().equalsIgnoreCase(disableVanish.toString()) || hold_items.getType().toString().equalsIgnoreCase(enableVanish.toString())){
                        Bukkit.dispatchCommand(player, "fvanish");
                        int task = Bukkit.getScheduler().scheduleSyncDelayedTask(Moderation.getInstance(), new Runnable() {
                            public void run() {
                                PlayerManager.getFromPlayer(player).giveModeratorItems();
                            }
                        }, 2L);
                    }
                }
            } catch (Exception exception){
                event.getPlayer().sendMessage("Exception : " + exception);
            }

        }
    }

    @EventHandler
    public void moderatorClickItemsAtEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        ItemStack hold_items = player.getPlayer().getEquipment().getItemInMainHand();
        ArrayList<ItemStack> list = PlayerManager.getListModerationItems(player);
        if(entity instanceof Player){
            Player target = ((Player) entity).getPlayer();
            boolean isMainHand = (event.getHand().equals(EquipmentSlot.HAND)), itemExist = (hold_items != null), listContains = list.contains(hold_items);
            if(itemExist && PlayerManager.isInModeratorMode(player) && listContains && isMainHand){
                if(list.contains(hold_items)){
                    switch (hold_items.getType()){
                        case BLAZE_ROD -> Bukkit.dispatchCommand(player, "freeze " + target.getName());
                        case PLAYER_HEAD -> Bukkit.dispatchCommand(player, "inventory " + target.getName());
                    }
                }
            }
        }
    }
}
