package net.nameplate.network;

import com.yungnickyoung.minecraft.travelerstitles.TravelersTitlesCommon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.Text;
import net.nameplate.access.MobEntityAccess;

@Environment(EnvType.CLIENT)
public class NameplateClientPacket {

    @SuppressWarnings("resource")
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TitlePacket.PACKET_ID, (payload, context) -> {
            int mobLevel = payload.level();
            context.client().execute(() -> {
                if (TravelersTitlesCommon.titleManager.biomeTitleRenderer.displayedTitle != null) {
                    TravelersTitlesCommon.titleManager.biomeTitleRenderer.displayTitle(TravelersTitlesCommon.titleManager.biomeTitleRenderer.displayedTitle,
                            Text.translatable("text.rpgdifficulty.title", mobLevel));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(LevelPacket.PACKET_ID, (payload, context) -> {
            int mobLevel = payload.mobLevel();
            int mobId = payload.mobId();
            boolean hasRpgLabel = payload.hasRpgLabel();
            context.client().execute(() -> {
                if (context.client().world.getEntityById(mobId) != null && context.client().world.getEntityById(mobId) instanceof MobEntity mobEntity) {
                    ((MobEntityAccess) mobEntity).setMobRpgLevel(mobLevel);
                    ((MobEntityAccess) mobEntity).setShowMobRpgLabel(hasRpgLabel);
                }
            });
        });
    }

}
