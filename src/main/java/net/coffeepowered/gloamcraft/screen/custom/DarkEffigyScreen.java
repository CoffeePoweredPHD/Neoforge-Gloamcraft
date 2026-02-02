package net.coffeepowered.gloamcraft.screen.custom;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.modsavedata.RitualProgressHelper;
import net.coffeepowered.gloamcraft.modsavedata.UnlockRitualPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

//just the image thats displayed
public class DarkEffigyScreen extends AbstractContainerScreen<DarkEffigyMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Gloamcraft.MOD_ID, "textures/gui/darkeffigy/darkeffigy_gui.png");

    public DarkEffigyScreen(DarkEffigyMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        //Add widget stuff here

        // Unlock Teleport
        this.addRenderableWidget(Button.builder(Component.literal("Unlock Teleport"),button -> {
            PacketDistributor.sendToServer(new UnlockRitualPayload("teleport"));
            RitualProgressHelper.unlock(this.getMinecraft().player, "teleport");
            }
                ).bounds(x + 100, y + 10, 80, 20).build()
        );

        // Lock Teleport
        this.addRenderableWidget(Button.builder(Component.literal(""), button -> {
            }
                ).bounds(x + 100, y + 35, 80, 20).build()
        );

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }


}