package fr.faylis.moderation.utils;

import fr.faylis.moderation.Moderation;
import fr.faylis.moderation.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.texture.BlockTexture;

public class Particles {
    Player player;
    public Particles(Player player){ this.player = player; }

    public void summonRotatingHelix(ParticleEffect particleEffect){
        final double[] degree = {0};
        final double[] y = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                double radians = Math.toRadians(degree[0]);
                double radians2 = Math.toRadians(degree[0] + 180);
                if(!PlayerManager.isFrozen(player)){ cancel(); }
                if (isCancelled()) {
                    Bukkit.getConsoleSender().sendMessage("Canceled");
                }
                double x = Math.cos(radians); double z = Math.sin(radians);
                double x2 = Math.cos(radians2); double z2 = Math.sin(radians2);
                y[0] = (degree[0] % 720 > 355) ? y[0] - (double)1/36 : y[0] + (double)1/36;
                Location loc = player.getLocation().add(x, y[0], z);
                Location loc2 = player.getLocation().add(x2, y[0], z2);
                new ParticleBuilder(particleEffect, loc).setParticleData(new BlockTexture(Material.BLUE_ICE)).display();
                new ParticleBuilder(particleEffect, loc2).setParticleData(new BlockTexture(Material.BLUE_ICE)).display();
                degree[0] += 5;
            }
        }.runTaskTimer(Moderation.getInstance(), 0L, 1L);

    }

}
