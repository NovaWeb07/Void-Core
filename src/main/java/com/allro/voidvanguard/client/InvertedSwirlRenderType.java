package com.allro.voidvanguard.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class InvertedSwirlRenderType extends RenderType {

    public InvertedSwirlRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
            boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    protected static final RenderStateShard.CullStateShard CULL_FRONT = new RenderStateShard.CullStateShard(true) {
        @Override
        public void setupRenderState() {
            RenderSystem.enableCull();
            org.lwjgl.opengl.GL11.glCullFace(org.lwjgl.opengl.GL11.GL_FRONT);
        }

        @Override
        public void clearRenderState() {
            org.lwjgl.opengl.GL11.glCullFace(org.lwjgl.opengl.GL11.GL_BACK);
            RenderSystem.disableCull();
            RenderSystem.enableCull();
        }
    };

    public static RenderType buildInvertedSwirl(ResourceLocation texture, float u, float v) {
        return RenderType.create("inverted_swirl",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTexturingState(new RenderStateShard.OffsetTexturingStateShard(u, v))
                        .setTransparencyState(ADDITIVE_TRANSPARENCY)
                        .setCullState(CULL_FRONT)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(false));
    }

    protected static final RenderStateShard.WriteMaskStateShard DEPTH_ONLY = new RenderStateShard.WriteMaskStateShard(
            true, true) {
        @Override
        public void setupRenderState() {
            RenderSystem.depthMask(true);
            RenderSystem.colorMask(false, false, false, false);
        }

        @Override
        public void clearRenderState() {
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.depthMask(true);
        }
    };

    public static RenderType buildDepthOnly(ResourceLocation texture) {
        return RenderType.create("depth_only",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setCullState(CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setWriteMaskState(DEPTH_ONLY)
                        .createCompositeState(false));
    }
}
