package net.darmo_creations.lockable_doors;

import net.darmo_creations.lockable_doors.block_entities.ModBlockEntities;
import net.darmo_creations.lockable_doors.blocks.ModBlocks;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * Common mod initializer.
 */
public class LockableDoors implements ModInitializer {
  public static final String MOD_ID = "lockable_doors";

  // Creative mode’s item group
  public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
      new Identifier(MOD_ID, "item_group"),
      () -> new ItemStack(ModItems.KEY)
  );

  @Override
  public void onInitialize() {
    ModBlocks.init();
    ModBlockEntities.init();
    ModItems.init();
  }
}
