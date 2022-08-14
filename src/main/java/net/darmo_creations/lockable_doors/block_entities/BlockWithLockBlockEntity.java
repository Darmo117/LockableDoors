package net.darmo_creations.lockable_doors.block_entities;

import net.darmo_creations.lockable_doors.items.KeyItem;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
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

  public LockData getLockData() {
    return this.lockData;
  }

  public void setLockData(final LockData lockData) {
    this.lockData = Objects.requireNonNull(lockData);
  }

  public boolean tryLock(final ItemStack itemStack) {
    if (this.lockData == null) {
      return false;
    }
    Optional<String> key = this.getKeyData(itemStack);
    if (key.isPresent()) {
      String k = key.get();
      if (this.lockData.keyFits(k)) {
        this.locked = true;
        return true;
      }
    }
    return false;
  }

  public boolean tryUnlock(final ItemStack itemStack) {
    if (this.lockData == null) {
      return false;
    }
    Optional<String> key = this.getKeyData(itemStack);
    if (key.isPresent()) {
      String k = key.get();
      if (this.lockData.keyFits(k)) {
        this.locked = false;
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
    if (this.lockData != null) {
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
    this.locked = nbt.getBoolean(LOCKED_KEY);
  }
}
