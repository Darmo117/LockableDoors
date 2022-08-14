package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.blocks.BlockWithLock;
import net.darmo_creations.lockable_doors.lock_system.LockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LockRemoverItem extends Item {
  public LockRemoverItem(Settings settings) {
    super(settings);
  }

  // TODO only remove if allowed
  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    World world = context.getWorld();
    BlockPos blockPos = context.getBlockPos();
    BlockState blockState = world.getBlockState(blockPos);
    Block block = blockState.getBlock();
    if (!(block instanceof BlockWithLock)) {
      return ActionResult.FAIL;
    }
    Block baseBlock = LockRegistry.getBaseBlock(block);
    if (baseBlock == null) {
      return ActionResult.FAIL;
    }
    PlayerEntity player = context.getPlayer();
    //noinspection ConstantConditions
    if (!player.isCreative()) {
      context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
    }
    world.setBlockState(blockPos, baseBlock.getStateWithProperties(blockState));
    return super.useOnBlock(context);
  }
}
