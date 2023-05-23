package net.darmo_creations.lockable_doors.gui;

import net.darmo_creations.lockable_doors.LockableDoors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

/**
 * Defines all screen handlers added by this mod.
 */
public final class ModScreenHandlers {
  public static final ScreenHandlerType<LocksmithingStationScreenHandler> LOCKSMITHING_SCREEN_HANDLER =
      register("", LocksmithingStationScreenHandler::new);

  /**
   * Registers a screen handler type.
   *
   * @param id      Handler’s ID.
   * @param factory Handler factory.
   * @param <T>     Handler’s type.
   * @return The registered handler type.
   */
  private static <T extends ScreenHandler> ScreenHandlerType<T> register(final String id, final ScreenHandlerType.Factory<T> factory) {
    return Registry.register(Registries.SCREEN_HANDLER, new Identifier(LockableDoors.MOD_ID, id),
        new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
  }

  /**
   * Dummy method called from {@link LockableDoors#onInitialize()} to register screen handler types:
   * it forces the class to be loaded during mod initialization, while the registries are unlocked.
   * <p>
   * Must be called on both clients and server.
   */
  public static void init() {
  }

  private ModScreenHandlers() {
  }
}
