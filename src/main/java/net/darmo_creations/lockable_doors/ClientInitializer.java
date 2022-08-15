package net.darmo_creations.lockable_doors;

import net.darmo_creations.lockable_doors.blocks.ModBlocks;
import net.darmo_creations.lockable_doors.gui.LocksmithingStationScreen;
import net.darmo_creations.lockable_doors.gui.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

/**
 * Client-side mod initializer.
 */
public class ClientInitializer implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ModBlocks.registerRenderLayers();
    HandledScreens.register(ModScreenHandlers.LOCKSMITHING_SCREEN_HANDLER, LocksmithingStationScreen::new);
  }
}
