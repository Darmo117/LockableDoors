package net.darmo_creations.lockable_doors.mixin;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.TrapdoorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrapdoorBlock.class)
public interface TrapdoorBlockMixin {
  @Accessor
  BlockSetType getBlockSetType();
}
