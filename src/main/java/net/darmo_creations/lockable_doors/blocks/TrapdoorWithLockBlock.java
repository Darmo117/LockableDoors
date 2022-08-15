package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.block_entities.BlockWithLockBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

// TODO check waterloggability is not impacted when locked

/**
 * A trapdoor block that can be locked.
 */
public class TrapdoorWithLockBlock extends TrapdoorBlock implements BlockWithLock, BlockEntityProvider {
  public TrapdoorWithLockBlock(final TrapdoorBlock baseBlock) {
    super(Settings.copy(baseBlock));
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
    return BlockWithLock.super.getDroppedStacks(state, builder);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
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
    return new BlockWithLockBlockEntity(pos, state);
  }
}
