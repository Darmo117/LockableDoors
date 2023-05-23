package net.darmo_creations.lockable_doors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Simple record that holds the mod’s configuration.
 *
 * @param canDestroyLockedBlocks Whether non-creative players can destroy locked blocks.
 * @param canRenameKeys          Whether already named keys can be renamed.
 */
public record ModConfig(boolean canDestroyLockedBlocks, boolean canRenameKeys) {
  public static final boolean DEFAULT_CAN_DESTROY_LOCKED_BLOCKS = true;
  public static final boolean DEFAULT_CAN_RENAME_KEYS = false;

  public static final ModConfig DEFAULT = new ModConfig(
      DEFAULT_CAN_DESTROY_LOCKED_BLOCKS,
      DEFAULT_CAN_RENAME_KEYS
  );

  /**
   * Create a {@link ModConfig} object from a JSON object.
   *
   * @param jsonElement A JSON object.
   * @return The corresponding {@link ModConfig} object.
   */
  public static ModConfig fromJSON(final JsonObject jsonElement) {
    return new ModConfig(
        getBoolean(jsonElement, "can_destroy_locked_blocks", DEFAULT_CAN_DESTROY_LOCKED_BLOCKS),
        getBoolean(jsonElement, "can_rename_keys", DEFAULT_CAN_RENAME_KEYS)
    );
  }

  private static boolean getBoolean(final JsonObject jsonObject, String optionName, boolean defaultValue) {
    JsonElement option = jsonObject.get(optionName);
    if (option.isJsonPrimitive() && option.getAsJsonPrimitive().isBoolean()) {
      return option.getAsBoolean();
    }
    return defaultValue;
  }
}
