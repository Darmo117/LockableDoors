package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.blocks.LockableBlock;
import net.minecraft.block.Block;
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
import java.util.Objects;
import java.util.Optional;

/**
 * This item is used to lock/unlock a {@link LockableBlock} equipped with a lock.
 */
public class KeyItem extends Item {
  private static final String KEY_DATA_KEY = "KeyData";

  public KeyItem(Settings settings) {
    super(settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
    Optional<String> data = this.getData(stack);
    MutableText text;
    if (data.isEmpty()) {
      text = new TranslatableText("item.lockable_doors.key.tooltip.blank")
          .setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    } else {
      text = new TranslatableText("item.lockable_doors.key.tooltip.name", data.get())
          .setStyle(Style.EMPTY.withColor(Formatting.GREEN));
    }
    tooltip.add(text);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    Block block = world.getBlockState(pos).getBlock();
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResult.FAIL;
    }
    if (block instanceof LockableBlock bwl) {
      if (!bwl.hasLock(world, pos)) {
        this.notifyErrorToPlayer(world, pos, player, "lockable_doors.message.no_lock");
        return ActionResult.FAIL;
      }
      ItemStack stack = context.getStack();
      boolean ok;
      boolean wasLocked = bwl.isLocked(world, pos);
      if (wasLocked) {
        ok = bwl.tryUnlock(world, pos, stack);
      } else {
        ok = bwl.tryLock(world, pos, stack);
      }
      if (ok) {
        this.notifySuccessToPlayer(world, pos, player, !wasLocked);
        return ActionResult.SUCCESS;
      } else {
        this.notifyErrorToPlayer(world, pos, player, "lockable_doors.message.wrong_key");
      }
    }
    return ActionResult.FAIL;
  }

  /**
   * Returns the key data from the given stack.
   *
   * @param stack The stack.
   * @return The key’s data or an empty value if the stack does not contain any valid data.
   */
  public Optional<String> getData(final ItemStack stack) {
    NbtCompound nbt = stack.getNbt();
    if (nbt != null && nbt.contains(KEY_DATA_KEY, NbtElement.STRING_TYPE)) {
      return Optional.of(nbt.getString(KEY_DATA_KEY));
    }
    return Optional.empty();
  }

  /**
   * Insters the given key data into a stack.
   *
   * @param stack The stack.
   * @param data  The data to insert.
   */
  public void setData(ItemStack stack, final String data) {
    if (stack.getItem() != this) {
      return;
    }
    Objects.requireNonNull(data);
    NbtCompound nbt = new NbtCompound();
    nbt.putString(KEY_DATA_KEY, data);
    stack.setNbt(nbt);
  }

  /**
   * Notifies the given player the block has been locked/unlocked and plays a sound effect.
   *
   * @param world  The world.
   * @param pos    The block’s position.
   * @param player The player to notify.
   * @param locked Whether the block was just locked or not.
   */
  protected void notifySuccessToPlayer(World world, final BlockPos pos, PlayerEntity player, final boolean locked) {
    world.playSound(null, pos, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1, 1);
    MutableText message = new TranslatableText("lockable_doors.message.successfully_" + (locked ? "locked" : "unlocked"));
    player.sendMessage(message, true);
  }

  /**
   * Notifies the given player an error has occured and plays a sound effect.
   *
   * @param world   The world.
   * @param pos     The block’s position.
   * @param player  The player to notify.
   * @param message The message’s translation key.
   */
  protected void notifyErrorToPlayer(World world, final BlockPos pos, PlayerEntity player, final String message) {
    world.playSound(null, pos, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1, 1);
    player.sendMessage(new TranslatableText(message).setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
  }
}
