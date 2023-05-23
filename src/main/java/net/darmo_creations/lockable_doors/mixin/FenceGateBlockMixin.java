package net.darmo_creations.lockable_doors.mixin;

import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FenceGateBlock.class)
public interface FenceGateBlockMixin {
  @Accessor
  WoodType getType();
}
