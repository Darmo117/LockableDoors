package net.darmo_creations.lockable_doors.items;

import net.darmo_creations.lockable_doors.LockableDoors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Declares all items added by this mod.
 */
@SuppressWarnings("unused")
public final class ModItems {
  public static final Item LOCK_HOUSING =
      register("lock_housing", new Item(new FabricItemSettings().group(LockableDoors.ITEM_GROUP)));
  public static final LockItem LOCK =
      register("lock", new LockItem(new FabricItemSettings().group(LockableDoors.ITEM_GROUP)));
  public static final KeyItem KEY =
      register("key", new KeyItem(new FabricItemSettings().group(LockableDoors.ITEM_GROUP)));
  public static final LockRemoverItem LOCK_REMOVER =
      register("lock_remover", new LockRemoverItem(new FabricItemSettings().group(LockableDoors.ITEM_GROUP).maxDamage(400)));

  /**
   * Registers an item.
   *
   * @param name Item’s registry name.
   * @param item Item instance.
   * @param <T>  Item’s type.
   * @return The registered item.
   */
  private static <T extends Item> T register(final String name, final T item) {
    return Registry.register(Registry.ITEM, new Identifier(LockableDoors.MOD_ID, name), item);
  }

  /**
   * Dummy method called from {@link LockableDoors#onInitialize()} to register items:
   * it forces the class to be loaded during mod initialization, while the registries are unlocked.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
  }

  private ModItems() {
  }
}
