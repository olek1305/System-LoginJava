package org.main.idle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.main.API.HealthBar;

public class ZombieListener implements Listener {

    private final HealthBar healthBar;
    private final PotionEffect slowPotionEffect;

    public ZombieListener(Plugin plugin) {
        this.healthBar = new HealthBar();
        this.slowPotionEffect = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 127, false, false);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Zombie && event.getDamager() instanceof Player) {
            Zombie zombie = (Zombie) event.getEntity();
            Player player = (Player) event.getDamager();

            event.setDamage(1);
            zombie.setHealth(Math.max(zombie.getHealth() - 1, 0));
            this.healthBar.updateHealthBar(zombie);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event, Plugin plugin) {
        if (!(event.getEntity() instanceof Zombie)) {
            return;
        }

        Zombie zombie = (Zombie) event.getEntity();
        Player player = zombie.getKiller();

        if (player != null) {
            ArcyCoins.giveCoins(player, 1);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location location = zombie.getLocation();
            World world = location.getWorld();
            Zombie newZombie = (Zombie) world.spawnEntity(location, EntityType.ZOMBIE);
            applyZombieAttributes(newZombie);
        }, 200L);

        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler
    public void onZombieTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof Zombie) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SPONGE) {
            Location blockLocation = event.getClickedBlock().getLocation();
            Location spawnLocation = new Location(blockLocation.getWorld(), blockLocation.getX() + 0.5, blockLocation.getY() + 1, blockLocation.getZ() + 0.5);

            Zombie zombie = (Zombie) blockLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
            applyZombieAttributes(zombie);

            ItemStack spawnerItem = event.getPlayer().getInventory().getItemInMainHand();
            if (spawnerItem.getAmount() == 1) {
                event.getPlayer().getInventory().setItemInMainHand(null);
            } else {
                spawnerItem.setAmount(spawnerItem.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onZombieSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            applyZombieAttributes(zombie);
        }
    }

    private void applyZombieAttributes(Zombie zombie) {
        zombie.setAI(false);
        zombie.setCollidable(false);
        zombie.setInvulnerable(true);
        zombie.setCanPickupItems(false);
        zombie.setCanBreakDoors(false);
        zombie.addPotionEffect(slowPotionEffect);
    }
}
