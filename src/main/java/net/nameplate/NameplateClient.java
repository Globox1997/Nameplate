package net.nameplate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.nameplate.config.NameplateConfig;
import net.nameplate.network.NameplateClientPacket;

@Environment(EnvType.CLIENT)
public class NameplateClient implements ClientModInitializer {

    public static NameplateConfig CONFIG = new NameplateConfig();

    public static final Identifier MOB_LEVEL_INFO = new Identifier("nameplate", "mob_level_info");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(NameplateConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NameplateConfig.class).getConfig();
        NameplateClientPacket.init();
    }

}
