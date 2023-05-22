package net.nameplate.network;

import com.yungnickyoung.minecraft.travelerstitles.TravelersTitlesCommon;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.text.Text;
import net.nameplate.access.MobEntityAccess;

public class NameplateClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(NameplateServerPacket.SET_MOB_LEVEL, (client, handler, buffer, responseSender) -> {
            int mobLevel = buffer.readVarInt();
            int mobId = buffer.readVarInt();
            boolean hasRpgLabel = buffer.readBoolean();
            client.execute(() -> {
                if (client.world.getEntityById(mobId) != null) {
                    ((MobEntityAccess) (MobEntity) client.world.getEntityById(mobId)).setMobRpgLevel(mobLevel);
                    ((MobEntityAccess) (MobEntity) client.world.getEntityById(mobId)).setShowMobRpgLabel(hasRpgLabel);
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(NameplateServerPacket.TITLE_SC_COMPAT, (client, handler, buffer, responseSender) -> {
            int mobLevel = buffer.readInt();
            client.execute(() -> {
                if (TravelersTitlesCommon.titleManager.biomeTitleRenderer.displayedTitle != null) {
                    TravelersTitlesCommon.titleManager.biomeTitleRenderer.displayTitle(TravelersTitlesCommon.titleManager.biomeTitleRenderer.displayedTitle,
                            Text.translatable("text.rpgdifficulty.title", mobLevel));
                }
            });
        });
    }

    public static void writeC2STravelerCompatPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(NameplateServerPacket.TITLE_CS_COMPAT, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
    }

}
