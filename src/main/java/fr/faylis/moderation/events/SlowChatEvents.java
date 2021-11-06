package fr.faylis.moderation.events;

import fr.faylis.moderation.Moderation;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;

public class SlowChatEvents implements Listener {
    Moderation m = Moderation.getInstance();
    @EventHandler
    public void onChat(AsyncChatEvent event){
        Player player = event.getPlayer();
        if(!player.hasPermission("moderation.slowchat.bypass")){
            long currentTime = new Date().getTime();
            if(m.slowChat.containsKey(player)){
                long lastMessage = m.slowChat.get(player);
                int timer[] = m.timer;
                long until = lastMessage + 60L*1000 *timer[1] + timer[0]*1000L;
                if(currentTime < until){
                    long between = (until - currentTime)/1000;
                    int minutes = (int) between/60;
                    int seconds = (int)between%60;
                    int timerLeft[] = {seconds, minutes};
                    player.sendMessage(m.stringList.prefix.toString() + m.stringList.slow_chat_cancel.timerLeft(timerLeft).toString());
                    event.setCancelled(true);
                } else {
                    m.slowChat.remove(player);
                }
            } else {
                m.slowChat.put(player, currentTime);
            }

        }

    }

}
