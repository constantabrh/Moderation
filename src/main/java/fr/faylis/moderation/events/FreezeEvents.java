package fr.faylis.moderation.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import fr.faylis.moderation.utils.Particles;
import fr.faylis.moderation.utils.StringList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.xenondevs.particle.ParticleEffect;

public class FreezeEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Particles particles = new Particles(player);
        Moderation.getInstance().playerParticles.put(player.getUniqueId(), particles);
        if(PlayerManager.isFrozen(player)){
            particles.summonRotatingHelix(ParticleEffect.FALLING_DUST);
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event){
        Player player = event.getPlayer();
        if(PlayerManager.isFrozen(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(PlayerManager.isFrozen(player)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void disableCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        StringList stringList = new StringList();
        if(PlayerManager.isFrozen(player) && !player.hasPermission("moderation.freeze.bypass-command-disable")){
            player.sendMessage(stringList.prefix.toString() + stringList.freeze_disabled_command.toString());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void cantPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(PlayerManager.isFrozen(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void cantBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(PlayerManager.isFrozen(player)){
            event.setCancelled(true);
        }
    }



}
