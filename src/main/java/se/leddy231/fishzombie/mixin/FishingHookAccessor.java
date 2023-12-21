package se.leddy231.fishzombie.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.projectile.FishingHook;

@Mixin(FishingHook.class)
public interface FishingHookAccessor {

    @Accessor
    public int getNibble();
}
