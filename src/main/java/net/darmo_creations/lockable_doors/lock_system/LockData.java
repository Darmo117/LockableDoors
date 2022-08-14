package net.darmo_creations.lockable_doors.lock_system;

import net.minecraft.nbt.NbtCompound;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LockData {
  private static final String HASH_KEY = "Hash";

  private final String hash;

  public LockData(final String key) {
    this.hash = this.hashKey(Objects.requireNonNull(key));
  }

  public LockData(final NbtCompound nbt) {
    this(nbt.getString(HASH_KEY));
  }

  public boolean keyFits(final String key) {
    return this.hash.equals(this.hashKey(key));
  }

  protected String hashKey(final String key) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    md.update(key.getBytes());
    byte[] digest = md.digest();
    return new String(digest, StandardCharsets.UTF_8);
  }

  public NbtCompound writeToNBT() {
    NbtCompound nbt = new NbtCompound();
    nbt.putString(HASH_KEY, this.hash);
    return nbt;
  }
}
