package fr.faylis.moderation.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryMenu {

    public static void open(Player player, Player target){
        ChestGui chestGui = new ChestGui(6, "§e» §c"+target.getName() + "'s inventory");
        chestGui.setOnGlobalClick(event -> event.setCancelled(true));
        ItemStack black_stained_glass_pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemStack white_stained_glass_pane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        OutlinePane outlinePane = new OutlinePane(0, 0, 9, 4);
        OutlinePane outlinePaneBottom = new OutlinePane(0, 4, 9, 2);
        for(int i = 0; i < 36; i++){
            ItemStack itemStack = (target.getInventory().getItem(i) == null || target.getInventory().getItem(i).equals(Material.AIR) ? white_stained_glass_pane : target.getInventory().getItem(i));
            if(itemStack != null){ outlinePane.addItem(new GuiItem(itemStack)); }
        }

        for(int i = 0; i < 9; i++){ outlinePaneBottom.addItem(new GuiItem(black_stained_glass_pane)); }
        ItemStack helmet = (target.getInventory().getHelmet() == null ? white_stained_glass_pane : target.getInventory().getHelmet());
        ItemStack chestplate = (target.getInventory().getChestplate() == null ? white_stained_glass_pane : target.getInventory().getChestplate());
        ItemStack leggings = (target.getInventory().getLeggings() == null ? white_stained_glass_pane : target.getInventory().getLeggings());
        ItemStack boots = (target.getInventory().getBoots() == null ? white_stained_glass_pane : target.getInventory().getBoots());
        ItemStack second_hand = (target.getInventory().getItemInOffHand().getType().equals(Material.AIR) ? white_stained_glass_pane : target.getInventory().getItemInOffHand());
        outlinePaneBottom.addItem(new GuiItem(helmet));
        outlinePaneBottom.addItem(new GuiItem(chestplate));
        outlinePaneBottom.addItem(new GuiItem(leggings));
        outlinePaneBottom.addItem(new GuiItem(boots));
        outlinePaneBottom.addItem(new GuiItem(black_stained_glass_pane));
        outlinePaneBottom.addItem(new GuiItem(black_stained_glass_pane));
        outlinePaneBottom.addItem(new GuiItem(black_stained_glass_pane));
        outlinePaneBottom.addItem(new GuiItem(black_stained_glass_pane));
        outlinePaneBottom.addItem(new GuiItem(second_hand));
        chestGui.addPane(outlinePane);
        chestGui.addPane(outlinePaneBottom);
        chestGui.show(player);

    }

}
