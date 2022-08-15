package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.blocks.BlockWithLock;
import net.darmo_creations.lockable_doors.lock_system.LockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LockRemoverItem extends Item {
  public LockRemoverItem(Settings settings) {
    super(settings);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    World world = context.getWorld();
    BlockPos blockPos = context.getBlockPos();
    BlockState blockState = world.getBlockState(blockPos);
    Block block = blockState.getBlock();
    if (block instanceof BlockWithLock bwl) {
      Block baseBlock = LockRegistry.getBaseBlock(block);
      if (baseBlock == null) {
        return ActionResult.FAIL;
      }
      if (bwl.isLocked(world, blockPos)) {
        //noinspection ConstantConditions
        this.notifyLocked(context.getPlayer());
        return ActionResult.FAIL;
      }
      PlayerEntity player = context.getPlayer();
      if (!world.isClient()) {
        //noinspection ConstantConditions
        if (!player.isCreative()) {
          context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
        }
        world.setBlockState(blockPos, baseBlock.getStateWithProperties(blockState));
        world.playSound(null, blockPos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
      }
      return ActionResult.SUCCESS;
    }
    return ActionResult.FAIL;
  }

  protected void notifyLocked(PlayerEntity player) {
    MutableText message = new TranslatableText("lockable_doors.message.cannot_remove_while_locked")
        .setStyle(Style.EMPTY.withColor(Formatting.RED));
    player.sendMessage(message, true);
  }
}
