package fr.faylis.moderation.managers;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.utils.StringList;
import me.devtec.theapi.configapi.Config;

import java.util.Arrays;
import java.util.List;

public class FileManager {
    private static List<String> header = Arrays.asList(
            "#################################",
            "#                               #",
            "# Moderation configuration file #",
            "#                               #",
            "#################################",
            "");
    private static List<String> footer = Arrays.asList("",
            "",
            "#################################",
            "#                               #",
            "#   End of configuration file   #",
            "#                               #",
            "#################################");


    public static void createFiles(){
        try {
            loadConfig();
            loadLang();

            Moderation.getInstance().stringList = new StringList();
        } catch (Exception exception){
            Moderation.getInstance().getLogger().warning("§cException: §b" + exception);
        }

    }

    private static void loadLang() {
        Config lang = Config.loadConfig(Moderation.getInstance(),"lang.yml", "Moderation/lang.yml");
        lang.setComments("vanish", Arrays.asList("", "# Vanish command messages"));
        lang.setComments("freeze", Arrays.asList("", "# Freeze command messages"));
        lang.setComments("inventory", Arrays.asList("", "# Inventory command messages"));
        lang.setComments("randomtp", Arrays.asList("", "# RandomTP messages"));
        lang.setComments("slow-chat", Arrays.asList("", "# Slow-Chat messages"));
        lang.save();
    }

    private static void loadConfig() {
        Config config = Config.loadConfig(Moderation.getInstance(), "config.yml", "Moderation/config.yml");
        config.setComments("moderators", Arrays.asList(
                "",
                "###################",
                "# Moderators list #",
                "###################"));
        config.setComments("vanish", Arrays.asList(
                "",
                "###############",
                "# Vanish list #",
                "###############"));
        config.setComments("items", Arrays.asList(
                "",
                "###################",
                "# Moderator items #",
                "###################"));
        config.setComments("slow-chat", Arrays.asList(
                "",
                "##########################################################################################",
                "#                                Slow chat timing, minutes                               #",
                "# The minutes and seconds must be between 0 and 60, otherwise the modulo will be applied #",
                "##########################################################################################"
        ));
        config.save();
        if(!config.getHeader().contains(header)){ config.setHeader(header); }
        config.save();
    }

}
