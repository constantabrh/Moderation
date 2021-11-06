package fr.faylis.moderation.utils;

import me.devtec.theapi.configapi.Config;
import org.bukkit.Bukkit;

public class StringList {
    /* Main */
    public MyString prefix;
    public MyString no_permissions;
    public MyString target_offline;

    /* Inventory */
    public MyString inventory_command;
    public MyString inventory_open_target;

    /* Freeze */
    public MyString freeze_command;
    public MyString freeze_on_others;
    public MyString freeze_on_byothers;
    public MyString freeze_off_others;
    public MyString freeze_off_byothers;
    public MyString freeze_disabled_command;

    /* Moderation mode */
    public MyString turnOn_moderation_mode;
    public MyString turnOff_moderation_mode;

    /* Slow chat */
    public MyString slow_chat_cancel;
    public MyString slow_chat_timer;

    /* Vanish */
    public MyString vanish_command;

    public MyString vanish_turnOn;
    public MyString vanish_turnOn_others;
    public MyString vanish_turnOn_byothers;

    public MyString vanish_turnOff;
    public MyString vanish_turnOff_others;
    public MyString vanish_turnOff_byothers;

    /* Random  teleport */
    public MyString randomtp_empty;
    public MyString randomtp_teleport;

    public StringList(){
        Config lang = new Config("Moderation/lang.yml");
        try {
            /* Other messages */
            prefix = new MyString(lang.getString("prefix"));
            no_permissions = new MyString(lang.getString("no-permissions"));
            target_offline = new MyString(lang.getString("target-offline"));

            /* Random TP */
            randomtp_empty = new MyString(lang.getString("randomtp.empty"));
            randomtp_teleport = new MyString(lang.getString("randomtp.teleport"));

            /* Inventory */
            inventory_command = new MyString(lang.getString("inventory.command"));
            inventory_open_target = new MyString(lang.getString("inventory.open-target"));

            /* Freeze */
            freeze_command = new MyString(lang.getString("freeze.command"));
            freeze_on_others = new MyString(lang.getString("freeze.on.others"));
            freeze_on_byothers = new MyString(lang.getString("freeze.on.byothers"));
            freeze_off_others = new MyString(lang.getString("freeze.off.others"));
            freeze_off_byothers = new MyString(lang.getString("freeze.off.byothers"));
            freeze_disabled_command = new MyString(lang.getString("freeze.disabled-command"));

            /* Moderation mode messages */
            turnOn_moderation_mode = new MyString(lang.getString("turnOn-moderation-mode"));
            turnOff_moderation_mode = new MyString(lang.getString("turnOff-moderation-mode"));

            /* Moderation slow chat */
            slow_chat_cancel = new MyString(lang.getString("slow-chat.cancel"));
            slow_chat_timer = new MyString(lang.getString("slow-chat.timer"));

            /* Vanish */
            vanish_command = new MyString(lang.getString("vanish.command"));

            /* Vanish on */
            vanish_turnOn = new MyString(lang.getString("vanish.turnOn"));
            vanish_turnOn_others = new MyString(lang.getString("vanish.turnOn-others"));
            vanish_turnOn_byothers = new MyString(lang.getString("vanish.turnOn-by-others"));

            /* Vanish off */
            vanish_turnOff = new MyString(lang.getString("vanish.turnOff"));
            vanish_turnOff_others = new MyString(lang.getString("vanish.turnOff-others"));
            vanish_turnOff_byothers = new MyString(lang.getString("vanish.turnOff-by-others"));
        } catch (Exception exception){
            Bukkit.getConsoleSender().sendMessage("Exception StringList.java : " + exception);
        }



    }

}
