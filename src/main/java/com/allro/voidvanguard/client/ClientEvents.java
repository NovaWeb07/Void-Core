package com.allro.voidvanguard.client;

import com.allro.voidvanguard.VoidVanguard;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VoidVanguard.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    public static final ModelLayerLocation VOID_ARMOR = new ModelLayerLocation(
            new ResourceLocation(VoidVanguard.MODID, "void_armor"), "main");
    public static final ModelLayerLocation VOID_SUIT = new ModelLayerLocation(
            new ResourceLocation(VoidVanguard.MODID, "void_suit"), "main");

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(VOID_ARMOR, VoidArmorModel::createBodyLayer);
        event.registerLayerDefinition(VOID_SUIT, VoidSuitModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
    }
}
