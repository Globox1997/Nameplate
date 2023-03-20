package net.nameplate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nameplate.util.NameplateTracker;

@Mixin(value = EntityTrackerEntry.class, priority = 1001)
public class EntityTrackerEntryMixin {

    @Shadow
    private final Entity entity;

    public EntityTrackerEntryMixin(Entity entity) {
        this.entity = entity;
    }

    @Inject(method = "startTracking", at = @At(value = "TAIL"))
    public void startTrackingMixin(ServerPlayerEntity serverPlayer, CallbackInfo info) {
        if (entity instanceof MobEntity) {
            NameplateTracker.startTracking((MobEntity) entity, serverPlayer);
        }
    }

}
