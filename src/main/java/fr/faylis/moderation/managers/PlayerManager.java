package fr.faylis.moderation.managers;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.utils.StringList;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.configapi.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayerManager {
    private Player player;
    private ItemStack[] inventory = new ItemStack[41];

    public PlayerManager(Player player) { this.player = player; }
    public static PlayerManager getFromPlayer(Player player){ return Moderation.getInstance().getPlayersList().get(player.getUniqueId()); }

    public static boolean isOnlineAndPlayer(String name){
        Player player = Bukkit.getPlayer(name);
        return player != null;
    }

    public static boolean isInModeratorMode(Player player){ return Moderation.getInstance().modsList.contains(player.getUniqueId()); }

    public static boolean isVanished(Player player){ return Moderation.getInstance().vanishList.contains(player.getUniqueId()); }

    public static boolean isFrozen(Player player){ return Moderation.getInstance().frozenList.contains(player.getUniqueId()); }

    public static HashMap<Integer, ItemStack> getItemFromPath(String path, Player moderator, HashMap<Integer, ItemStack> itemStackHashMap){
        Config config = new Config("Moderation/config.yml");
        String type = "", displayname = "";
        List<String> lore = new ArrayList<>();
        int slot = 0;
        type = config.getString(path + ".type");
        displayname = config.getString(path + ".displayname");
        lore = config.getStringList(path + ".lore");
        slot = config.getInt(path + ".slot");
        Material material = Material.valueOf(type);
        if(material == null){ material = Material.STONE; }
        ItemStack itemStack;
        if(material.equals(Material.PLAYER_HEAD)){
            itemStack = ItemCreatorAPI.createHead(1, displayname, moderator.getName(), lore);
        } else {
            itemStack = ItemCreatorAPI.create(material, 1, displayname, lore);
        }
        if(slot > 8){ slot = slot % 9; }
        itemStackHashMap.put(slot, itemStack);
        return itemStackHashMap;
    }
    private static HashMap<Integer, ItemStack> loadModeratorItemFromConfig(Player player){
        HashMap<Integer, ItemStack> map = new HashMap<>();
        try {
            if(isVanished(player)){ map = getItemFromPath("items.disableVanish", player, map); }
            else { map = getItemFromPath("items.enableVanish", player, map); }
        } catch (Exception exception){ Bukkit.getConsoleSender().sendMessage("Exception : " + exception); }
        try {
            map = getItemFromPath("items.random-tp", player, map);
            map = getItemFromPath("items.moderation-menu", player, map);
            map = getItemFromPath("items.players-inventory", player, map);
            map = getItemFromPath("items.freeze-player", player, map);
        } catch (Exception exception){ Bukkit.getConsoleSender().sendMessage("Exception : " + exception); }
        return map;
    }
    public void giveModeratorItems(){
        HashMap<Integer, ItemStack> map = loadModeratorItemFromConfig(player);
        map.forEach((k, v) -> {
            player.getInventory().setItem(k, v);
        });
    }


    public static ArrayList<ItemStack> getListModerationItems(Player player){
        HashMap<Integer, ItemStack> map = loadModeratorItemFromConfig(player);
        ArrayList<ItemStack> list = new ArrayList<>();
        map.forEach((k, v) -> { list.add(v); });
        return list;
    }

    public void init(){
        StringList stringList = new StringList();
        Moderation.getInstance().getPlayersList().put(player.getUniqueId(), this);
        Moderation.getInstance().modsList.add(player.getUniqueId());
        saveInventory();
        player.sendMessage(stringList.prefix.translateHexColorCodes().toString() + stringList.turnOn_moderation_mode.toString());
    }

    public void destroy(){
        StringList stringList = new StringList();
        Moderation.getInstance().modsList.remove(player.getUniqueId());
        Moderation.getInstance().getPlayersList().remove(player.getUniqueId());
        giveInventory();
        player.sendMessage(stringList.prefix.translateHexColorCodes().toString() + stringList.turnOff_moderation_mode.toString());
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void saveInventory(){
        for(int slot = 0; slot < 36; slot++){
            ItemStack item = player.getInventory().getItem(slot);
            if(item != null){
               inventory[slot] = item;
            }
        }
        inventory[36] =  player.getInventory().getHelmet();
        inventory[37] =  player.getInventory().getChestplate();
        inventory[38] =  player.getInventory().getLeggings();
        inventory[39] =  player.getInventory().getBoots();
        inventory[40] =  player.getInventory().getItemInOffHand();
        String playerInventoryPath = "Moderation/inventory/" + player.getUniqueId() + ".yml";
        File file = new File(playerInventoryPath);
        if(!file.exists()){
            Config config = new Config(playerInventoryPath);
            try {
                for(int i = 0; i < 41; i++){
                    ItemStack itemStack = inventory[i];
                    if(itemStack != null && !itemStack.getType().equals(Material.AIR)){
                        config.set("items." + i, itemStack);
                    }
                }
                config.save();
            } catch (Exception exception){
                Moderation.getInstance().getLogger().warning("§cException : §e" + exception);
            }
        }
        player.getInventory().clear();
        giveModeratorItems();
    }

    public void giveInventory(){
        player.getInventory().clear();
        boolean isInventoryEmpty = true;
        int i = 0;
        while(isInventoryEmpty && i < 41){
            ItemStack itemStack = inventory[i];
            if(itemStack != null){
                if(!itemStack.getType().equals(Material.AIR)){
                    isInventoryEmpty = false;
                }
            }
            i++;
        }
        if(isInventoryEmpty){ loadInventoryFromConfig(); }
        for(int slot = 0; slot < 41; slot++){
            ItemStack item = inventory[slot];
            if(item != null){
                if(slot < 36){ player.getInventory().setItem(slot, item); }
                else {
                    switch (slot) {
                        case 36:
                            player.getInventory().setHelmet(item);
                        case 37:
                            player.getInventory().setChestplate(item);
                        case 38:
                            player.getInventory().setLeggings(item);
                        case 39:
                            player.getInventory().setBoots(item);
                        case 40:
                            player.getInventory().setItemInOffHand(item);
                    }
                }
            }
        }
        clearInventoryFile();
    }

    private void clearInventoryFile(){
        String path = "/Moderation/inventory/" + player.getUniqueId() + ".yml";
        Config config = new Config(path);
        for(int i = 0; i < 41; i++){
            if(config.exists("items." + i)){
                config.remove("items." + i);
            }
        }
        config.save();
    }

    private void loadInventoryFromConfig() {
        String path = "/Moderation/inventory/" + player.getUniqueId() + ".yml";
        try {
            Config config = new Config(path);
            if(config.exists("items")){
                for(int i = 0; i < 41; i++){
                    if(config.exists("items." + i)){
                        Object object = config.get(String.valueOf(i));
                        if(object instanceof ItemStack){
                            ItemStack itemStack = (ItemStack) object;
                            inventory[i] = itemStack;
                        }
                    }
                }
            }
        } catch (Exception exception){
            Moderation.getInstance().getLogger().warning("§cException: §e"+ exception);
        }



    }
}
