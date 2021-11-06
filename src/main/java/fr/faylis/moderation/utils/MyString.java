package fr.faylis.moderation.utils;

import fr.faylis.moderation.Moderation;
import me.devtec.theapi.configapi.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyString {
    String str;
    private Moderation m = Moderation.getInstance();
    public final char COLOR_CHAR = ChatColor.COLOR_CHAR;
    public MyString(String str) {
        this.str = str;
        if(str != null){
            translateHexColorCodesString();
        }

    }
    public void setStr(String str) { this.str = str; }

    public MyString player(Player player){ return new MyString(str.replace("{player}", player.getName())); }
    public MyString target(Player target){ return new MyString(str.replace("{target}", target.getName())); }
    public MyString targetString(String target){ return new MyString(str.replace("{target}", target)); }
    public MyString timer(){
        String format = "";
        int timer[] = m.timer;
        Config config = new Config("Moderation/lang.yml");
        if(timer[0] == 0 && timer[1] == 0){ format = config.getString("slow-chat.format.0m-0s");
        } else if(timer[0] == 0){ format = config.getString("slow-chat.format.0s");
        } else if(timer[1] == 0){ format = config.getString("slow-chat.format.0m"); }
        else { format = config.getString("slow-chat.format.default"); }
        format = format.replace("{minutes}", String.valueOf(timer[1])).replace("{seconds}", String.valueOf(timer[0]));
        return new MyString(str.replace("{timer}", format));
    }

    public MyString timerLeft(int timer[]){
        String format = "";
        Config config = new Config("Moderation/lang.yml");
        if(timer[0] == 0 && timer[1] == 0){ format = config.getString("slow-chat.format.0m-0s");
        } else if(timer[0] == 0){ format = config.getString("slow-chat.format.0s");
        } else if(timer[1] == 0){ format = config.getString("slow-chat.format.0m"); }
        else { format = config.getString("slow-chat.format.default"); }
        format = format.replace("{minutes}", String.valueOf(timer[1])).replace("{seconds}", String.valueOf(timer[0]));
        return new MyString(str.replace("{timer}", format));
    }

    public MyString translateHexColorCodes() {
        final Pattern hexPattern = Pattern.compile("\\{" + "#([A-Fa-f0-9]{6})" + "\\}");
        Matcher matcher = hexPattern.matcher(str);
        StringBuffer buffer = new StringBuffer(str.length() + 4 * 8);
        while (matcher.find()){
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return new MyString(matcher.appendTail(buffer).toString());
    }

    public void translateHexColorCodesString() {
        try {
            final Pattern hexPattern = Pattern.compile("\\{" + "#([A-Fa-f0-9]{6})" + "\\}");
            if(str == null){ Bukkit.getConsoleSender().sendMessage("Str null"); }
            Matcher matcher = hexPattern.matcher(str);
            StringBuffer buffer = new StringBuffer(str.length() + 4 * 8);
            while (matcher.find()){
                String group = matcher.group(1);
                matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                        + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                        + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                        + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                );
            }
            this.str = matcher.appendTail(buffer).toString();
        } catch (Exception exception){
            Bukkit.getConsoleSender().sendMessage("Exception MyString.java: " + exception);
        }
    }


    @Override
    public String toString() { return str; }


}
