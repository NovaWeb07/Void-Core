package com.allro.voidvanguard.client;

import com.allro.voidvanguard.VoidVanguard;
import com.allro.voidvanguard.item.VoidArmorItem;
import com.allro.voidvanguard.item.VoidSuitItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VoidVanguard.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VoidFirstPersonRenderHandler {

    private static boolean isWearingVoidChestplate(AbstractClientPlayer player) {
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        return chestStack.getItem() instanceof VoidArmorItem || chestStack.getItem() instanceof VoidSuitItem;
    }

    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        if (ClientVoidData.isTransformed(player.getId()) || isWearingVoidChestplate(player)) {
            event.setCanceled(true);

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource bufferSource = event.getMultiBufferSource();
            int packedLight = event.getPackedLight();
            HumanoidArm arm = event.getArm();
            
            PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
            PlayerModel<AbstractClientPlayer> model = renderer.getModel();
            
            ModelPart armPart = (arm == HumanoidArm.RIGHT) ? model.rightArm : model.leftArm;
            ModelPart sleevePart = (arm == HumanoidArm.RIGHT) ? model.rightSleeve : model.leftSleeve;

            if (armPart == null) return;

            float time = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
            ResourceLocation beamTex = new ResourceLocation("minecraft", "textures/entity/end_gateway_beam.png");
            RenderType depthType = InvertedSwirlRenderType.buildDepthOnly(beamTex);
            RenderType swirlType = InvertedSwirlRenderType.buildInvertedSwirl(beamTex, time * 0.01F, time * 0.01F);
            RenderType portalType = RenderType.endPortal();

            float aXR = armPart.xRot, aYR = armPart.yRot, aZR = armPart.zRot;
            boolean aVis = armPart.visible;
            float sXR = sleevePart.xRot, sYR = sleevePart.yRot, sZR = sleevePart.zRot;
            boolean sVis = sleevePart.visible;

            armPart.xRot = 0; armPart.yRot = 0; armPart.zRot = 0;
            armPart.visible = true;
            sleevePart.xRot = 0; sleevePart.yRot = 0; sleevePart.zRot = 0;
            sleevePart.visible = true;

            VertexConsumer depthBuffer = bufferSource.getBuffer(depthType);
            armPart.render(poseStack, depthBuffer, packedLight, OverlayTexture.NO_OVERLAY, 0, 0, 0, 0);
            sleevePart.render(poseStack, depthBuffer, packedLight, OverlayTexture.NO_OVERLAY, 0, 0, 0, 0);
            if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch(depthType);

            VertexConsumer outlineBuffer = bufferSource.getBuffer(swirlType);
            poseStack.pushPose();
            poseStack.scale(1.025F, 1.025F, 1.025F);
            armPart.render(poseStack, outlineBuffer, packedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.0F, 0.8F, 1.0F);
            sleevePart.render(poseStack, outlineBuffer, packedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.0F, 0.8F, 1.0F);
            poseStack.popPose();
            if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch(swirlType);

            VertexConsumer portalBuffer = bufferSource.getBuffer(portalType);
            armPart.render(poseStack, portalBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            sleevePart.render(poseStack, portalBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch(portalType);

            armPart.xRot = aXR; armPart.yRot = aYR; armPart.zRot = aZR;
            armPart.visible = aVis;
            sleevePart.xRot = sXR; sleevePart.yRot = sYR; sleevePart.zRot = sZR;
            sleevePart.visible = sVis;
        }
    }
}
