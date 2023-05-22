package net.nameplate.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nameplate.NameplateMain;
import net.nameplate.access.MobEntityAccess;
import net.nameplate.network.NameplateServerPacket;
import net.rpgdifficulty.access.EntityAccess;

public class NameplateTracker {

    public static void startTracking(MobEntity mobEntity, ServerPlayerEntity serverPlayer) {
        // Send packet if entity should show
        if (((MobEntityAccess) mobEntity).showMobRpgLabel()) {
            int level = getMobLevel(mobEntity);

            ((MobEntityAccess) mobEntity).setMobRpgLevel(level);

            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeVarInt(level);
            data.writeVarInt(mobEntity.getId());
            data.writeBoolean(((MobEntityAccess) mobEntity).showMobRpgLabel());
            ServerPlayNetworking.send(serverPlayer, NameplateServerPacket.SET_MOB_LEVEL, new PacketByteBuf(data));
        }
    }

    public static int getMobLevel(MobEntity mobEntity) {
        int level = 1;
        if (NameplateMain.isRpgDifficultyLoaded) {
            level = (int) (NameplateMain.CONFIG.levelMultiplier * ((EntityAccess) mobEntity).getMobHealthMultiplier() - NameplateMain.CONFIG.levelMultiplier);

        } else if (DefaultAttributeRegistryAccessor.getRegistry().get(mobEntity.getType()) != null) {
            level = (int) (NameplateMain.CONFIG.levelMultiplier * mobEntity.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH)
                    / Math.abs(DefaultAttributeRegistryAccessor.getRegistry().get(mobEntity.getType()).getBaseValue(EntityAttributes.GENERIC_MAX_HEALTH))) - NameplateMain.CONFIG.levelMultiplier + 1;
        }
        if (level < 1) {
            level = 1;
        }
        return level;
    }
}
