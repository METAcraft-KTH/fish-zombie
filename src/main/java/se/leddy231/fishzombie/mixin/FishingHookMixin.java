package se.leddy231.fishzombie.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.leddy231.fishzombie.FishZombie;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {

	public FishingHookMixin(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

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
			zombie.finalizeSpawn(
					(ServerLevelAccessor) level, level.getCurrentDifficultyAt(zombie.blockPosition()), MobSpawnType.EVENT,
					new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(level.random), false), null
			);
			zombie.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			zombie.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
			zombie.setTarget(player);
			Vec3 vec3 = new Vec3(player.getX() - this.getX(), player.getY() - this.getY(), player.getZ() - this.getZ()).scale(0.1);
			zombie.setDeltaMovement(zombie.getDeltaMovement().add(vec3));
			level.addFreshEntity(zombie);
		}
	}
}
