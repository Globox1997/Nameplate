package net.nameplate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.nameplate.util.NameplateRender;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

@Environment(EnvType.CLIENT)
@SuppressWarnings("rawtypes")
@Mixin(GeoReplacedEntityRenderer.class)
public abstract class GeoReplacedEntityRendererMixin extends EntityRenderer {

    @Shadow
    protected Entity currentEntity;

    public GeoReplacedEntityRendererMixin(Context ctx) {
        super(ctx);
    }

    @Inject(method = "actuallyRender", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/mob/MobEntity;getHoldingEntity()Lnet/minecraft/entity/Entity;"))
    private void renderMixin(MatrixStack poseStack, GeoAnimatable animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer,
            boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        NameplateRender.renderNameplate(this, (MobEntity) currentEntity, poseStack, bufferSource, dispatcher, this.getTextRenderer(), isVisible((MobEntity) currentEntity), packedLight);
    }

    @Inject(method = "hasLabel", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    protected void hasLabelMixin(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof MobEntity) {
            info.setReturnValue(false);
        }
    }

    @Shadow(remap = false)
    protected boolean isVisible(LivingEntity livingEntityIn) {
        return false;
    }
}
