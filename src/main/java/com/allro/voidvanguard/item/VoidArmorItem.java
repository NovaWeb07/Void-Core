package com.allro.voidvanguard.item;

import com.allro.voidvanguard.VoidVanguard;
import com.allro.voidvanguard.client.ClientEvents;
import com.allro.voidvanguard.client.VoidArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class VoidArmorItem extends ArmorItem {

    public VoidArmorItem(Type type, Properties properties) {
        super(ArmorMaterials.NETHERITE, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return VoidVanguard.MODID + ":textures/models/armor/dummy.png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private VoidArmorModel model;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                          EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                if (this.model == null) {
                    this.model = new VoidArmorModel(Minecraft.getInstance().getEntityModels().bakeLayer(ClientEvents.VOID_ARMOR));
                }

                model.young = _default.young;
                model.crouching = _default.crouching;
                model.riding = _default.riding;
                model.rightArmPose = _default.rightArmPose;
                model.leftArmPose = _default.leftArmPose;

                model.setAllVisible(false);

                model.torso.visible = false;
                model.belt.visible = false;
                model.rightUpperLeg.visible = false;
                model.rightLowerLeg.visible = false;
                model.leftUpperLeg.visible = false;
                model.leftLowerLeg.visible = false;

                switch (armorSlot) {
                    case HEAD:
                        model.head.visible = true;
                        model.hat.visible = true;
                        break;
                    case CHEST:
                        model.body.visible = true;
                        model.torso.visible = true;
                        model.rightArm.visible = true;
                        model.leftArm.visible = true;
                        break;
                    case LEGS:
                        model.body.visible = true;
                        model.belt.visible = true;
                        model.rightLeg.visible = true;
                        model.leftLeg.visible = true;
                        model.rightUpperLeg.visible = true;
                        model.leftUpperLeg.visible = true;
                        break;
                    case FEET:
                        model.rightLeg.visible = true;
                        model.leftLeg.visible = true;
                        model.rightLowerLeg.visible = true;
                        model.leftLowerLeg.visible = true;
                        break;
                    default:
                        break;
                }

                return model;
            }
        });
    }
}
