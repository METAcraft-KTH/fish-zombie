package se.leddy231.fishzombie.mixin;

import se.leddy231.fishzombie.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public class FishZombieMixin {

	@Inject(at = @At("HEAD"), method = "use")
	private void spawnZombie(CallbackInfoReturnable<Integer> info) {
		FishingBobberEntity e = (FishingBobberEntity) (Object) this;
		FishBobberAccessor accessor = (FishBobberAccessor) this;
		PlayerEntity player = e.getPlayerOwner();
		if (e.world.isClient || player == null) {
            return;
        }
		int countdown = accessor.getHookCountdown();
		if (countdown > 0 && e.world.random.nextInt(10) == 0) {
			FishZombie.LOGGER.info("Someone hooked a zombie!");
			DrownedEntity zombie = new DrownedEntity(EntityType.DROWNED, e.world);
			zombie.setPos(e.getX(), e.getY(), e.getZ());
			zombie.setAttacking(player);
			e.world.spawnEntity(zombie);
		}
	}
}
