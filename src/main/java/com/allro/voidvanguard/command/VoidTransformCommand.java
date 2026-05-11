package com.allro.voidvanguard.command;

import com.allro.voidvanguard.network.ModMessages;
import com.allro.voidvanguard.network.S2CSyncVoidTransformPacket;
import com.allro.voidvanguard.server.ServerVoidData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class VoidTransformCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("void-transform")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    boolean isTransformed = ServerVoidData.toggleVoidMode(player.getUUID());
                    
                    ModMessages.sendToClientsTrackingEntity(new S2CSyncVoidTransformPacket(player.getId(), isTransformed), player);
                    ModMessages.sendToPlayer(new S2CSyncVoidTransformPacket(player.getId(), isTransformed), player);
                    
                    String message = isTransformed ? "Void transformation enabled!" : "Void transformation disabled!";
                    player.sendSystemMessage(Component.literal(message));
                    
                    return 1;
                }));
    }
}
