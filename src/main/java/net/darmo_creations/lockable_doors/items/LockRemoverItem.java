package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.blocks.LockableBlock;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class LockRemoverItem extends Item {
  public LockRemoverItem(Settings settings) {
    super(settings);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    BlockState blockState = world.getBlockState(pos);
    Block block = blockState.getBlock();
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResult.FAIL;
    }
    if (block instanceof LockableBlock bwl) {
      if (!bwl.hasLock(world, pos)) {
        this.notifyPlayer(player, "lockable_doors.message.no_lock");
        return ActionResult.FAIL;
      }
      if (bwl.isLocked(world, pos)) {
        this.notifyPlayer(context.getPlayer(), "lockable_doors.message.cannot_remove_while_locked");
        return ActionResult.FAIL;
      }
      if (!player.isCreative()) {
        context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
      }
      Optional<LockData> lockData = bwl.tryRemoveLock(world, pos);
      if (lockData.isPresent()) {
        ItemStack stack = new ItemStack(ModItems.LOCK);
        ModItems.LOCK.setData(stack, lockData.get());
        ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
        return ActionResult.SUCCESS;
      }
    }
    return ActionResult.FAIL;
  }

  protected void notifyPlayer(PlayerEntity player, final String message) {
    player.sendMessage(new TranslatableText(message).setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
  }
}
