package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.block_entities.LockableBlockEntity;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A lockable block can be locked and unlocked using a key whose display name matches the block’s lock.
 * <p>
 * The lock is managed by the block’s block entity.
 *
 * @see LockableBlockEntity
 */
public interface LockableBlock {
  /**
   * This property’s purpose should be reserved to cosmetic uses only.
   */
  BooleanProperty HAS_LOCK = BooleanProperty.of("has_lock");

  /**
   * Tries to install a lock on this block.
   *
   * @param world The world.
   * @param pos   Block’s position.
   * @param data  The lock to install.
   * @return True if the lock could be installed, false otherwise.
   */
  default boolean tryInstallLock(World world, final BlockPos pos, final LockData data) {
    return this.getBlockEntity(world, pos).map(be -> be.tryInstallLock(data)).orElse(false);
  }

  /**
   * Tries to remove the lock from this block.
   *
   * @param world The world.
   * @param pos   Block’s position.
   * @return The lock that was removed or an empty value if there was none.
   */
  default Optional<LockData> tryRemoveLock(World world, final BlockPos pos) {
    return this.getBlockEntity(world, pos).flatMap(LockableBlockEntity::tryRemoveLock);
  }

  /**
   * Checks whether this block has a lock.
   *
   * @param world The world.
   * @param pos   Block’s position.
   * @return True if the block has a lock, false if it has none.
   */
  default boolean hasLock(World world, BlockPos pos) {
    return this.getBlockEntity(world, pos).map(LockableBlockEntity::hasLock).orElse(false);
  }

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
    return this.getBlockEntity(world, pos).map(LockableBlockEntity::isLocked).orElse(false);
  }

  default void setHasLockProperty(World world, BlockPos pos, BlockState state, boolean hasLock) {
    if (state.contains(HAS_LOCK)) {
      world.setBlockState(pos, state.with(HAS_LOCK, hasLock));
    }
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
    World world = builder.getWorld();
    Entity entity = builder.getNullable(LootContextParameters.THIS_ENTITY);
    if (!world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)
        || (entity instanceof PlayerEntity p && p.isCreative())) {
      return Collections.emptyList();
    }
    List<ItemStack> stacks = new LinkedList<>();
    stacks.add(new ItemStack((Block) this));
    if (builder.getNullable(LootContextParameters.BLOCK_ENTITY) instanceof LockableBlockEntity be) {
      be.getLockData().ifPresent(lockData -> {
        ItemStack stack = new ItemStack(ModItems.LOCK);
        ModItems.LOCK.setData(stack, lockData);
        stacks.add(stack);
      });
    }
    return stacks;
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
    MutableText message = new TranslatableText("lockable_doors.message.locked_block")
        .setStyle(Style.EMPTY.withColor(Formatting.RED));
    player.sendMessage(message, true);
  }

  /**
   * Returns the block entity at the given position.
   *
   * @param world The world.
   * @param pos   Block entity’s position.
   * @return An optional containing the block entity if it exists and is a {@link LockableBlockEntity},
   * an empty optional otherwise.
   */
  default Optional<LockableBlockEntity> getBlockEntity(World world, BlockPos pos) {
    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof LockableBlockEntity)) {
      return Optional.empty();
    }
    return Optional.of((LockableBlockEntity) blockEntity);
  }
}
