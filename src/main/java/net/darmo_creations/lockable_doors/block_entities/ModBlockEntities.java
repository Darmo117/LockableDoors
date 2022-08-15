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
  public static final BlockEntityType<LockableBlockEntity> LOCKABLE_BLOCK_ENTITY_TYPE =
      register("lockable_block_entity_type", LockableBlockEntity::new,
          ModBlocks.LOCKABLE_OAK_DOOR,
          ModBlocks.LOCKABLE_BIRCH_DOOR,
          ModBlocks.LOCKABLE_SPRUCE_DOOR,
          ModBlocks.LOCKABLE_JUNGLE_DOOR,
          ModBlocks.LOCKABLE_ACACIA_DOOR,
          ModBlocks.LOCKABLE_DARK_OAK_DOOR,
          ModBlocks.LOCKABLE_CRIMSON_DOOR,
          ModBlocks.LOCKABLE_WARPED_DOOR,
          ModBlocks.LOCKABLE_IRON_DOOR,

          ModBlocks.LOCKABLE_OAK_TRAPDOOR,
          ModBlocks.LOCKABLE_BIRCH_TRAPDOOR,
          ModBlocks.LOCKABLE_SPRUCE_TRAPDOOR,
          ModBlocks.LOCKABLE_JUNGLE_TRAPDOOR,
          ModBlocks.LOCKABLE_ACACIA_TRAPDOOR,
          ModBlocks.LOCKABLE_DARK_OAK_TRAPDOOR,
          ModBlocks.LOCKABLE_CRIMSON_TRAPDOOR,
          ModBlocks.LOCKABLE_WARPED_TRAPDOOR,
          ModBlocks.LOCKABLE_IRON_TRAPDOOR,

          ModBlocks.LOCKABLE_OAK_FENCE_GATE,
          ModBlocks.LOCKABLE_BIRCH_FENCE_GATE,
          ModBlocks.LOCKABLE_SPRUCE_FENCE_GATE,
          ModBlocks.LOCKABLE_JUNGLE_FENCE_GATE,
          ModBlocks.LOCKABLE_ACACIA_FENCE_GATE,
          ModBlocks.LOCKABLE_DARK_OAK_FENCE_GATE,
          ModBlocks.LOCKABLE_CRIMSON_FENCE_GATE,
          ModBlocks.LOCKABLE_WARPED_FENCE_GATE
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
