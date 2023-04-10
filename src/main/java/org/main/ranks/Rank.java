package org.main.ranks;

public enum Rank {
    PLAYER("arcy.player"),
    MOD("arcy.mod"),
    ADMIN("arcy.admin");

    private final String permission;

    Rank(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
