package org.main.API;

import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;

public class HealthBar implements Listener {

    public void updateHealthBar(Zombie zombie) {
        double healthPercentage = zombie.getHealth() / zombie.getMaxHealth();
        String displayName = String.format("HP: %d/%d", (int) zombie.getHealth(), (int) zombie.getMaxHealth());
        zombie.setCustomName(displayName);
        zombie.setCustomNameVisible(true);
    }

}
