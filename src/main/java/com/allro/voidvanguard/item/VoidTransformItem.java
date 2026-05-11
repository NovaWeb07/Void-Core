package com.allro.voidvanguard.item;

import com.allro.voidvanguard.network.ModMessages;
import com.allro.voidvanguard.network.S2CSyncVoidTransformPacket;
import com.allro.voidvanguard.server.ServerVoidData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VoidTransformItem extends Item {

    public VoidTransformItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget,
                                                  InteractionHand hand) {
        if (!player.level().isClientSide()) {
            boolean newState = ServerVoidData.toggleVoidMode(interactionTarget.getUUID());

            if (newState) {
                player.displayClientMessage(
                        Component.literal("Void Mode: ").append(Component.literal("ON").withStyle(ChatFormatting.GREEN)),
                        true);
                ((ServerLevel) player.level()).sendParticles(ParticleTypes.REVERSE_PORTAL,
                        interactionTarget.getX(), interactionTarget.getY() + 1, interactionTarget.getZ(),
                        20, 0.5, 0.5, 0.5, 0.1);
            } else {
                player.displayClientMessage(
                        Component.literal("Void Mode: ").append(Component.literal("OFF").withStyle(ChatFormatting.RED)),
                        true);
            }
            ModMessages.sendToClientsTrackingEntity(new S2CSyncVoidTransformPacket(interactionTarget.getId(), newState),
                    interactionTarget);

            if (interactionTarget instanceof Player targetPlayer) {
                ModMessages.sendToPlayer(new S2CSyncVoidTransformPacket(interactionTarget.getId(), newState), (net.minecraft.server.level.ServerPlayer) targetPlayer);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.SUCCESS;
    }
}
