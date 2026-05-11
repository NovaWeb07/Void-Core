package com.allro.voidvanguard.client;

import com.allro.voidvanguard.VoidVanguard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VoidVanguard.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class VoidRenderHandler {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SubscribeEvent
    public static void onPreRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;

        if (!ClientVoidData.isTransformed(entity.getId()))
            return;

        event.setCanceled(true);

        EntityRenderer<? super LivingEntity> entityRenderer = (EntityRenderer<? super LivingEntity>) event.getRenderer();
        if (!(entityRenderer instanceof LivingEntityRenderer renderer))
            return;

        EntityModel model = renderer.getModel();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        PoseStack poseStack = event.getPoseStack();

        float partialTicks = event.getPartialTick();
        float time = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
        ResourceLocation beamTex = new ResourceLocation("minecraft", "textures/entity/end_gateway_beam.png");

        float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float limbSwingAmount = entity.walkAnimation.speed(partialTicks);
        float limbSwing = entity.walkAnimation.position(partialTicks);
        float ageInTicks = (float) entity.tickCount + partialTicks;
        float netHeadYaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot) - bodyYaw;
        float headPitch = entity.getXRot();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        if (entity.isBaby()) {
            poseStack.translate(0.0D, -1.501D, 0.0D);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.0D, 1.501D, 0.0D);
        }

        poseStack.translate(0.0D, -1.501D, 0.0D);

        model.riding = entity.isPassenger();
        model.young = entity.isBaby();
        model.attackTime = entity.getAttackAnim(partialTicks);

        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        RenderType depthType = InvertedSwirlRenderType.buildDepthOnly(beamTex);
        VertexConsumer depthBuffer = bufferSource.getBuffer(depthType);
        model.renderToBuffer(poseStack, depthBuffer, 15728880, OverlayTexture.NO_OVERLAY, 0.0F, 0.0F, 0.0F, 0.0F);
        if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch(depthType);

        RenderType swirlType = InvertedSwirlRenderType.buildInvertedSwirl(beamTex, time * 0.01F, time * 0.01F);
        VertexConsumer outlineBuffer = bufferSource.getBuffer(swirlType);
        poseStack.pushPose();
        poseStack.scale(1.015F, 1.015F, 1.015F);
        model.renderToBuffer(poseStack, outlineBuffer, 15728880, OverlayTexture.NO_OVERLAY, 0.5F, 0.0F, 0.8F, 1.0F);
        poseStack.popPose();
        if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch(swirlType);

        RenderType portalType = RenderType.endPortal();
        VertexConsumer portalBuffer = bufferSource.getBuffer(portalType);
        model.renderToBuffer(poseStack, portalBuffer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch(portalType);

        poseStack.popPose();

        if (bufferSource instanceof MultiBufferSource.BufferSource bs) bs.endBatch();
    }
}
