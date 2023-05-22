package net.nameplate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.nameplate.network.NameplateServerPacket;

public class NameplateMain implements ModInitializer {

    public static final boolean isRpgDifficultyLoaded = FabricLoader.getInstance().isModLoaded("rpgdifficulty");

    @Override
    public void onInitialize() {
        NameplateServerPacket.init();
    }

}
