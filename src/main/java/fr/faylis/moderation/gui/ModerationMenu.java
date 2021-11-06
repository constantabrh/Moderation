package fr.faylis.moderation.gui;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.configapi.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ModerationMenu {

    private static Moderation m = Moderation.getInstance();
    public static HashMap<String, List<String>> getModerationMenuItem(){
        HashMap<String, List<String>> map = new HashMap<>();
        Config config = new Config("Moderation/config.yml");
        String[] item_names = {"time", "day-time", "night-time", "slow-chat", "vanish", "vanishOn", "vanishOff", "vanishAlreadyOn", "vanishAlreadyOff"};
        for(String item_name : item_names){
            try {
                String displayname = config.getString("moderation-menu." + item_name + ".displayname");
                List<String> list = config.getStringList("moderation-menu." + item_name + ".lore");
                list.add(0, displayname);
                map.put(item_name, list);
            } catch (Exception exception){
                Bukkit.getConsoleSender().sendMessage("Exception: " + exception);
            }
        }
        return map;
    }

    public static void updateVanish(ChestGui chestGui, StaticPane staticPane, Player player){
        HashMap<String, List<String>> map = getModerationMenuItem();
        PlayerManager.getFromPlayer(player).giveModeratorItems();
        /* Vanish items */
        String item_name = "vanishAlreadyOn";
        String displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        List<String> lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishAlreadyON = ItemCreatorAPI.create(Material.GRAY_DYE, 1, displayname, lore);

        item_name = "vanishAlreadyOff";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishAlreadyOFF = ItemCreatorAPI.create(Material.GRAY_DYE, 1, displayname, lore);

        // Turn on vanish item
        item_name = "vanishOn";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishOn = ItemCreatorAPI.create(Material.LIME_DYE, 1, displayname, lore);
        GuiItem vanishOnItem;
        if(PlayerManager.isVanished(player)){ vanishOnItem = new GuiItem(vanishAlreadyON);
        } else { vanishOnItem = new GuiItem(vanishOn, inventoryClickEvent -> { Bukkit.dispatchCommand(player, "fvanish"); updateVanish(chestGui, staticPane, player);}); }

        // Middle vanish item
        item_name = "vanish";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanish = ItemCreatorAPI.create(Material.WHITE_WOOL, 1, displayname, lore);
        GuiItem vanishItem = new GuiItem(vanish, inventoryClickEvent -> {});

        // Turn off vanish item
        item_name = "vanishOff";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishOff = ItemCreatorAPI.create(Material.RED_DYE, 1, displayname, lore);
        GuiItem vanishOffItem;
        if(PlayerManager.isVanished(player)){
            vanishOffItem = new GuiItem(vanishOff, inventoryClickEvent -> { Bukkit.dispatchCommand(player, "fvanish"); updateVanish(chestGui, staticPane, player);});
        } else {
            vanishOffItem = new GuiItem(vanishAlreadyOFF);
        }
        staticPane.addItem(vanishOnItem, 6, 1);
        staticPane.addItem(vanishItem, 6, 2);
        staticPane.addItem(vanishOffItem, 6, 3);
        chestGui.update();
    }

    public static void open(Player player){
        ChestGui gui = new ChestGui(6, "§e» §cModeration menu");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(0, 0, 9, 5);
        /* Timer */
        // Set day time
        HashMap<String, List<String>> map = getModerationMenuItem();
        String item_name = "day-time";
        String displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        List<String> lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack timerDay = ItemCreatorAPI.create(Material.ORANGE_DYE, 1, displayname, lore);
        GuiItem timerDayItem = new GuiItem(timerDay, inventoryClickEvent -> {
            player.getWorld().setTime(0);
        });

        // Middle timer item
        item_name = "time";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack timer = ItemCreatorAPI.create(Material.CLOCK, 1, displayname, lore);
        GuiItem timerItem = new GuiItem(timer, inventoryClickEvent -> {});

        // Set night time
        item_name = "night-time";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack timerNight = ItemCreatorAPI.create(Material.BLACK_DYE, 1, displayname, lore);
        GuiItem timerNightItem = new GuiItem(timerNight, inventoryClickEvent -> {
            player.getWorld().setTime(14000);
        });

        /* Slow down items */
        item_name = "slow-chat";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack slowChat = ItemCreatorAPI.create(Material.CYAN_DYE, 1, displayname, lore);
        GuiItem slowChatItem = new GuiItem(slowChat, inventoryClickEvent -> {
            player.closeInventory();
            int my_timer[] = Moderation.getInstance().timer;
            player.sendMessage("Opening slow down menu");
            openSlownDown(player, my_timer);
        });

        /* Vanish items */
        item_name = "vanishAlreadyOn";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishAlreadyON = ItemCreatorAPI.create(Material.GRAY_DYE, 1, displayname, lore);

        item_name = "vanishAlreadyOff";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishAlreadyOFF = ItemCreatorAPI.create(Material.GRAY_DYE, 1, displayname, lore);

        // Turn on vanish item
        item_name = "vanishOn";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishOn = ItemCreatorAPI.create(Material.LIME_DYE, 1, displayname, lore);
        GuiItem vanishOnItem;
        if(PlayerManager.isVanished(player)){ vanishOnItem = new GuiItem(vanishAlreadyON);
        } else { vanishOnItem = new GuiItem(vanishOn, inventoryClickEvent -> { Bukkit.dispatchCommand(player, "fvanish"); updateVanish(gui, pane, player);}); }

        // Middle vanish item
        item_name = "vanish";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanish = ItemCreatorAPI.create(Material.WHITE_WOOL, 1, displayname, lore);
        GuiItem vanishItem = new GuiItem(vanish, inventoryClickEvent -> {});

        // Turn off vanish item
        item_name = "vanishOff";
        displayname = (map.containsKey(item_name) ? map.get(item_name).remove(0) : "§4No title");
        lore =  (map.containsKey(item_name) && !map.get(item_name).isEmpty()) ?  map.get(item_name) : Arrays.asList();
        ItemStack vanishOff = ItemCreatorAPI.create(Material.RED_DYE, 1, displayname, lore);
        GuiItem vanishOffItem;
        if(PlayerManager.isVanished(player)){
            vanishOffItem = new GuiItem(vanishOff, inventoryClickEvent -> { Bukkit.dispatchCommand(player, "fvanish"); updateVanish(gui, pane, player);});
        } else {
            vanishOffItem = new GuiItem(vanishAlreadyOFF);
        }

        /* Set items in pane */
        // Timer items
        pane.addItem(timerDayItem, 2, 1);
        pane.addItem(timerItem, 2, 2);
        pane.addItem(timerNightItem, 2, 3);

        // Slow chat items
        pane.addItem(slowChatItem, 4, 2);

        // Vanish items
        pane.addItem(vanishOnItem, 6, 1);
        pane.addItem(vanishItem, 6, 2);
        pane.addItem(vanishOffItem, 6, 3);

        // Set GUI pane
        gui.addPane(pane);
        gui.show(player);
    }

    public static void saveCurrentTimer(int timer[]){
        if(timer[0] >= 0 && timer[1] >= 0){
            Config config = new Config("Moderation/config.yml");
            config.set("slow-chat.minutes", timer[1]);
            config.set("slow-chat.seconds", timer[0]);
            config.save();
            config.reload();
        }
    }

    public static void updateTimer(StaticPane navigation, StaticPane timerItemsPane, int timer[], Player player){
        navigation.removeItem(8, 0);
        ItemStack saveTimer = ItemCreatorAPI.create(Material.LIME_STAINED_GLASS_PANE, 1, ChatColor.of("#8866aa") + "Save timer", Arrays.asList("", "§7Current timer:", "§e-> §c" + timer[1] + "m", "§e-> §c" + timer[0] + "s"));
        GuiItem saveTimerGuiItem = new GuiItem(saveTimer, inventoryClickEvent -> {
            saveCurrentTimer(timer);
            updateTimer(navigation, timerItemsPane, timer, player);
            broadcastNewTimer(player);
        });
        navigation.addItem(saveTimerGuiItem, 8,0);
        for(GuiItem guiItem : timerItemsPane.getItems()){
            ItemStack itemStack = guiItem.getItem();
            if(itemStack.getType().equals(Material.NETHERITE_INGOT) || itemStack.getType().equals(Material.BLACK_STAINED_GLASS_PANE)){
                if(timer[1] == 0){ itemStack.setType(Material.BLACK_STAINED_GLASS_PANE); itemStack.setAmount(1); }
                else { itemStack.setAmount(timer[1]); if (itemStack.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) { itemStack.setType(Material.NETHERITE_INGOT); } }
            } else if(itemStack.getType().equals(Material.IRON_INGOT) || itemStack.getType().equals(Material.WHITE_STAINED_GLASS_PANE)){
                if(timer[0] == 0){ itemStack.setType(Material.WHITE_STAINED_GLASS_PANE); itemStack.setAmount(1); }
                else { itemStack.setAmount(timer[0]); if (itemStack.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) { itemStack.setType(Material.IRON_INGOT); } }
            }
        }
    }

    private static void broadcastNewTimer(Player player){ Bukkit.broadcastMessage(m.stringList.prefix + m.stringList.slow_chat_timer.player(player).timer().toString()); }

    public static void openSlownDown(Player player, int timer[]){
        ChestGui gui = new ChestGui(6, "§e» §cSlown chat");
        gui.setOnGlobalClick(inventoryClickEvent -> {});
        // Timers item pane
        StaticPane timerItemsPane = new StaticPane(4,2, 1, 2);
        ItemStack itemStackMinutes = ItemCreatorAPI.create(Material.NETHERITE_INGOT, timer[1], "§cMinutes", Arrays.asList("", "§7Click §e▶ §7 to increase minutes","§7Click §e◀ §7 to lower minutes"));
        if(timer[1] == 0){ itemStackMinutes.setType(Material.BLACK_STAINED_GLASS_PANE); itemStackMinutes.setAmount(1); } else { itemStackMinutes.setAmount(timer[1]); }
        ItemStack itemStackSeconds = ItemCreatorAPI.create(Material.IRON_INGOT, timer[0], "§cSeconds", Arrays.asList("", "§7Click §e▶ §7 to increase seconds","§7Click §e◀ §7 to lower seconds"));
        if(timer[0] == 0){ itemStackSeconds.setType(Material.WHITE_STAINED_GLASS_PANE); itemStackSeconds.setAmount(1); } else { itemStackSeconds.setAmount(timer[0]); }
        timerItemsPane.addItem(new GuiItem(itemStackMinutes), 0,0);
        timerItemsPane.addItem(new GuiItem(itemStackSeconds), 0,1);

        // Navigation
        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        ItemStack goBack = ItemCreatorAPI.create(Material.ARROW, 1, ChatColor.of("#8866aa") + "Go back!", Arrays.asList("", "§e-> §7 Click to go back", "§7to main moderation menu."));
        GuiItem backguiItem = new GuiItem(goBack, inventoryClickEvent -> {
            player.closeInventory();
            open(player);
        });
        navigation.addItem(backguiItem,0 ,0);
        for(int i = 0; i < 7; i++){
            ItemStack itemStack = ItemCreatorAPI.create(Material.BLACK_STAINED_GLASS_PANE, 1, "§c<-", Arrays.asList("", "§e-> §7Minutes = " + timer[1], "§e-> §7Secondes = " + timer[0]));
            GuiItem guiItem = new GuiItem(itemStack);
            navigation.addItem(guiItem, 1+i, 0);
        }
        ItemStack saveTimer = ItemCreatorAPI.create(Material.LIME_STAINED_GLASS_PANE, 1, ChatColor.of("#8866aa") + "Save timer", Arrays.asList("", "§7Current timer:", "§e-> §c" + timer[1] + "m", "§e-> §c" + timer[0] + "s"));
        GuiItem saveTimerGuiItem = new GuiItem(saveTimer, inventoryClickEvent -> {
            saveCurrentTimer(timer);
            updateTimer(navigation, timerItemsPane, timer, player);
            broadcastNewTimer(player);
        });
        navigation.addItem(saveTimerGuiItem, 8,0);

        // Minutes decrement
        Label decrementMinutes = new Label(2, 2, 1, 1, Font.SPRUCE_PLANKS);
        decrementMinutes.setText("-");
        decrementMinutes.setOnClick(inventoryClickEvent -> {
            if(timer[1] > 0){ timer[1]--; }
            updateTimer(navigation, timerItemsPane, timer, player);
            player.sendMessage("§c" + timer[1] + "m " + timer[0] + "s");
            gui.update();
        });
        // Label minutes = new Label(4, 2, 1 ,1, Font.CYAN); minutes.setText("m");
        Label incrementMinutes = new Label(6, 2, 1, 1, Font.SPRUCE_PLANKS);
        incrementMinutes.setText("+");
        incrementMinutes.setOnClick(inventoryClickEvent -> {
            if(timer[1] < 60){ timer[1]++; }
            player.sendMessage("§c" + timer[1] + "m " + timer[0] + "s");
            updateTimer(navigation, timerItemsPane, timer, player);
            gui.update();
        });
        // Seconds
        Label decrementSeconds = new Label(2, 3, 1, 1, Font.SPRUCE_PLANKS);
        decrementSeconds.setText("-");
        decrementSeconds.setOnClick(inventoryClickEvent -> {
            if(timer[0] > 0){ timer[0]--; } else if(timer[0] == 0 && timer[1] > 0){ timer[1]--;timer[0] = 59; }
            player.sendMessage("§c" + timer[1] + "m " + timer[0] + "s");
            updateTimer(navigation, timerItemsPane, timer, player);
            gui.update();
        });

        // Label seconds = new Label(4, 3, 1 ,1, Font.CYAN); seconds.setText("s");

        Label incrementSeconds = new Label(6, 3, 1, 1, Font.SPRUCE_PLANKS);
        incrementSeconds.setText("+");
        incrementSeconds.setOnClick(inventoryClickEvent -> {
            timer[0]++;
            if(timer[0] == 60){
                timer[0] = 0;
                timer[1]++;
                player.sendMessage("§c" + timer[1] + "m " + timer[0] + "s");
            }
            updateTimer(navigation, timerItemsPane, timer, player);
            gui.update();
        });

        gui.addPane(timerItemsPane);
        gui.addPane(decrementMinutes); gui.addPane(incrementMinutes); // gui.addPane(minutes);
        gui.addPane(decrementSeconds);  gui.addPane(incrementSeconds); // gui.addPane(seconds);
        gui.addPane(navigation);
        gui.show(player);
    }

}
