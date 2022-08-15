package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.block_entities.BlockWithLockBlockEntity;
import net.darmo_creations.lockable_doors.blocks.BlockWithLock;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.darmo_creations.lockable_doors.lock_system.LockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class LockItem extends Item {
  private static final String LOCK_DATA_KEY = "LockData";

  public LockItem(Settings settings) {
    super(settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
    boolean keyed = this.getData(stack).isPresent();
    MutableText text = new TranslatableText("item.lockable_doors.lock.tooltip." + (keyed ? "keyed" : "blank"));
    text.setStyle(Style.EMPTY.withColor(keyed ? Formatting.GREEN : Formatting.GRAY));
    tooltip.add(text);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    Optional<LockData> lockData = this.getData(context.getStack());
    World world = context.getWorld();
    BlockPos blockPos = context.getBlockPos();
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResult.FAIL;
    }
    BlockState blockState = world.getBlockState(blockPos);
    Block block = blockState.getBlock();
    if (block instanceof BlockWithLock) {
      this.notifyPlayer(player, "lockable_doors.message.lock_already_present");
    }
    Block lockableBlock = LockRegistry.getLockableBlock(block);
    if (lockableBlock == null) {
      return ActionResult.FAIL;
    }
    if (lockData.isEmpty()) {
      this.notifyPlayer(player, "lockable_doors.message.blank_lock");
      return ActionResult.FAIL;
    }
    if (!player.isCreative()) {
      context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
    }
    world.setBlockState(blockPos, lockableBlock.getStateWithProperties(blockState));
    // FIXME error thrown
    BlockWithLockBlockEntity be = ((BlockWithLock) lockableBlock).getBlockEntity(world, blockPos)
        .orElseThrow(IllegalStateException::new);
    be.setLockData(lockData.get());
    world.playSound(null, blockPos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
    return super.useOnBlock(context);
  }

  public Optional<LockData> getData(final ItemStack stack) {
    NbtCompound nbt = stack.getNbt();
    if (nbt != null && nbt.contains(LOCK_DATA_KEY, NbtElement.COMPOUND_TYPE)) {
      return Optional.of(new LockData(nbt.getCompound(LOCK_DATA_KEY)));
    }
    return Optional.empty();
  }

  public void setData(ItemStack stack, final LockData data) {
    if (data != null) {
      NbtCompound nbt = new NbtCompound();
      nbt.put(LOCK_DATA_KEY, data.writeToNBT());
      stack.setNbt(nbt);
    } else {
      stack.setNbt(null);
    }
  }

  protected void notifyPlayer(PlayerEntity player, final String s) {
    MutableText message = new TranslatableText(s).setStyle(Style.EMPTY.withColor(Formatting.RED));
    player.sendMessage(message, true);
  }
}
