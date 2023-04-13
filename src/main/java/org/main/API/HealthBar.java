package org.main.API;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HealthBar implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            updateHealthBar(livingEntity);
        }
    }

    public void updateHealthBar(LivingEntity livingEntity) {
        double healthPercentage = livingEntity.getHealth() / livingEntity.getMaxHealth();
        String displayName = String.format("HP: %d/%d", (int) livingEntity.getHealth(), (int) livingEntity.getMaxHealth());
        livingEntity.setCustomName(displayName);
        livingEntity.setCustomNameVisible(true);
    }
}
