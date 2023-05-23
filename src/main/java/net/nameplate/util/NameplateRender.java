package net.nameplate.util;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.nameplate.NameplateMain;
import net.nameplate.access.MobEntityAccess;

@Environment(EnvType.CLIENT)
public class NameplateRender {

    private static final Identifier ICONS = new Identifier("nameplate:textures/icons.png");

    public static void renderNameplate(EntityRenderer<?> entityRenderer, MobEntity mobEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, EntityRenderDispatcher dispatcher,
            TextRenderer textRenderer, boolean isVisible, int i) {
        if (MinecraftClient.isHudEnabled() && NameplateMain.CONFIG.showLevel && dispatcher.getSquaredDistanceToCamera(mobEntity) <= NameplateMain.CONFIG.squaredDistance && !mobEntity.hasPassengers())
            if (isVisible && ((MobEntityAccess) mobEntity).showMobRpgLabel()) {
                if (!NameplateMain.CONFIG.showNameplateIfObstructed && !MinecraftClient.getInstance().player.canSee(mobEntity)) {
                    return;
                }
                matrices.push();
                matrices.translate(0.0D, (double) mobEntity.getHeight() + NameplateMain.CONFIG.nameplateHeight, 0.0D);
                matrices.multiply(dispatcher.getRotation());
                matrices.scale(NameplateMain.CONFIG.nameplateSize, NameplateMain.CONFIG.nameplateSize, 0.025F);

                if (NameplateMain.CONFIG.healthBar) {
                    matrices.push();
                    matrices.scale(1.5f, 1.5f, 1f);

                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderSystem.enableBlend();

                    RenderSystem.defaultBlendFunc();
                    RenderSystem.enableDepthTest();

                    RenderSystem.enablePolygonOffset();
                    RenderSystem.polygonOffset(3.0F, 3.0F);

                    RenderSystem.setShaderTexture(0, ICONS);

                    DrawableHelper.drawTexture(matrices, -20, 0, 0, 0, 40, 6, 256, 256);

                    float health = mobEntity.getHealth() / mobEntity.getMaxHealth();
                    matrices.translate(0.0D, 0.0D, -0.01D);
                    DrawableHelper.drawTexture(matrices, -20, 0, 0, 6, Math.round(40 * health), 6, 256, 256);

                    RenderSystem.polygonOffset(0.0F, 0.0F);
                    RenderSystem.disablePolygonOffset();

                    matrices.pop();
                    matrices.translate(0.0D, -9D, 0.0D);
                    RenderSystem.disableBlend();

                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                }

                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float o = dispatcher.gameOptions.getTextBackgroundOpacity(NameplateMain.CONFIG.backgroundOpacity);
                int j = (int) (o * 255.0F) << 24;
                String string = mobEntity.hasCustomName() ? mobEntity.getCustomName().getString() : mobEntity.getName().getString();
                if (NameplateMain.CONFIG.showHealth) {
                    string = string + " " + Text.translatable("text.nameplate.health", Math.round(mobEntity.getHealth()), Math.round(mobEntity.getMaxHealth())).getString();
                }
                // string = new TranslatableText("text.nameplate.level", Math.round(mobEntity.getMaxHealth() / Nameplate.CONFIG.levelDivider)).getString() + string;
                string = Text.translatable("text.nameplate.level", ((MobEntityAccess) mobEntity).getMobRpgLevel()).getString() + string;

                Text text = Text.of(string);
                float h = (float) (-textRenderer.getWidth(text) / 2);
                textRenderer.draw(text, h, 0.0F, NameplateMain.CONFIG.nameColor, false, matrix4f, vertexConsumers, true, j, i);
                textRenderer.draw(text, h, 0.0F, NameplateMain.CONFIG.backgroundColor, false, matrix4f, vertexConsumers, false, 0, i);
                matrices.pop();
            }
    }
}
