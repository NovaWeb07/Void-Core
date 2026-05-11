package com.allro.voidvanguard.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ServerVoidData {
    private static final Set<UUID> TRANSFORMED_PLAYERS = new HashSet<>();

    public static boolean toggleVoidMode(UUID playerUuid) {
        if (TRANSFORMED_PLAYERS.contains(playerUuid)) {
            TRANSFORMED_PLAYERS.remove(playerUuid);
            return false;
        } else {
            TRANSFORMED_PLAYERS.add(playerUuid);
            return true;
        }
    }

    public static boolean isVoidMode(UUID playerUuid) {
        return TRANSFORMED_PLAYERS.contains(playerUuid);
    }
}
