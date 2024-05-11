package net.nameplate.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import net.nameplate.NameplateMain;
import net.nameplate.util.NameplateTracker;
import net.rpgdifficulty.api.MobStrengthener;

public class NameplateServerPacket {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(TitlePacket.PACKET_ID, TitlePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TitlePacket.PACKET_ID, TitlePacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(LevelPacket.PACKET_ID, LevelPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TitlePacket.PACKET_ID, (payload, context) -> {
            payload.level();
            context.player().server.execute(() -> {
                SkeletonEntity skeletonEntity = EntityType.SKELETON.create(context.player().getWorld());
                skeletonEntity.refreshPositionAndAngles(context.player().getX(), context.player().getY(), context.player().getZ(), 0.0f, 0.0f);
                if (NameplateMain.isRpgDifficultyLoaded) {
                    MobStrengthener.changeAttributes(skeletonEntity, context.player().getWorld());
                }
                ServerPlayNetworking.send(context.player(), new TitlePacket(NameplateTracker.getMobLevel(skeletonEntity)));
                skeletonEntity.discard();
            });
        });
    }

}
