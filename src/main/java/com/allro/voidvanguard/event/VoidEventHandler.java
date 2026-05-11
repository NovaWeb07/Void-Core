package com.allro.voidvanguard.event;

import com.allro.voidvanguard.VoidVanguard;
import com.allro.voidvanguard.command.VoidTransformCommand;
import com.allro.voidvanguard.network.ModMessages;
import com.allro.voidvanguard.network.S2CSyncVoidTransformPacket;
import com.allro.voidvanguard.server.ServerVoidData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VoidVanguard.MODID)
public class VoidEventHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        VoidTransformCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player targetPlayer) {
            boolean isTransformed = ServerVoidData.isVoidMode(targetPlayer.getUUID());
            if (isTransformed) {
                ModMessages.sendToPlayer(new S2CSyncVoidTransformPacket(targetPlayer.getId(), true), (ServerPlayer) event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        boolean isTransformed = ServerVoidData.isVoidMode(player.getUUID());
        if (isTransformed) {
            ModMessages.sendToPlayer(new S2CSyncVoidTransformPacket(player.getId(), true), player);
        }
    }
}
