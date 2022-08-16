package net.darmo_creations.lockable_doors.blocks;

import net.darmo_creations.lockable_doors.gui.LocksmithingStationScreenHandler;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The locksmithing station is used to prepare keys and locks.
 */
public class LocksmithingStationBlock extends Block {
  private static final Text TITLE = new TranslatableText("container.lockable_doors.locksmithing");

  public LocksmithingStationBlock() {
    super(FabricBlockSettings.of(Material.WOOD)
        .strength(2.5f)
        .sounds(BlockSoundGroup.WOOD));
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    if (world.isClient()) {
      return ActionResult.SUCCESS;
    }
    player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
    return ActionResult.CONSUME;
  }

  @SuppressWarnings("deprecation")
  @Override
  public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
    return new SimpleNamedScreenHandlerFactory(
        (syncId, inventory, player) ->
            new LocksmithingStationScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)),
        TITLE
    );
  }
}
