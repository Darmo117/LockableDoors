package net.darmo_creations.lockable_doors.lock_system;

import net.minecraft.nbt.NbtCompound;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Holds all relevent data for a lock.
 * <p>
 * The lock’s code is hashed using MD5 algorithm to avoid leaks to clients.
 */
public class LockData {
  private static final String HASH_KEY = "Hash";

  private final String hash;

  /**
   * Create lock data for the given key.
   *
   * @param key The key.
   */
  public LockData(final String key) {
    this.hash = this.hashKey(Objects.requireNonNull(key));
  }

  /**
   * Create lock data from an NBT tag.
   *
   * @param nbt The tag to deserialize.
   */
  public LockData(final NbtCompound nbt) {
    this.hash = nbt.getString(HASH_KEY);
  }

  /**
   * Checks whether the given key fits this lock,
   * i.e. if the key’s hash is equal to this lock’s hash.
   *
   * @param key The key to check.
   * @return True if the key fits, false otherwise.
   */
  public boolean keyFits(final String key) {
    return this.hash.equals(this.hashKey(key));
  }

  /**
   * Hashes the given key using the MD5 algorithm.
   *
   * @param key The key to hash.
   * @return The key’s hash.
   */
  protected String hashKey(final String key) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    md.update(key.getBytes());
    byte[] digest = md.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }

  /**
   * Serialize this lock to NBT.
   *
   * @return The tag.
   */
  public NbtCompound writeToNBT() {
    NbtCompound nbt = new NbtCompound();
    nbt.putString(HASH_KEY, this.hash);
    return nbt;
  }
}
