package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.LockableDoors;
import net.darmo_creations.lockable_doors.lock_system.LockRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.impl.content.registry.FlammableBlockRegistryImpl;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.InvocationTargetException;

// TODO flammability

/**
 * This class declares all blocks for this mod.
 */
public final class ModBlocks {
  // Doors
  public static final Block OAK_DOOR_WITH_LOCK =
      registerLockableBlock("oak_door_with_lock", DoorWithLockBlock.class, Blocks.OAK_DOOR);
  public static final Block BIRCH_DOOR_WITH_LOCK =
      registerLockableBlock("birch_door_with_lock", DoorWithLockBlock.class, Blocks.BIRCH_DOOR);
  public static final Block SPRUCE_DOOR_WITH_LOCK =
      registerLockableBlock("spruce_door_with_lock", DoorWithLockBlock.class, Blocks.SPRUCE_DOOR);
  public static final Block JUNGLE_DOOR_WITH_LOCK =
      registerLockableBlock("jungle_door_with_lock", DoorWithLockBlock.class, Blocks.JUNGLE_DOOR);
  public static final Block ACACIA_DOOR_WITH_LOCK =
      registerLockableBlock("acacia_door_with_lock", DoorWithLockBlock.class, Blocks.ACACIA_DOOR);
  public static final Block DARK_OAK_DOOR_WITH_LOCK =
      registerLockableBlock("dark_oak_door_with_lock", DoorWithLockBlock.class, Blocks.DARK_OAK_DOOR);
  public static final Block IRON_DOOR_WITH_LOCK =
      registerLockableBlock("iron_door_with_lock", DoorWithLockBlock.class, Blocks.IRON_DOOR);
  // Trapdoors
  public static final Block OAK_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("oak_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.OAK_TRAPDOOR);
  public static final Block BIRCH_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("birch_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.BIRCH_TRAPDOOR);
  public static final Block SPRUCE_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("spruce_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.SPRUCE_TRAPDOOR);
  public static final Block JUNGLE_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("jungle_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.JUNGLE_TRAPDOOR);
  public static final Block ACACIA_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("acacia_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.ACACIA_TRAPDOOR);
  public static final Block DARK_OAK_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("dark_oak_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.DARK_OAK_TRAPDOOR);
  public static final Block IRON_TRAPDOOR_WITH_LOCK =
      registerLockableBlock("iron_trapdoor_with_lock", TrapdoorWithLockBlock.class, Blocks.IRON_TRAPDOOR);
  // Fence gates
  public static final Block OAK_FENCE_GATE_WITH_LOCK =
      registerLockableBlock("oak_fence_gate_with_lock", FenceGateWithLockBlock.class, Blocks.OAK_FENCE_GATE);
  public static final Block BIRCH_FENCE_GATE_WITH_LOCK =
      registerLockableBlock("birch_fence_gate_with_lock", FenceGateWithLockBlock.class, Blocks.BIRCH_FENCE_GATE);
  public static final Block SPRUCE_FENCE_GATE_WITH_LOCK =
      registerLockableBlock("spruce_fence_gate_with_lock", FenceGateWithLockBlock.class, Blocks.SPRUCE_FENCE_GATE);
  public static final Block JUNGLE_FENCE_GATE_WITH_LOCK =
      registerLockableBlock("jungle_fence_gate_with_lock", FenceGateWithLockBlock.class, Blocks.JUNGLE_FENCE_GATE);
  public static final Block ACACIA_FENCE_GATE_WITH_LOCK =
      registerLockableBlock("acacia_fence_gate_with_lock", FenceGateWithLockBlock.class, Blocks.ACACIA_FENCE_GATE);
  public static final Block DARK_OAK_FENCE_GATE_WITH_LOCK =
      registerLockableBlock("dark_oak_fence_gate_with_lock", FenceGateWithLockBlock.class, Blocks.DARK_OAK_FENCE_GATE);

  /**
   * Registers a lockable block and its item, and puts it in the given item group.
   *
   * @param name               Block’s name.
   * @param lockableBlockClass Block to register.
   * @param <T>                Type of the block to register.
   * @return The registered block.
   */
  private static <T extends Block> T registerLockableBlock(final String name, final Class<T> lockableBlockClass, final Block baseBlock) {
    T b;
    try {
      b = lockableBlockClass.getConstructor(Settings.class).newInstance(Settings.copy(baseBlock));
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    Registry.register(Registry.BLOCK, new Identifier(LockableDoors.MOD_ID, name), b);
    Registry.register(Registry.ITEM, new Identifier(LockableDoors.MOD_ID, name),
        new BlockItem(b, new FabricItemSettings().group(LockableDoors.ITEM_GROUP)));
    LockRegistry.add(baseBlock, b);
    setFlammability(b, baseBlock);
    return b;
  }

  /**
   * Makes the given block flammable if the base one is too.
   *
   * @param block     The block with a lock.
   * @param baseBlock The base block.
   */
  private static void setFlammability(final Block block, final Block baseBlock) {
    FlammableBlockRegistryImpl fireRegistry = (FlammableBlockRegistryImpl) FlammableBlockRegistry.getDefaultInstance();
    FlammableBlockRegistry.Entry entry = fireRegistry.getFabric(baseBlock);
    if (entry == null) {
      entry = fireRegistry.get(baseBlock); // Fallback on entry for vanilla fire block
    }
    int burnChance = entry.getBurnChance();
    int spreadChance = entry.getSpreadChance();
    if (burnChance != 0 && spreadChance != 0) {
      fireRegistry.add(block, burnChance, spreadChance);
    }
  }

  /**
   * Dummy method called from {@link LockableDoors#onInitialize()} to register blocks:
   * it forces the class to be loaded during mod initialization, while the registries are unlocked.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
  }

  /**
   * Registers block render layers.
   * Should be called on client only.
   */
  public static void registerRenderLayers() {
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_DOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BIRCH_DOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPRUCE_DOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JUNGLE_DOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ACACIA_DOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DARK_OAK_DOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_DOOR_WITH_LOCK, RenderLayer.getCutout());

    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BIRCH_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPRUCE_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JUNGLE_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ACACIA_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DARK_OAK_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_TRAPDOOR_WITH_LOCK, RenderLayer.getCutout());

    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_FENCE_GATE_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BIRCH_FENCE_GATE_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPRUCE_FENCE_GATE_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JUNGLE_FENCE_GATE_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ACACIA_FENCE_GATE_WITH_LOCK, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DARK_OAK_FENCE_GATE_WITH_LOCK, RenderLayer.getCutout());
  }

  private ModBlocks() {
  }
}
