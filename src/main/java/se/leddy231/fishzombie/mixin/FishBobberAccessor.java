package se.leddy231.fishzombie.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.projectile.FishingBobberEntity;

@Mixin(FishingBobberEntity.class)
public interface FishBobberAccessor {

    @Accessor
    int getHookCountdown();
}
