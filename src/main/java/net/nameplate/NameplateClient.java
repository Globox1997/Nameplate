package net.nameplate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.nameplate.network.NameplateClientPacket;

@Environment(EnvType.CLIENT)
public class NameplateClient implements ClientModInitializer {

    public static final Identifier MOB_LEVEL_INFO = new Identifier("nameplate", "mob_level_info");

    @Override
    public void onInitializeClient() {
        NameplateClientPacket.init();
    }

}
