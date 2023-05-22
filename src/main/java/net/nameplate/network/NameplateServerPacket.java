package net.nameplate.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.nameplate.util.NameplateTracker;
import net.rpgdifficulty.api.MobStrengthener;

public class NameplateServerPacket {

    public static final Identifier SET_MOB_LEVEL = new Identifier("nameplate", "set_mob_level");
    public static final Identifier TITLE_CS_COMPAT = new Identifier("nameplate", "title_cs_compat");
    public static final Identifier TITLE_SC_COMPAT = new Identifier("nameplate", "title_sc_compat");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(TITLE_CS_COMPAT, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                SkeletonEntity skeletonEntity = EntityType.SKELETON.create(player.world);
                skeletonEntity.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0.0f, 0.0f);
                MobStrengthener.changeAttributes(skeletonEntity, player.world);
                writeS2TravelerCompatPacket(player, NameplateTracker.getMobLevel(skeletonEntity));
                skeletonEntity.discard();
            });
        });
    }

    public static void writeS2TravelerCompatPacket(ServerPlayerEntity serverPlayerEntity, int level) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(level);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(TITLE_SC_COMPAT, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }
}
