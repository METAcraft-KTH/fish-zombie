package se.leddy231.fishzombie.mixin;

import se.leddy231.fishzombie.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

@Mixin(FishingHook.class)
public class FishingHookMixin {

	@Inject(at = @At("HEAD"), method = "retrieve")
	private void spawnZombie(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		var hook = (FishingHook) (Object) this;
		var player = hook.getPlayerOwner();
		var level = hook.level();
		var accessor = (FishingHookAccessor) this;
		if (level.isClientSide || player == null) {
			return;
		}
		if (accessor.getNibble() > 0 && level.random.nextInt(10) == 0) {
			FishZombie.LOGGER.info(player.getGameProfile().getName() + " hooked a zombie!");
			var zombie = new Drowned(EntityType.DROWNED, level);
			zombie.setPos(hook.getX(), hook.getY(), hook.getZ());
			zombie.setTarget(player);
			level.addFreshEntity(zombie);
		}
	}
}
