package org.main.ranks;

import org.bukkit.entity.Player;

public class RankManager {
    public static Rank getRank(Player player) {
        if (player.hasPermission(Rank.ADMIN.getPermission())) {
            return Rank.ADMIN;
        } else if (player.hasPermission(Rank.MOD.getPermission())) {
            return Rank.MOD;
        } else {
            return Rank.PLAYER;
        }
    }
}
