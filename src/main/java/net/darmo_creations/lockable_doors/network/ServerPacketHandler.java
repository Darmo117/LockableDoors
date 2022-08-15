package net.darmo_creations.lockable_doors.network;

import net.darmo_creations.lockable_doors.network.packets.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Handler for packets comming from a client.
 *
 * @param <T> Packet’s type.
 */
public interface ServerPacketHandler<T extends Packet> {
  /**
   * Called whenever a packet is received by the server.
   *
   * @param server Server instance.
   * @param player Player that sent the packet.
   * @param packet The packet.
   */
  void onPacket(MinecraftServer server, ServerPlayerEntity player, final T packet);
}
