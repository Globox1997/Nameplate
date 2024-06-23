package net.nameplate.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LevelPacket(int mobLevel, int mobId, boolean hasRpgLabel) implements CustomPayload {

    public static final CustomPayload.Id<LevelPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("nameplate", "level_packet"));

    public static final PacketCodec<RegistryByteBuf, LevelPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.mobLevel);
        buf.writeInt(value.mobId);
        buf.writeBoolean(value.hasRpgLabel);
    }, buf -> new LevelPacket(buf.readInt(), buf.readInt(), buf.readBoolean()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
