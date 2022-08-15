package net.darmo_creations.lockable_doors.network;

import net.darmo_creations.lockable_doors.network.packets.Packet;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages all custom packets defined by this mod.
 */
public final class PacketRegistry {
  private static final Map<Class<? extends Packet>, Identifier> PACKET_CLASSES = new HashMap<>();

  /**
   * Registers a packet.
   *
   * @param id            Packet’s ID.
   * @param packetClass   Packet’s class.
   * @param serverHandler The server-side handler.
   */
  public static <T extends Packet> void registerPacket(final Identifier id, final Class<T> packetClass, final ServerPacketHandler<T> serverHandler) {
    PACKET_CLASSES.put(packetClass, id);
    ServerPlayNetworking.registerGlobalReceiver(id,
        (server, player, handler, buf, responseSender) -> {
          try {
            serverHandler.onPacket(server, player, packetClass.getConstructor(PacketByteBuf.class).newInstance(buf));
          } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /**
   * Returns the ID on the given packet class.
   */
  public static Optional<Identifier> getPacketID(Class<? extends Packet> packetClass) {
    return Optional.ofNullable(PACKET_CLASSES.get(packetClass));
  }

  private PacketRegistry() {
  }
}
