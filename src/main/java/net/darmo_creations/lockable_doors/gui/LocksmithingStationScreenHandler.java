package net.darmo_creations.lockable_doors.gui;

import net.darmo_creations.lockable_doors.blocks.LocksmithingStationBlock;
import net.darmo_creations.lockable_doors.blocks.ModBlocks;
import net.darmo_creations.lockable_doors.items.KeyItem;
import net.darmo_creations.lockable_doors.items.LockItem;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.darmo_creations.lockable_doors.lock_system.LockData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

/**
 * Screen handler for {@link LocksmithingStationBlock}.
 */
public class LocksmithingStationScreenHandler extends ScreenHandler {
  protected final ScreenHandlerContext context;
  protected final PlayerEntity player;
  protected final Inventory input = new SimpleInventory(1) {
    @Override
    public void markDirty() {
      super.markDirty();
      LocksmithingStationScreenHandler.this.onContentChanged(this);
    }
  };
  protected final Inventory output = new SimpleInventory(1) {
    @Override
    public void markDirty() {
      super.markDirty();
      LocksmithingStationScreenHandler.this.onContentChanged(this);
    }
  };
  protected String data;

  public LocksmithingStationScreenHandler(int syncId, PlayerInventory inventory) {
    this(syncId, inventory, ScreenHandlerContext.EMPTY);
  }

  public LocksmithingStationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
    super(ModScreenHandlers.LOCKSMITHING_SCREEN_HANDLER, syncId);
    this.context = context;
    this.player = playerInventory.player;

    int middle = 80;
    this.addSlot(new Slot(this.input, 0, middle - 20, 50) {
      @Override
      public boolean canInsert(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof KeyItem || item instanceof LockItem;
      }
    });
    this.addSlot(new Slot(this.output, 0, middle + 20, 50) {
      @Override
      public boolean canInsert(ItemStack stack) {
        return false;
      }

      @Override
      public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
        LocksmithingStationScreenHandler.this.onTakeOutput(stack);
      }
    });

    // Generate player inventory slots
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }
    for (int i = 0; i < 9; ++i) {
      this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }
  }

  /**
   * Set output item’s data.
   *
   * @param data The data.
   */
  public void setData(String data) {
    this.data = data.isEmpty() ? null : data;
    this.updateResult();
  }

  @Override
  public boolean canUse(PlayerEntity player) {
    return this.context.get(
        (world, pos) -> world.getBlockState(pos).isOf(ModBlocks.LOCKSMITHING_STATION)
            && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64,
        true
    );
  }

  @Override
  public void onContentChanged(Inventory inventory) {
    super.onContentChanged(inventory);
    if (inventory == this.input) {
      this.updateResult();
    }
  }

  @Override
  public ItemStack transferSlot(PlayerEntity player, int index) {
    ItemStack itemStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot.hasStack()) {
      ItemStack itemStack2 = slot.getStack();
      itemStack = itemStack2.copy();
      if (index == 1) {
        if (!this.insertItem(itemStack2, 2, 38, true)) {
          return ItemStack.EMPTY;
        }
        slot.onQuickTransfer(itemStack2, itemStack);
      } else if (index == 0) {
        if (!this.insertItem(itemStack2, 2, 38, false)) {
          return ItemStack.EMPTY;
        }
      } else if (index >= 2 && index < 38) {
        if (!this.insertItem(itemStack2, 0, 1, false)) {
          return ItemStack.EMPTY;
        }
      }
      if (itemStack2.isEmpty()) {
        slot.setStack(ItemStack.EMPTY);
      } else {
        slot.markDirty();
      }
      if (itemStack2.getCount() == itemStack.getCount()) {
        return ItemStack.EMPTY;
      }
      slot.onTakeItem(player, itemStack2);
    }
    return itemStack;
  }

  @Override
  public void close(PlayerEntity player) {
    super.close(player);
    this.context.run((world, pos) -> this.dropInventory(player, this.input));
  }

  private void updateResult() {
    ItemStack inputStack = this.input.getStack(0);
    if (inputStack.isEmpty()) {
      this.output.setStack(0, ItemStack.EMPTY);
      return;
    }
    Item inputItem = inputStack.getItem();
    ItemStack outputStack = new ItemStack(inputItem, inputStack.getCount());
    if (this.data != null) {
      if (inputItem == ModItems.KEY) {
        ModItems.KEY.setData(outputStack, this.data);
      } else if (inputItem == ModItems.LOCK) {
        ModItems.LOCK.setData(outputStack, new LockData(this.data));
      } else {
        this.output.setStack(0, ItemStack.EMPTY);
        return;
      }
    }
    this.output.setStack(0, outputStack);
    this.sendContentUpdates();
  }

  private void onTakeOutput(final ItemStack stack) {
    if (!ItemStack.areEqual(this.input.getStack(0), stack)) {
      this.context.run((world, pos) ->
          world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1));
    }
    this.input.setStack(0, ItemStack.EMPTY);
  }
}
