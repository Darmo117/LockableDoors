package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.blocks.LockableBlock;
import net.darmo_creations.lockable_doors.lock_system.LockData;
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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * This item can be applied to a {@link LockableBlock} to enable players to lock/unlock it.
 */
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
    super.appendTooltip(stack, world, tooltip, context);
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    ItemStack thisStack = user.getStackInHand(hand);
    ItemStack otherStack = user.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
    if (otherStack.isOf(ModItems.KEY)) {
      KeyItem.checkKeyAndLockFit(otherStack, thisStack, user);
      return new TypedActionResult<>(ActionResult.PASS, thisStack);
    }
    return new TypedActionResult<>(ActionResult.FAIL, thisStack);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    Optional<LockData> lockData = this.getData(context.getStack());
    World world = context.getWorld();
    BlockPos pos = context.getBlockPos();
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResult.FAIL;
    }
    BlockState blockState = world.getBlockState(pos);
    Block block = blockState.getBlock();
    if (block instanceof LockableBlock bwl) {
      if (bwl.hasLock(world, pos)) {
        this.notifyErrorToPlayer(player, "lockable_doors.message.lock_already_present");
        return ActionResult.FAIL;
      }
      if (lockData.isEmpty()) {
        this.notifyErrorToPlayer(player, "lockable_doors.message.blank_lock");
        return ActionResult.FAIL;
      }
      if (bwl.tryInstallLock(world, pos, lockData.get())) {
        if (!player.isCreative()) {
          context.getStack().decrement(1);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
        return ActionResult.SUCCESS;
      }
    }
    return ActionResult.FAIL;
  }

  /**
   * Returns the lock data from the given stack.
   *
   * @param stack The stack.
   * @return The lock’s data or an empty value if the stack does not contain any valid data.
   */
  public Optional<LockData> getData(final ItemStack stack) {
    NbtCompound nbt = stack.getNbt();
    if (nbt != null && nbt.contains(LOCK_DATA_KEY, NbtElement.COMPOUND_TYPE)) {
      return Optional.of(new LockData(nbt.getCompound(LOCK_DATA_KEY)));
    }
    return Optional.empty();
  }

  /**
   * Insters the given lock data into a stack.
   *
   * @param stack The stack.
   * @param data  The data to insert.
   */
  public void setData(ItemStack stack, final LockData data) {
    if (stack.getItem() != this) {
      return;
    }
    if (data != null) {
      NbtCompound nbt = new NbtCompound();
      nbt.put(LOCK_DATA_KEY, data.writeToNBT());
      stack.setNbt(nbt);
    } else {
      stack.setNbt(null);
    }
  }

  /**
   * Notifies the given player an error has occured.
   *
   * @param player  The player to notify.
   * @param message The message’s translation key.
   */
  protected void notifyErrorToPlayer(PlayerEntity player, final String message) {
    player.sendMessage(new TranslatableText(message).setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
  }
}
