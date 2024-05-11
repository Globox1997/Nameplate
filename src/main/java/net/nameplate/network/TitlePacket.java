package net.nameplate.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TitlePacket(int level) implements CustomPayload {

    public static final CustomPayload.Id<TitlePacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("nameplate", "title_packet"));

    public static final PacketCodec<RegistryByteBuf, TitlePacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.level);
    }, buf -> new TitlePacket(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
