package net.darmo_creations.lockable_doors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Simple record that holds the mod’s configuration.
 *
 * @param canDestroyLockedBlocks Whether non-creative players can destroy locked blocks.
 */
public record ModConfig(boolean canDestroyLockedBlocks) {
  /**
   * Create a {@link ModConfig} object from a JSON object.
   *
   * @param jsonElement A JSON object.
   * @return The corresponding {@link ModConfig} object.
   */
  public static ModConfig fromJSON(final JsonElement jsonElement) {
    boolean canDestroyLockedBlocks = true;
    if (jsonElement instanceof JsonObject o) {
      JsonElement canDestroyLockedBlocksOpt = o.get("can_destroy_locked_blocks");
      if (canDestroyLockedBlocksOpt.isJsonPrimitive() && canDestroyLockedBlocksOpt.getAsJsonPrimitive().isBoolean()) {
        canDestroyLockedBlocks = canDestroyLockedBlocksOpt.getAsBoolean();
      }
    }
    return new ModConfig(
        canDestroyLockedBlocks
    );
  }
}
