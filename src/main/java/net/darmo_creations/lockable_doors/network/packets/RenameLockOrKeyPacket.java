package net.darmo_creations.lockable_doors.network.packets;

import io.netty.buffer.Unpooled;
import net.darmo_creations.lockable_doors.gui.LocksmithingStationScreen;
import net.darmo_creations.lockable_doors.gui.LocksmithingStationScreenHandler;
import net.darmo_creations.lockable_doors.network.ServerPacketHandler;
import net.minecraft.SharedConstants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * This packet is used by {@link LocksmithingStationScreen} to synchronize the output stack’s name.
 */
public class RenameLockOrKeyPacket implements Packet {
  private final String name;

  // Required by PacketRegistry
  @SuppressWarnings("unused")
  public RenameLockOrKeyPacket(final PacketByteBuf buf) {
    this(buf.readString());
  }

  public RenameLockOrKeyPacket(final String name) {
    this.name = name;
  }

  @Override
  public PacketByteBuf getBuffer() {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    buf.writeString(this.name);
    return buf;
  }

  /**
   * Returns the output stack’s new name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Server-side handler for this packet.
   */
  public static class ServerHandler implements ServerPacketHandler<RenameLockOrKeyPacket> {
    @Override
    public void onPacket(MinecraftServer server, ServerPlayerEntity player, RenameLockOrKeyPacket packet) {
      server.execute(() -> {
        if (player.currentScreenHandler instanceof LocksmithingStationScreenHandler sh) {
          String string = SharedConstants.stripInvalidChars(packet.getName());
          if (string.length() <= LocksmithingStationScreen.NAME_MAX_LENGTH) {
            sh.setData(string);
          }
        }
      });
    }
  }
}
