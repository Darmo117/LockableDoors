package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.block_entities.BlockWithLockBlockEntity;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.darmo_creations.lockable_doors.lock_system.LockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A lockable block can be locked and unlocked using a key whose display name matches the block’s lock.
 * <p>
 * The lock is managed by the block’s block entity.
 *
 * @see BlockWithLockBlockEntity
 */
public interface BlockWithLock {
  /**
   * Tries to lock this block with the given key.
   *
   * @param world The world.
   * @param pos   Block’s position.
   * @param stack The key’s item stack.
   * @return True if the block could be locked, false otherwise.
   */
  default boolean tryLock(World world, BlockPos pos, ItemStack stack) {
    return this.getBlockEntity(world, pos).map(be -> be.tryLock(stack)).orElse(false);
  }

  /**
   * Tries to unlock this block with the given key.
   *
   * @param world The world.
   * @param pos   Block’s position.
   * @param stack The key’s item stack.
   * @return True if the block could be unlocked, false otherwise.
   */
  default boolean tryUnlock(World world, BlockPos pos, ItemStack stack) {
    return this.getBlockEntity(world, pos).map(be -> be.tryUnlock(stack)).orElse(false);
  }

  /**
   * Checks whether this block is locked.
   *
   * @param world The world.
   * @param pos   Block’s position.
   * @return True if the block is locked, false if it is not.
   */
  default boolean isLocked(World world, BlockPos pos) {
    return this.getBlockEntity(world, pos).map(BlockWithLockBlockEntity::isLocked).orElse(false);
  }

  /**
   * Returns the block entity at the given position.
   *
   * @param world The world.
   * @param pos   Block entity’s position.
   * @return An optional containing the block entity if it exists and is a {@link BlockWithLockBlockEntity},
   * an empty optional otherwise.
   */
  default Optional<BlockWithLockBlockEntity> getBlockEntity(World world, BlockPos pos) {
    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof BlockWithLockBlockEntity)) {
      return Optional.empty();
    }
    return Optional.of((BlockWithLockBlockEntity) blockEntity);
  }

  /**
   * Returns the item stacks to drop whenever this block is destroyed.
   * <p>
   * All lockable blocks will drop their base block and their lock separately.
   *
   * @param state   The block state.
   * @param builder The loot context builder.
   * @return The list of all stacks to drop.
   */
  default List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
    ItemStack lock = new ItemStack(ModItems.LOCK);
    BlockEntity be = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
    if (be instanceof BlockWithLockBlockEntity b) {
      lock.setNbt(b.getLockData().writeToNBT());
    }
    return Arrays.asList(new ItemStack(LockRegistry.getBaseBlock((Block) this)), lock);
  }

  /**
   * Notifies the given player whenever that they tried to open a locked door without a key.
   *
   * @param world  The world.
   * @param pos    Block’s position.
   * @param player The player to notify.
   */
  default void notifyLocked(World world, BlockPos pos, PlayerEntity player) {
    world.playSound(null, pos, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1, 1);
    MutableText message = new TranslatableText("locked_doors.message.locked_block")
        .setStyle(Style.EMPTY.withColor(Formatting.RED));
    player.sendMessage(message, true);
  }
}
