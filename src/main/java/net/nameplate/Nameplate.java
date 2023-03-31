package net.nameplate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.nameplate.config.NameplateConfig;
import net.nameplate.network.MobLevelPacket;

public class Nameplate implements ClientModInitializer {

    public static NameplateConfig CONFIG = new NameplateConfig();

    public static final boolean isRpgDifficultyLoaded = FabricLoader.getInstance().isModLoaded("rpgdifficulty");

    public static final Identifier MOB_LEVEL_INFO = new Identifier("nameplate", "mob_level_info");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(NameplateConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NameplateConfig.class).getConfig();
        MobLevelPacket.init();
    }

}
