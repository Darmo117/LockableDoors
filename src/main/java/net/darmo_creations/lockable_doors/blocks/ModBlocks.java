package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.LockableDoors;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.impl.content.registry.FlammableBlockRegistryImpl;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * This class declares all blocks for this mod.
 */
public final class ModBlocks {
  public static final Block LOCKSMITHING_STATION =
      register("locksmithing_station", new LocksmithingStationBlock());

  // Doors
  public static final Block LOCKABLE_OAK_DOOR =
      registerLockableBlock("lockable_oak_door", LockableDoorBlock::new, (DoorBlock) Blocks.OAK_DOOR);
  public static final Block LOCKABLE_BIRCH_DOOR =
      registerLockableBlock("lockable_birch_door", LockableDoorBlock::new, (DoorBlock) Blocks.BIRCH_DOOR);
  public static final Block LOCKABLE_SPRUCE_DOOR =
      registerLockableBlock("lockable_spruce_door", LockableDoorBlock::new, (DoorBlock) Blocks.SPRUCE_DOOR);
  public static final Block LOCKABLE_JUNGLE_DOOR =
      registerLockableBlock("lockable_jungle_door", LockableDoorBlock::new, (DoorBlock) Blocks.JUNGLE_DOOR);
  public static final Block LOCKABLE_ACACIA_DOOR =
      registerLockableBlock("lockable_acacia_door", LockableDoorBlock::new, (DoorBlock) Blocks.ACACIA_DOOR);
  public static final Block LOCKABLE_DARK_OAK_DOOR =
      registerLockableBlock("lockable_dark_oak_door", LockableDoorBlock::new, (DoorBlock) Blocks.DARK_OAK_DOOR);
  public static final Block LOCKABLE_CRIMSON_DOOR =
      registerLockableBlock("lockable_crimson_door", LockableDoorBlock::new, (DoorBlock) Blocks.CRIMSON_DOOR);
  public static final Block LOCKABLE_WARPED_DOOR =
      registerLockableBlock("lockable_warped_door", LockableDoorBlock::new, (DoorBlock) Blocks.WARPED_DOOR);
  public static final Block LOCKABLE_IRON_DOOR =
      registerLockableBlock("lockable_iron_door", LockableDoorBlock::new, (DoorBlock) Blocks.IRON_DOOR);
  // Trapdoors
  public static final Block LOCKABLE_OAK_TRAPDOOR =
      registerLockableBlock("lockable_oak_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.OAK_TRAPDOOR);
  public static final Block LOCKABLE_BIRCH_TRAPDOOR =
      registerLockableBlock("lockable_birch_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.BIRCH_TRAPDOOR);
  public static final Block LOCKABLE_SPRUCE_TRAPDOOR =
      registerLockableBlock("lockable_spruce_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.SPRUCE_TRAPDOOR);
  public static final Block LOCKABLE_JUNGLE_TRAPDOOR =
      registerLockableBlock("lockable_jungle_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.JUNGLE_TRAPDOOR);
  public static final Block LOCKABLE_ACACIA_TRAPDOOR =
      registerLockableBlock("lockable_acacia_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.ACACIA_TRAPDOOR);
  public static final Block LOCKABLE_DARK_OAK_TRAPDOOR =
      registerLockableBlock("lockable_dark_oak_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.DARK_OAK_TRAPDOOR);
  public static final Block LOCKABLE_CRIMSON_TRAPDOOR =
      registerLockableBlock("lockable_crimson_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.CRIMSON_TRAPDOOR);
  public static final Block LOCKABLE_WARPED_TRAPDOOR =
      registerLockableBlock("lockable_warped_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.WARPED_TRAPDOOR);
  public static final Block LOCKABLE_IRON_TRAPDOOR =
      registerLockableBlock("lockable_iron_trapdoor", LockableTrapdoorBlock::new, (TrapdoorBlock) Blocks.IRON_TRAPDOOR);
  // Fence gates
  public static final Block LOCKABLE_OAK_FENCE_GATE =
      registerLockableBlock("lockable_oak_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.OAK_FENCE_GATE);
  public static final Block LOCKABLE_BIRCH_FENCE_GATE =
      registerLockableBlock("lockable_birch_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.BIRCH_FENCE_GATE);
  public static final Block LOCKABLE_SPRUCE_FENCE_GATE =
      registerLockableBlock("lockable_spruce_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.SPRUCE_FENCE_GATE);
  public static final Block LOCKABLE_JUNGLE_FENCE_GATE =
      registerLockableBlock("lockable_jungle_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.JUNGLE_FENCE_GATE);
  public static final Block LOCKABLE_ACACIA_FENCE_GATE =
      registerLockableBlock("lockable_acacia_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.ACACIA_FENCE_GATE);
  public static final Block LOCKABLE_DARK_OAK_FENCE_GATE =
      registerLockableBlock("lockable_dark_oak_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.DARK_OAK_FENCE_GATE);
  public static final Block LOCKABLE_CRIMSON_FENCE_GATE =
      registerLockableBlock("lockable_crimson_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.CRIMSON_FENCE_GATE);
  public static final Block LOCKABLE_WARPED_FENCE_GATE =
      registerLockableBlock("lockable_warped_fence_gate", LockableFenceGateBlock::new, (FenceGateBlock) Blocks.WARPED_FENCE_GATE);

  /**
   * Registers a lockable block and its item, and puts it in the given item group.
   *
   * @param name    Block’s name.
   * @param factory A factory that provides the block to register.
   * @param <T>     Type of the block to register.
   * @param <U>     Type of the base block.
   * @return The registered block.
   */
  private static <T extends Block, U extends Block> T registerLockableBlock(final String name, final Function<U, T> factory, final U baseBlock) {
    T lockableBlock = factory.apply(baseBlock);
    register(name, lockableBlock);
    setFlammability(lockableBlock, baseBlock);
    return lockableBlock;
  }

  /**
   * Registers a block and puts it in the mod’s item group.
   *
   * @param <T>   Type of the block to register.
   * @param name  Block’s name.
   * @param block Block to register.
   * @return The registered block.
   */
  private static <T extends Block> T register(final String name, final T block) {
    Registry.register(Registries.BLOCK, new Identifier(LockableDoors.MOD_ID, name), block);
    BlockItem blockItem = new BlockItem(block, new FabricItemSettings());
    Registry.register(Registries.ITEM, new Identifier(LockableDoors.MOD_ID, name), blockItem);
    ItemGroupEvents.modifyEntriesEvent(LockableDoors.ITEM_GROUP).register(content -> content.add(blockItem));
    return block;
  }

  /**
   * Makes the given block flammable if the base one is too.
   *
   * @param block     The block to set the flammability of.
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
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_OAK_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_BIRCH_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_SPRUCE_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_JUNGLE_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_ACACIA_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_DARK_OAK_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_CRIMSON_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_WARPED_DOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_IRON_DOOR, RenderLayer.getCutout());

    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_OAK_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_BIRCH_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_SPRUCE_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_JUNGLE_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_ACACIA_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_DARK_OAK_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_CRIMSON_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_WARPED_TRAPDOOR, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_IRON_TRAPDOOR, RenderLayer.getCutout());

    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_OAK_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_BIRCH_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_SPRUCE_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_JUNGLE_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_ACACIA_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_DARK_OAK_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_CRIMSON_FENCE_GATE, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCKABLE_WARPED_FENCE_GATE, RenderLayer.getCutout());
  }

  private ModBlocks() {
  }
}
