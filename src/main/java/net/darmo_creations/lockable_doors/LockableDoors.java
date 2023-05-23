package net.darmo_creations.lockable_doors;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.darmo_creations.lockable_doors.block_entities.ModBlockEntities;
import net.darmo_creations.lockable_doors.blocks.ModBlocks;
import net.darmo_creations.lockable_doors.gui.ModScreenHandlers;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.darmo_creations.lockable_doors.network.C2SPacketFactory;
import net.darmo_creations.lockable_doors.network.PacketRegistry;
import net.darmo_creations.lockable_doors.network.packets.RenameLockOrKeyPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Common mod initializer.
 */
public class LockableDoors implements ModInitializer {
  public static final String MOD_ID = "lockable_doors";

  // Creative mode’s item group
  public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(MOD_ID, "item_group"))
      .icon(() -> new ItemStack(ModItems.KEY))
      .build();

  public static final ModConfig CONFIG;

  static {
    Path configFile = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
    JsonElement jsonElement;
    try (FileReader fr = new FileReader(configFile.toFile())) {
      jsonElement = JsonParser.parseReader(fr);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    CONFIG = ModConfig.fromJSON(jsonElement);
  }

  @Override
  public void onInitialize() {
    ModBlocks.init();
    ModBlockEntities.init();
    ModItems.init();
    ModScreenHandlers.init();
    this.registerServerPacketHandlers();
  }

  /**
   * Registers all packets and associated handlers.
   */
  private void registerServerPacketHandlers() {
    PacketRegistry.registerPacket(
        C2SPacketFactory.RENAME_LOCK_OR_KEY_PACKET_ID,
        RenameLockOrKeyPacket.class,
        new RenameLockOrKeyPacket.ServerHandler()
    );
  }
}
