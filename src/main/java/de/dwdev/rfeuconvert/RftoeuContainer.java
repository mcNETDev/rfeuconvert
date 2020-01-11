package de.dwdev.rfeuconvert;

import ic2.api.item.IC2Items;
import ic2.core.gui.SlotGrid.SlotStyle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RftoeuContainer extends Container {

	private TileRFtoEU te;

	public RftoeuContainer(IInventory playerInventory, TileRFtoEU te) {
		this.te = te;
		addOwnSlots(playerInventory);
		addPlayerSlots(playerInventory);
	}

	private void addOwnSlots(IInventory playerInventory) {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 80, 36));

	}

	private void addPlayerSlots(IInventory playerInventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 84;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		}

		for (int ii = 0; ii < 9; ++ii) {
			int x = 8 + ii * 18;
			int y = 142;
			this.addSlotToContainer(new Slot(playerInventory, ii, x, y));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);
		if (!slot.getHasStack()) {
			return ItemStack.EMPTY;
		}
		ItemStack slot_stack = slot.getStack();
		int stack_count = slot_stack.getCount();
		ItemStack transformer = IC2Items.getItem("upgrade", "transformer");
		if (!slot_stack.getUnlocalizedName().equalsIgnoreCase(transformer.getUnlocalizedName())) {
			return ItemStack.EMPTY;
		}
		if (index == 0) {
			mergeItemStack(slot.getStack(), 1, 36, false);
			te.updateTier();
			return ItemStack.EMPTY;

		} else {
			Slot inv_slot = this.inventorySlots.get(0);
			if (inv_slot.getHasStack()) {
				ItemStack inv_stack = inv_slot.getStack();
				if (inv_stack.getCount() == 4) {
					return ItemStack.EMPTY;
				}
				int inv_count = inv_stack.getCount();
				if (inv_count + stack_count > 4) {
					slot_stack.setCount((inv_count + stack_count) - 4);
					inv_stack.setCount(4);
				} else if (inv_count + stack_count < 4) {
					inv_stack.setCount(inv_count + stack_count);
					slot_stack.setCount(0);
				} else if (inv_count + stack_count == 4) {
					inv_stack.setCount(4);
					slot_stack.setCount(0);
				}
				inv_slot.onSlotChanged();
				slot.onSlotChanged();
			} else {

				int put = Math.min(stack_count, 4);
				transformer.setCount(put);
				slot_stack.setCount(stack_count - put);
				inv_slot.putStack(transformer);
				inv_slot.onSlotChanged();
				slot.onSlotChanged();
			}

		}
		te.updateTier();

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}

}
