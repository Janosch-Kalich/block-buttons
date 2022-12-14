package janoschkalich.blockbuttons.Screenhandler;

import janoschkalich.blockbuttons.Blockbuttons;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BlockButtonScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public BlockButtonScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public BlockButtonScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Blockbuttons.BLOCK_BUTTON_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 80, 35));

        int m;
        int l;

        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    public boolean onButtonClick(PlayerEntity player, int id) {
        System.out.println(id);
        return true;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();

            newStack = originalStack.copy();
                if (invSlot < this.inventory.size()) {
                    if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                    return ItemStack.EMPTY;
                }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        inventory.onClose(player);
    }
}
