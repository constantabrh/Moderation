package fr.faylis.moderation;

import fr.faylis.moderation.commands.*;
import fr.faylis.moderation.events.*;
import fr.faylis.moderation.managers.FileManager;
import fr.faylis.moderation.managers.PlayerManager;
import fr.faylis.moderation.utils.Particles;
import fr.faylis.moderation.utils.StringList;
import me.devtec.theapi.configapi.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xenondevs.particle.ParticleEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Moderation extends JavaPlugin {


    private static Moderation instance;
    public ArrayList<UUID> modsList;
    public ArrayList<UUID> vanishList;
    public ArrayList<UUID> frozenList;
    public HashMap<UUID, PlayerManager> playersList;
    public HashMap<UUID, Particles> playerParticles;
    public ArrayList<Player> onlinePlayerList;
    public HashMap<Player, Long> slowChat;
    public int timer[] = {0, 0};

    public StringList stringList;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("§aLoading plugin");
        registers();
    }

    @Override
    public void onDisable() {
        unload();
        getLogger().info("§cDisabling plugin");
    }


    public void registers(){
        if(getDataFolder().mkdir()){
            getLogger().info("§aData folder created");
        }
        File file = new File(getDataFolder() + "/inventory/");
        if(file.mkdir()){
            getLogger().info("§aInventory data folder created");
        }
        // Load utils
        FileManager.createFiles();
        load();
        /* Load commands */
        // Moderation mode command
        this.getCommand("moderation").setExecutor(new ModerationCommand());
        this.getCommand("moderation").setTabCompleter(new ModerationCommand());

        // Moderation vanish command
        this.getCommand("fvanish").setExecutor(new VanishCommand());
        this.getCommand("fvanish").setTabCompleter(new VanishCommand());

        // Moderation check inventory command
        this.getCommand("inventory").setExecutor(new PlayerInventoryCommand());
        this.getCommand("inventory").setTabCompleter(new PlayerInventoryCommand());

        //Moderation freeze command
        this.getCommand("freeze").setExecutor(new FreezeCommand());
        this.getCommand("freeze").setTabCompleter(new FreezeCommand());

        // Load events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ModerationCancelEvents(), this);
        pm.registerEvents(new ModerationItemsClicked(), this);
        pm.registerEvents(new VanishEvents(), this);
        pm.registerEvents(new ModerationEvents(), this);
        pm.registerEvents(new FreezeEvents(), this);
        pm.registerEvents(new SlowChatEvents(), this);
    }

    private void load(){
        playersList = new HashMap<>();
        modsList = new ArrayList<>();
        vanishList = new ArrayList<>();
        frozenList = new ArrayList<>();
        playerParticles = new HashMap<>();
        onlinePlayerList = new ArrayList<>();
        slowChat = new HashMap<>();

        Config config = new Config("Moderation/config.yml");
        List<String> modsStringList = new ArrayList<>();
        if(config.exists("moderators")){ modsStringList = config.getStringList("moderators"); }
        for (String str : modsStringList) {
            UUID id = UUID.fromString(str);
            modsList.add(id);
        }
        List<String> vanishStringList = new ArrayList<>();
        if(config.exists("vanish")){ vanishStringList = config.getStringList("vanish"); }
        for (String str : vanishStringList) {
            UUID id = UUID.fromString(str);
            vanishList.add(id);
        }
        List<String> freezeStringList = new ArrayList<>();
        if(config.exists("vanish")){ freezeStringList = config.getStringList("freeze"); }
        for (String str : freezeStringList) {
            UUID id = UUID.fromString(str);
            frozenList.add(id);
        }
        for(Player players : Bukkit.getOnlinePlayers()){
            if(PlayerManager.isInModeratorMode(players)){ playersList.put(players.getUniqueId(), new PlayerManager(players)); }
            onlinePlayerList.add(players);
            playerParticles.put(players.getUniqueId(), new Particles(players));
            if(PlayerManager.isFrozen(players)){ playerParticles.get(players.getUniqueId()).summonRotatingHelix(ParticleEffect.FALLING_DUST); }
        }


        config = new Config("Moderation/config.yml");
        if(config.exists("slow-chat.minutes") && config.exists("slow-chat.seconds")){
            timer[0] = config.getInt("slow-chat.seconds");
            timer[1] = config.getInt("slow-chat.minutes");
            if(timer[1] < 0){ timer[1] *= -1;}
            if(timer[0] < 0){ timer[0] *= -1;}
            if(timer[1] > 60){ timer[1] = (timer[1] % 60); }
            if(timer[0] > 60){ timer[0] = (timer[0] % 60); }
        }

    }

    private void unload(){
        ArrayList<String> listModerators = new ArrayList<>();
        ArrayList<String> listVanish = new ArrayList<>();
        ArrayList<String> listFreeze = new ArrayList<>();
        modsList.forEach(uuid -> { listModerators.add(uuid.toString()); });
        vanishList.forEach(uuid -> { listVanish.add(uuid.toString()); });
        frozenList.forEach(uuid -> { listFreeze.add(uuid.toString()); });

        Config config = new Config("/Moderation/config.yml");
        config.set("moderators", listModerators);
        config.set("vanish", listVanish);
        config.set("freeze", listFreeze);
        config.save();
    }

    public static Moderation getInstance(){
        return instance;
    }

    public HashMap<UUID, PlayerManager> getPlayersList() {
        return playersList;
    }

    public ArrayList<UUID> getModsList() {
        return modsList;
    }

    public StringList getStringList(){
        return stringList;
    }
}
