package com.allro.voidvanguard.client;

import com.allro.voidvanguard.VoidVanguard;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = VoidVanguard.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HudRenderHandler {

    private static final ResourceLocation ICONS = new ResourceLocation("minecraft:textures/gui/icons.png");
    private static final ResourceLocation END_PORTAL = new ResourceLocation("minecraft:textures/entity/end_portal.png");
    private static final ResourceLocation END_GATEWAY_BEAM = new ResourceLocation("minecraft:textures/entity/end_gateway_beam.png");

    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.HIGHEST)
    public static void onPreRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if (event.getOverlay().id().equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            if (ClientVoidData.isTransformed(player.getId())) {
                if (!player.isCreative() && !player.isSpectator()) {
                    event.setCanceled(true);

                    if (mc.screen == null) {
                        renderCustomHearts(event.getGuiGraphics(), player, event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight());
                    }
                }
            }
        }
    }

    private static void renderCustomHearts(GuiGraphics guiGraphics, Player player, int width, int height) {
        int health = Mth.ceil(player.getHealth());
        int maxHealth = Mth.ceil(player.getMaxHealth());
        int absorption = Mth.ceil(player.getAbsorptionAmount());

        int left = width / 2 - 91;
        int top = height - 39;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 200);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.25f);
        int totalUnits = (maxHealth + absorption + 1) / 2;
        for (int i = 0; i < totalUnits; i++) {
            int x = left + (i % 10) * 8;
            int y = top - (i / 10) * 10;
            boolean isAbsorb = (i * 2) >= maxHealth;
            int u = isAbsorb ? 160 : 16;
            guiGraphics.blit(ICONS, x, y, u, 0, 9, 9);
        }

        long millis = Util.getMillis();
        float tSlow = (millis / 3500.0f) % 1.0f;
        float tFast = (millis / 1100.0f) % 1.0f;
        float tPortal = (millis / 9000.0f) % 1.0f;
        float pulse = (float) Math.sin(millis * 0.003) * 0.04f + 1.05f;

        Matrix4f matrix = guiGraphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        for (int i = 0; i < totalUnits; i++) {
            int x = left + (i % 10) * 8;
            int y = top - (i / 10) * 10;
            
            int heartIdx = i * 2;
            int heartVal = Math.min(2, Math.max(0, health - heartIdx));
            int absorbVal = Math.min(2, Math.max(0, absorption - (heartIdx - maxHealth)));

            boolean isAbsorb = heartIdx >= maxHealth;
            int currentVal = isAbsorb ? absorbVal : heartVal;

            if (currentVal > 0) {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

                int u = (currentVal == 1) ? 61 : 52;
                if (isAbsorb) u = (currentVal == 1) ? 169 : 160;

                RenderSystem.depthFunc(GL11.GL_ALWAYS);
                RenderSystem.colorMask(false, false, false, false);
                guiGraphics.blit(ICONS, x, y, u, 0, 9, 9); 
                RenderSystem.colorMask(true, true, true, true);

                RenderSystem.depthFunc(GL11.GL_EQUAL);
                float cx = x + 4.5f;
                float cy = y + 4.5f;

                RenderSystem.enableBlend();
                RenderSystem.setShaderTexture(0, END_PORTAL);
                RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionTexShader);
                RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                renderLayer(buffer, matrix, cx, cy, 10, 10, 1.35f, 1.0f, 1.0f, 1.0f, 1.0f, tPortal, tPortal, 0.7f);

                RenderSystem.setShaderTexture(0, END_GATEWAY_BEAM);
                RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
                renderLayer(buffer, matrix, cx, cy, 10, 10, pulse, 0.6f, 0.1f, 0.9f, 0.95f, tSlow, 0.0f, 0.5f);
                renderLayer(buffer, matrix, cx, cy, 10, 10, pulse * 1.05f, 0.8f, 0.3f, 1.0f, 0.8f, -tFast, 0.5f, 0.35f);

                RenderSystem.depthFunc(GL11.GL_ALWAYS);
            }
        }

        guiGraphics.pose().popPose();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private static void renderLayer(BufferBuilder buffer, Matrix4f matrix, float cx, float cy, float w, float h,
            float scale, float r, float g, float b, float a, float vOff, float uOff, float uvScale) {
        float hw = (w / 2f) * scale;
        float hh = (h / 2f) * scale;
        RenderSystem.setShaderColor(r, g, b, a);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, cx - hw, cy + hh, 0).uv(uOff, vOff + uvScale).endVertex();
        buffer.vertex(matrix, cx + hw, cy + hh, 0).uv(uOff + uvScale, vOff + uvScale).endVertex();
        buffer.vertex(matrix, cx + hw, cy - hh, 0).uv(uOff + uvScale, vOff).endVertex();
        buffer.vertex(matrix, cx - hw, cy - hh, 0).uv(uOff, vOff).endVertex();
        Tesselator.getInstance().end();
    }
}
