package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.block_entities.LockableBlockEntity;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.darmo_creations.lockable_doors.mixin.TrapdoorBlockMixin;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A trapdoor block that can be locked.
 */
public class LockableTrapdoorBlock extends TrapdoorBlock implements LockableBlock, BlockEntityProvider {
  public LockableTrapdoorBlock(final TrapdoorBlock baseBlock) {
    super(Settings.copy(baseBlock), ((TrapdoorBlockMixin) baseBlock).getBlockSetType());
    this.setDefaultState(this.getDefaultState().with(HAS_LOCK, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(HAS_LOCK));
  }

  @Override
  public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
    super.onPlaced(world, pos, state, placer, itemStack);
    if (!world.isClient()) {
      world.setBlockState(pos, state.with(HAS_LOCK,
          this.getBlockEntity(world, pos).map(LockableBlockEntity::hasLock).orElse(false)));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
    return LockableBlock.super.getDroppedStacks(state, builder);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    if (this.material == Material.METAL) {
      return ActionResult.PASS;
    }
    ItemStack stackInHand = player.getStackInHand(hand);
    if (stackInHand.isOf(ModItems.KEY) || stackInHand.isOf(ModItems.LOCK) || stackInHand.isOf(ModItems.LOCK_REMOVER)) {
      return ActionResult.PASS;
    }
    if (this.isLocked(world, pos)) {
      this.notifyLocked(world, pos, player);
      return ActionResult.FAIL;
    }
    return super.onUse(state, world, pos, player, hand, hit);
  }

  @Override
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
    if (!this.isLocked(world, pos)) {
      super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new LockableBlockEntity(pos, state);
  }
}
