package net.darmo_creations.lockable_doors;

import net.darmo_creations.lockable_doors.blocks.ModBlocks;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client-side mod initializer.
 */
public class ClientInitializer implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ModBlocks.registerRenderLayers();
  }
}
