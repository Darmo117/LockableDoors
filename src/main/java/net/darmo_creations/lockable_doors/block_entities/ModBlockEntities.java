package net.darmo_creations.lockable_doors.block_entities;

import net.darmo_creations.lockable_doors.LockableDoors;
import net.darmo_creations.lockable_doors.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Declares all block entity types added by this mod.
 */
public final class ModBlockEntities {
  public static final BlockEntityType<BlockWithLockBlockEntity> BLOCK_WITH_LOCK_BLOCK_ENTITY_TYPE =
      register("block_with_lock_block_entity_type", BlockWithLockBlockEntity::new,
          ModBlocks.OAK_DOOR_WITH_LOCK,
          ModBlocks.BIRCH_DOOR_WITH_LOCK,
          ModBlocks.SPRUCE_DOOR_WITH_LOCK,
          ModBlocks.JUNGLE_DOOR_WITH_LOCK,
          ModBlocks.ACACIA_DOOR_WITH_LOCK,
          ModBlocks.DARK_OAK_DOOR_WITH_LOCK,
          ModBlocks.IRON_DOOR_WITH_LOCK,

          ModBlocks.OAK_TRAPDOOR_WITH_LOCK,
          ModBlocks.BIRCH_TRAPDOOR_WITH_LOCK,
          ModBlocks.SPRUCE_TRAPDOOR_WITH_LOCK,
          ModBlocks.JUNGLE_TRAPDOOR_WITH_LOCK,
          ModBlocks.ACACIA_TRAPDOOR_WITH_LOCK,
          ModBlocks.DARK_OAK_TRAPDOOR_WITH_LOCK,
          ModBlocks.IRON_TRAPDOOR_WITH_LOCK,

          ModBlocks.OAK_FENCE_GATE_WITH_LOCK,
          ModBlocks.BIRCH_FENCE_GATE_WITH_LOCK,
          ModBlocks.SPRUCE_FENCE_GATE_WITH_LOCK,
          ModBlocks.JUNGLE_FENCE_GATE_WITH_LOCK,
          ModBlocks.ACACIA_FENCE_GATE_WITH_LOCK,
          ModBlocks.DARK_OAK_FENCE_GATE_WITH_LOCK
      );

  /**
   * Registers a block entity type.
   *
   * @param name    Block entity’s name.
   * @param factory A factory for the block entity type.
   * @param blocks  Block to associate to the block entity.
   * @param <T>     Type of the block entity type.
   * @param <U>     Type of the associated block entity.
   * @return The registered block entity type.
   */
  private static <T extends BlockEntityType<U>, U extends BlockEntity> T register(
      final String name,
      final FabricBlockEntityTypeBuilder.Factory<U> factory,
      final Block... blocks
  ) {
    //noinspection unchecked
    return (T) Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(LockableDoors.MOD_ID, name),
        FabricBlockEntityTypeBuilder.create(factory, blocks).build()
    );
  }

  /**
   * Dummy method called from {@link LockableDoors#onInitialize()} to register block entity types:
   * it forces the class to be loaded during mod initialization, while the registries are unlocked.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
  }

  private ModBlockEntities() {
  }
}
