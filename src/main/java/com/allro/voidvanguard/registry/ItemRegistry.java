package com.allro.voidvanguard.registry;

import com.allro.voidvanguard.VoidVanguard;
import com.allro.voidvanguard.item.VoidArmorItem;
import com.allro.voidvanguard.item.VoidSuitItem;
import com.allro.voidvanguard.item.VoidTransformItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,
            VoidVanguard.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, VoidVanguard.MODID);

    public static final RegistryObject<Item> VOID_HELMET = REGISTER.register("void_helmet",
            () -> new VoidArmorItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VOID_CHESTPLATE = REGISTER.register("void_chestplate",
            () -> new VoidArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> VOID_LEGGINGS = REGISTER.register("void_leggings",
            () -> new VoidArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> VOID_BOOTS = REGISTER.register("void_boots",
            () -> new VoidArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> VOID_SUIT = REGISTER.register("void_suit",
            () -> new VoidSuitItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> VOID_SUIT_HELMET = REGISTER.register("void_suit_helmet",
            () -> new VoidSuitItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VOID_SUIT_LEGGINGS = REGISTER.register("void_suit_leggings",
            () -> new VoidSuitItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> VOID_SUIT_BOOTS = REGISTER.register("void_suit_boots",
            () -> new VoidSuitItem(ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> VOID_TRANSFORM_ITEM = REGISTER.register("void_transform_item",
            () -> new VoidTransformItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("void_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> VOID_HELMET.get().getDefaultInstance())
                    .title(Component.translatable("itemGroup.voidvanguard"))
                    .displayItems((parameters, pOutput) -> {
                        pOutput.accept(VOID_HELMET.get());
                        pOutput.accept(VOID_CHESTPLATE.get());
                        pOutput.accept(VOID_LEGGINGS.get());
                        pOutput.accept(VOID_BOOTS.get());
                        pOutput.accept(VOID_SUIT.get());
                        pOutput.accept(VOID_SUIT_HELMET.get());
                        pOutput.accept(VOID_SUIT_LEGGINGS.get());
                        pOutput.accept(VOID_SUIT_BOOTS.get());
                        pOutput.accept(VOID_TRANSFORM_ITEM.get());
                    })
                    .build());
}
