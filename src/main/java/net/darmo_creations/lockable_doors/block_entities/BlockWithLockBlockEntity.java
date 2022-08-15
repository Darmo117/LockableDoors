package net.darmo_creations.lockable_doors.block_entities;

import net.darmo_creations.lockable_doors.blocks.BlockWithLock;
import net.darmo_creations.lockable_doors.items.KeyItem;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BlockWithLockBlockEntity extends BlockEntity {
  private static final String LOCK_DATA_KEY = "LockData";
  private static final String LOCKED_KEY = "Locked";

  private LockData lockData;
  private boolean locked;

  public BlockWithLockBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.BLOCK_WITH_LOCK_BLOCK_ENTITY_TYPE, pos, blockState);
    this.lockData = null;
  }

  public Optional<LockData> getLockData() {
    return Optional.ofNullable(this.lockData);
  }

  public boolean tryInstallLock(final LockData data) {
    if (this.hasLock()) {
      return false;
    }
    this.lockData = data;
    this.updateBlockState();
    this.markDirty();
    return true;
  }

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

  public boolean hasLock() {
    return this.lockData != null;
  }

  public boolean tryLock(final ItemStack itemStack) {
    if (!this.hasLock()) {
      return false;
    }
    Optional<String> key = this.getKeyData(itemStack);
    if (key.isPresent()) {
      String k = key.get();
      if (this.lockData.keyFits(k)) {
        this.locked = true;
        this.markDirty();
        return true;
      }
    }
    return false;
  }

  public boolean tryUnlock(final ItemStack itemStack) {
    if (!this.hasLock()) {
      return false;
    }
    Optional<String> key = this.getKeyData(itemStack);
    if (key.isPresent()) {
      String k = key.get();
      if (this.lockData.keyFits(k)) {
        this.locked = false;
        this.markDirty();
        return true;
      }
    }
    return false;
  }

  public boolean isLocked() {
    return this.locked;
  }

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

  private void updateBlockState() {
    //noinspection ConstantConditions
    BlockState blockState = this.getWorld().getBlockState(this.pos);
    Block block = blockState.getBlock();
    if (block instanceof BlockWithLock bwl && blockState.getProperties().contains(BlockWithLock.HAS_LOCK)) {
      bwl.setHasLockProperty(this.getWorld(), this.pos, blockState, this.hasLock());
    }
  }
}
