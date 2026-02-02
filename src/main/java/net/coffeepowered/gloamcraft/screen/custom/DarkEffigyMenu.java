package net.coffeepowered.gloamcraft.screen.custom;

import net.coffeepowered.gloamcraft.block.ModBlocks;
import net.coffeepowered.gloamcraft.block.entity.DarkEffigyBlockEntity;
import net.coffeepowered.gloamcraft.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class DarkEffigyMenu extends AbstractContainerMenu {
    public final DarkEffigyBlockEntity blockEntity;
    public final Level level;

    public DarkEffigyMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public DarkEffigyMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.DARKEFFIGY_MENU.get(), containerId);
        this.blockEntity = ((DarkEffigyBlockEntity) blockEntity);
        this.level = inv.player.level();


        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 80, 35));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 90, 35));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 100, 35));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 3, 110, 35));
    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.DARK_EFFIGY.get());
    }
}
