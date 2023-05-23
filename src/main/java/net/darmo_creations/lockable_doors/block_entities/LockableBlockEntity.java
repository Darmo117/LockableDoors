package net.darmo_creations.lockable_doors.block_entities;

import net.darmo_creations.lockable_doors.blocks.LockableBlock;
import net.darmo_creations.lockable_doors.items.KeyItem;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

/**
 * Block entity for {@link LockableBlock}s. Handles all the locking/unlocking logic.
 */
public class LockableBlockEntity extends BlockEntity {
  private static final String LOCK_DATA_KEY = "LockData";
  private static final String LOCKED_KEY = "Locked";

  private LockData lockData;
  private boolean locked;

  public LockableBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.LOCKABLE_BLOCK_ENTITY_TYPE, pos, blockState);
    this.lockData = null;
  }

  /**
   * Returns the block’s lock data.
   *
   * @return The lock’s data if the block has a lock, an empty value otherwise.
   */
  public Optional<LockData> getLockData() {
    return Optional.ofNullable(this.lockData);
  }

  /**
   * Tries to install a lock on the associated block.
   *
   * @param data The lock to install.
   * @return True if the lock was installed, false if another is already present.
   */
  public boolean tryInstallLock(final LockData data) {
    if (this.hasLock()) {
      return false;
    }
    this.lockData = data;
    this.updateBlockState();
    this.markDirty();
    return true;
  }

  /**
   * Tries to remove the lock installed on the associated block.
   *
   * @return The removed lock, or an empty value if there was no lock to remove.
   */
  public Optional<LockData> tryRemoveLock() {
    if (!this.hasLock()) {
      return Optional.empty();
    }
    LockData old = this.lockData;
    this.lockData = null;
    this.updateBlockState();
    this.markDirty();
    return Optional.of(old);
  }

  /**
   * Returns whether the associated block has a lock installed.
   */
  public boolean hasLock() {
    return this.lockData != null;
  }

  /**
   * Tries to lock the associated block.
   *
   * @param itemStack The stack containing the key.
   * @return True if the block has been locked, false if the key does not fit the lock or no lock is installed.
   */
  public boolean tryLock(final ItemStack itemStack) {
    if (!this.hasLock()) {
      return false;
    }
    Optional<String> key = this.getKeyData(itemStack);
    if (key.isPresent()) {
      String k = key.get();
      if (this.lockData.keyFits(k)) {
        this.locked = true;
        //noinspection ConstantConditions
        this.world.updateListeners(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), Block.NOTIFY_LISTENERS);
        this.markDirty();
        return true;
      }
    }
    return false;
  }

  /**
   * Tries to unlock the associated block.
   *
   * @param itemStack The stack containing the key.
   * @return True if the block has been unlocked, false if the key does not fit the lock or no lock is installed.
   */
  public boolean tryUnlock(final ItemStack itemStack) {
    if (!this.hasLock()) {
      return false;
    }
    Optional<String> key = this.getKeyData(itemStack);
    if (key.isPresent()) {
      String k = key.get();
      if (this.lockData.keyFits(k)) {
        this.locked = false;
        //noinspection ConstantConditions
        this.world.updateListeners(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), Block.NOTIFY_LISTENERS);
        this.markDirty();
        return true;
      }
    }
    return false;
  }

  /**
   * Returns whether the associated block is locked.
   */
  public boolean isLocked() {
    return this.locked;
  }

  /**
   * Returns the key data from the given stack.
   *
   * @param stack The stack.
   * @return The key data or an empty value if the stack does not contain valid data.
   */
  protected Optional<String> getKeyData(final ItemStack stack) {
    Item item = stack.getItem();
    if (item instanceof KeyItem key) {
      return key.getData(stack);
    }
    return Optional.empty();
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    if (this.hasLock()) {
      nbt.put(LOCK_DATA_KEY, this.lockData.writeToNBT());
    }
    nbt.putBoolean(LOCKED_KEY, this.locked);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    if (nbt.contains(LOCK_DATA_KEY, NbtElement.COMPOUND_TYPE)) {
      this.lockData = new LockData(nbt.getCompound(LOCK_DATA_KEY));
    } else {
      this.lockData = null;
    }
    this.locked = this.hasLock() && nbt.getBoolean(LOCKED_KEY);
  }

  @Override
  public Packet<ClientPlayPacketListener> toUpdatePacket() {
    return BlockEntityUpdateS2CPacket.create(this);
  }

  @Override
  public NbtCompound toInitialChunkDataNbt() {
    return this.createNbt();
  }

  /**
   * Updates the associated block’s {@link LockableBlock#HAS_LOCK} property from this block entity’s state.
   */
  private void updateBlockState() {
    //noinspection ConstantConditions
    BlockState blockState = this.getWorld().getBlockState(this.pos);
    Block block = blockState.getBlock();
    if (block instanceof LockableBlock bwl && blockState.getProperties().contains(LockableBlock.HAS_LOCK)) {
      bwl.setHasLockProperty(this.getWorld(), this.pos, blockState, this.hasLock());
      //noinspection ConstantConditions
      this.world.updateListeners(this.pos, blockState, blockState, Block.NOTIFY_LISTENERS);
    }
  }
}
