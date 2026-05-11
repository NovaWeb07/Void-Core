package com.allro.voidvanguard.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VoidArmorModel extends HumanoidModel<LivingEntity> {
    public ModelPart torso;
    public ModelPart belt;
    public ModelPart rightUpperLeg;
    public ModelPart rightLowerLeg;
    public ModelPart leftUpperLeg;
    public ModelPart leftLowerLeg;

    public VoidArmorModel(ModelPart root) {
        super(root);
        this.torso = root.getChild("body").getChild("torso");
        this.belt = root.getChild("body").getChild("belt");
        this.rightUpperLeg = root.getChild("right_leg").getChild("upper");
        this.rightLowerLeg = root.getChild("right_leg").getChild("lower");
        this.leftUpperLeg = root.getChild("left_leg").getChild("upper");
        this.leftLowerLeg = root.getChild("left_leg").getChild("lower");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        CubeDeformation gap = new CubeDeformation(0.05F);

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("helmet_top",
                CubeListBuilder.create().addBox(-4.5F, -9.0F, -4.5F, 9.0F, 4.0F, 9.0F, gap),
                PartPose.ZERO);
        head.addOrReplaceChild("helmet_back",
                CubeListBuilder.create().addBox(-4.5F, -5.0F, 3.5F, 9.0F, 6.0F, 1.0F, gap),
                PartPose.ZERO);
        head.addOrReplaceChild("helmet_left",
                CubeListBuilder.create().addBox(3.5F, -5.0F, -4.5F, 1.0F, 6.0F, 8.0F, gap),
                PartPose.ZERO);
        head.addOrReplaceChild("helmet_right",
                CubeListBuilder.create().addBox(-4.5F, -5.0F, -4.5F, 1.0F, 6.0F, 8.0F, gap),
                PartPose.ZERO);
        head.addOrReplaceChild("helmet_front",
                CubeListBuilder.create().addBox(-3.0F, -6.0F, -4.5F, 6.0F, 1.0F, 1.0F, gap),
                PartPose.ZERO);

        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("torso", CubeListBuilder.create()
                .addBox(-4.5F, -0.5F, -2.5F, 9.0F, 9.0F, 5.0F, gap), PartPose.ZERO);
        body.addOrReplaceChild("belt", CubeListBuilder.create()
                .addBox(-4.5F, 8.5F, -2.5F, 9.0F, 4.0F, 5.0F, gap), PartPose.ZERO);

        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .addBox(-3.5F, -2.5F, -2.5F, 5.0F, 6.5F, 5.0F, gap), PartPose.offset(-5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .addBox(-1.5F, -2.5F, -2.5F, 5.0F, 6.5F, 5.0F, gap), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        rightLeg.addOrReplaceChild("upper", CubeListBuilder.create()
                .addBox(-2.5F, -0.5F, -2.5F, 5.0F, 7.0F, 5.0F, gap), PartPose.ZERO);
        rightLeg.addOrReplaceChild("lower", CubeListBuilder.create()
                .addBox(-2.5F, 6.0F, -2.5F, 5.0F, 6.5F, 5.0F, gap), PartPose.ZERO);

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
        leftLeg.addOrReplaceChild("upper", CubeListBuilder.create()
                .addBox(-2.5F, -0.5F, -2.5F, 5.0F, 7.0F, 5.0F, gap), PartPose.ZERO);
        leftLeg.addOrReplaceChild("lower", CubeListBuilder.create()
                .addBox(-2.5F, 6.0F, -2.5F, 5.0F, 6.5F, 5.0F, gap), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha) {

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        float time = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
        ResourceLocation beamTex = new ResourceLocation("minecraft", "textures/entity/end_gateway_beam.png");
        RenderType depthType = InvertedSwirlRenderType.buildDepthOnly(beamTex);
        VertexConsumer depthBuffer = bufferSource.getBuffer(depthType);
        super.renderToBuffer(poseStack, depthBuffer, packedLight, packedOverlay, 0.0F, 0.0F, 0.0F, 0.0F);
        bufferSource.endBatch(depthType);

        RenderType swirlType = InvertedSwirlRenderType.buildInvertedSwirl(beamTex, time * 0.01F, time * 0.01F);
        VertexConsumer outlineBuffer = bufferSource.getBuffer(swirlType);

        poseStack.pushPose();
        poseStack.scale(1.015F, 1.015F, 1.015F);
        super.renderToBuffer(poseStack, outlineBuffer, packedLight, packedOverlay, 0.5F, 0.0F, 0.8F, 1.0F);
        poseStack.popPose();
        bufferSource.endBatch(swirlType);

        RenderType portalType = RenderType.endPortal();
        VertexConsumer portalBuffer = bufferSource.getBuffer(portalType);

        super.renderToBuffer(poseStack, portalBuffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        bufferSource.endBatch(portalType);
    }

}
