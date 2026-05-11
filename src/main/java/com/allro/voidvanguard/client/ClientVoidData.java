package com.allro.voidvanguard.client;

import java.util.HashSet;
import java.util.Set;

public class ClientVoidData {
    private static final Set<Integer> TRANSFORMED_ENTITIES = new HashSet<>();

    public static void setTransformed(int entityId, boolean transformed) {
        if (transformed) {
            TRANSFORMED_ENTITIES.add(entityId);
        } else {
            TRANSFORMED_ENTITIES.remove(entityId);
        }
    }

    public static boolean isTransformed(int entityId) {
        return TRANSFORMED_ENTITIES.contains(entityId);
    }
}
