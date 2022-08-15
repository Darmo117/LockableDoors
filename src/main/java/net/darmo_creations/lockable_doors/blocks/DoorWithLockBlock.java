package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.block_entities.BlockWithLockBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A door block that can be locked.
 */
public class DoorWithLockBlock extends DoorBlock implements BlockWithLock, BlockEntityProvider {
  public DoorWithLockBlock(final DoorBlock baseBlock) {
    super(Settings.copy(baseBlock));
    this.setDefaultState(this.getDefaultState().with(HAS_LOCK, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.add(HAS_LOCK));
  }

  @Override
  public void setHasLockProperty(World world, BlockPos pos, BlockState state, boolean hasLock) {
    BlockWithLock.super.setHasLockProperty(world, pos, state, hasLock);
    BlockPos otherPos;
    if (state.get(HALF) == DoubleBlockHalf.UPPER) {
      otherPos = pos.down();
    } else {
      otherPos = pos.up();
    }
    BlockState otherState = world.getBlockState(otherPos);
    if (otherState.isOf(this) && otherState.get(HALF) != state.get(HALF)) {
      world.setBlockState(otherPos, otherState.with(HAS_LOCK, hasLock));
    }
  }

  @Override
  public Optional<BlockWithLockBlockEntity> getBlockEntity(World world, BlockPos pos) {
    BlockState state = world.getBlockState(pos);
    if (state.get(HALF) == DoubleBlockHalf.UPPER) {
      pos = pos.down();
    }
    return BlockWithLock.super.getBlockEntity(world, pos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
    if (state.get(HALF) == DoubleBlockHalf.LOWER) {
      return BlockWithLock.super.getDroppedStacks(state, builder);
    }
    return Collections.emptyList();
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
  public void setOpen(Entity entity, World world, BlockState state, BlockPos pos, boolean open) {
    if (!this.isLocked(world, pos)) {
      super.setOpen(entity, world, state, pos, open);
    }
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new BlockWithLockBlockEntity(pos, state);
  }
}
