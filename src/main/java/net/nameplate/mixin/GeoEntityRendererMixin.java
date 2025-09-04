package net.nameplate.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.nameplate.util.NameplateRender;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.ClientUtil;

@Environment(EnvType.CLIENT)
@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends Entity & GeoAnimatable> extends EntityRenderer<T> implements GeoRenderer<T> {

    public GeoEntityRendererMixin(Context ctx) {
        super(ctx);
    }

    @Inject(method = "renderFinal", at = @At("HEAD"), remap = false)
    private void renderFinalMixin(MatrixStack poseStack, T animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour, CallbackInfo info) {
        if (animatable instanceof MobEntity) {
            NameplateRender.renderNameplate(this, (MobEntity) animatable, poseStack, bufferSource, dispatcher, this.getTextRenderer(), animatable == null || !animatable.isInvisibleTo(ClientUtil.getClientPlayer()), packedLight);
        }
    }


    @Inject(method = "hasLabel", at = @At("RETURN"), cancellable = true)
    private void hasLabelMixin(T entity, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue() && entity instanceof MobEntity) {
            info.setReturnValue(false);
        }
    }

}
