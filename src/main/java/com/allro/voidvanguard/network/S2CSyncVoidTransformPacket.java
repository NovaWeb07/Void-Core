package com.allro.voidvanguard.network;

import com.allro.voidvanguard.client.ClientVoidData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSyncVoidTransformPacket {
    private final int entityId;
    private final boolean transformed;

    public S2CSyncVoidTransformPacket(int entityId, boolean transformed) {
        this.entityId = entityId;
        this.transformed = transformed;
    }

    public S2CSyncVoidTransformPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.transformed = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(transformed);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientVoidData.setTransformed(entityId, transformed);
        });
        return true;
    }
}
