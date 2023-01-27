package net.darmo_creations.lockable_doors.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.darmo_creations.lockable_doors.LockableDoors;
import net.darmo_creations.lockable_doors.items.KeyItem;
import net.darmo_creations.lockable_doors.items.LockItem;
import net.darmo_creations.lockable_doors.items.ModItems;
import net.darmo_creations.lockable_doors.network.C2SPacketFactory;
import net.darmo_creations.lockable_doors.network.packets.RenameLockOrKeyPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class LocksmithingStationScreen
    extends HandledScreen<LocksmithingStationScreenHandler>
    implements ScreenHandlerListener {
  public static final int NAME_MAX_LENGTH = 50;

  private static final Identifier TEXTURE =
      new Identifier(LockableDoors.MOD_ID, "textures/gui/container/locksmithing_station.png");

  private TextFieldWidget nameField;

  public LocksmithingStationScreen(LocksmithingStationScreenHandler handler, PlayerInventory inventory, final Text title) {
    super(handler, inventory, title);
    this.titleX = 60;
  }

  @Override
  protected void init() {
    super.init();
    this.setup();
    this.handler.addListener(this);
  }

  protected void setup() {
    //noinspection ConstantConditions
    this.client.keyboard.setRepeatEvents(true);
    int i = (this.width - this.backgroundWidth) / 2;
    int j = (this.height - this.backgroundHeight) / 2;
    this.nameField = new TextFieldWidget(this.textRenderer, i + 62, j + 24, 103, 12,
        Text.translatable("container.lockable_doors.locksmithing.rename"));
    this.nameField.setFocusUnlocked(false);
    this.nameField.setEditableColor(-1);
    this.nameField.setUneditableColor(-1);
    this.nameField.setDrawsBackground(false);
    this.nameField.setMaxLength(NAME_MAX_LENGTH);
    this.nameField.setChangedListener(this::onRenamed);
    this.nameField.setText("");
    this.addSelectableChild(this.nameField);
    this.setInitialFocus(this.nameField);
    this.nameField.setEditable(this.handler.getSlot(0).hasStack());
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    this.renderBackground(matrices);
    super.render(matrices, mouseX, mouseY, delta);
    RenderSystem.disableBlend();
    this.renderForeground(matrices, mouseX, mouseY, delta);
    this.drawMouseoverTooltip(matrices, mouseX, mouseY);
  }

  protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    this.nameField.render(matrices, mouseX, mouseY, delta);
  }

  @Override
  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1, 1, 1, 1);
    RenderSystem.setShaderTexture(0, TEXTURE);
    int i = (this.width - this.backgroundWidth) / 2;
    int j = (this.height - this.backgroundHeight) / 2;
    // Draw full GUI
    this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    if (!this.handler.getSlot(1).canTakeItems(this.handler.player)) {
      this.drawTexture(matrices, i + 79, j + 48, 176, 0, 18, 21);
    }
    // Draw text field background part
    this.drawTexture(matrices, i + 59, j + 20, 0, this.backgroundHeight + (this.handler.getSlot(0).hasStack() ? 0 : 16), 110, 16);
  }

  private void onRenamed(String name) {
    String string = name.trim();
    this.handler.setData(string);
    C2SPacketFactory.sendPacket(new RenameLockOrKeyPacket(string));
  }

  @Override
  public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    if (slotId == 0) {
      if (stack.getItem() instanceof KeyItem) {
        this.nameField.setText(ModItems.KEY.getData(stack).orElse(""));
      } else if (stack.getItem() instanceof LockItem) {
        this.nameField.setText("");
      }
      this.nameField.setEditable(!stack.isEmpty());
      this.setFocused(this.nameField);
    }
  }

  @Override
  public void handledScreenTick() {
    super.handledScreenTick();
    this.nameField.tick();
  }

  @Override
  public void resize(MinecraftClient client, int width, int height) {
    String string = this.nameField.getText();
    this.init(client, width, height);
    this.nameField.setText(string);
  }

  @Override
  public void removed() {
    super.removed();
    this.handler.removeListener(this);
    //noinspection ConstantConditions
    this.client.keyboard.setRepeatEvents(false);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
      //noinspection ConstantConditions
      this.client.player.closeHandledScreen();
    }
    if (this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.isActive()) {
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
  }
}
