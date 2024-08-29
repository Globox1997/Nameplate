package net.nameplate.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.nameplate.util.NameplateRender;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.ClientUtil;

@Environment(EnvType.CLIENT)
@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends Entity & GeoAnimatable> extends EntityRenderer<T> implements GeoRenderer<T> {

    @Shadow
    protected T animatable;

    public GeoEntityRendererMixin(Context ctx) {
        super(ctx);
    }

    @Inject(method = "render", at = @At("HEAD"), remap = false)
    private void renderMixin(T entity, float entityYaw, float partialTick, MatrixStack stack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo info) {
        if (entity instanceof MobEntity) {
            NameplateRender.renderNameplate(this, (MobEntity) entity, stack, bufferSource, dispatcher, this.getTextRenderer(), animatable == null || !animatable.isInvisibleTo(ClientUtil.getClientPlayer()), packedLight);
        }

    }


    @Inject(method = "hasLabel", at = @At("RETURN"), cancellable = true)
    private void hasLabelMixin(T entity, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue() && entity instanceof MobEntity) {
            info.setReturnValue(false);
        }
    }

}
