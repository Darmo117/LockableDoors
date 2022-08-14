package net.darmo_creations.lockable_doors.lock_system;

import net.darmo_creations.lockable_doors.blocks.BlockWithLock;
import net.minecraft.block.Block;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class LockRegistry {
  private static final Map<Block, Block> LOCKABLE_TO_BASE = new HashMap<>();
  private static final Map<Block, Block> BASE_TO_LOCKABLE = new HashMap<>();

  public static void add(final Block baseBlock, final Block lockableBlock) {
    if (LOCKABLE_TO_BASE.containsKey(baseBlock) || BASE_TO_LOCKABLE.containsKey(baseBlock)) {
      throw new IllegalArgumentException("block %s is already registered".formatted(baseBlock));
    }
    if (LOCKABLE_TO_BASE.containsKey(lockableBlock) || BASE_TO_LOCKABLE.containsKey(lockableBlock)) {
      throw new IllegalArgumentException("block %s is already registered".formatted(lockableBlock));
    }
    if (!(lockableBlock instanceof BlockWithLock)) {
      throw new IllegalArgumentException("block %s does not implement Lockable interface".formatted(lockableBlock));
    }
    LOCKABLE_TO_BASE.put(lockableBlock, baseBlock);
    BASE_TO_LOCKABLE.put(baseBlock, lockableBlock);
  }

  @Nullable
  public static Block getLockableBlock(final Block baseBlock) {
    return BASE_TO_LOCKABLE.get(baseBlock);
  }

  @Nullable
  public static Block getBaseBlock(final Block lockableBlock) {
    return LOCKABLE_TO_BASE.get(lockableBlock);
  }

  private LockRegistry() {
  }
}
