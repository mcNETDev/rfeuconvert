package de.dwdev.rfeuconvert;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.IC2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileRFtoEU extends TileEntity implements ITickable, IEnergySource {

	private IEnergyStorage energy = new EnergyStorage(1000000);
	private int tier = 1;
	private ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
		protected void onContentsChanged(int slot) {
			TileRFtoEU.this.updateTier();
			TileRFtoEU.this.markDirty();
		};

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (stack.getUnlocalizedName()
					.equalsIgnoreCase(IC2Items.getItem("upgrade", "transformer").getUnlocalizedName())) {

				return true;
			} else {
				return false;
			}
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return 4;
		}
	};

	public TileRFtoEU() {
	}

	protected void updateTier() {
		if (itemStackHandler.getStackInSlot(0) != null) {
			tier = itemStackHandler.getStackInSlot(0).getCount() + 1;
		} else {
			tier = 1;
		}
	}

	public TileRFtoEU(int meta) {
		this.tier = meta;
	}

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("energy", energy.getEnergyStored());
		tag.setInteger("tier", tier);
		tag.setTag("items", itemStackHandler.serializeNBT());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("energy")) {
			energy.receiveEnergy(tag.getInteger("energy"), false);
		}
		if (tag.hasKey("tier")) {
			tier = tag.getInteger("tier");
		}
		if (tag.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound) tag.getTag("items"));
		}
	}

	// GUI
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	// RF Interactions
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energy);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		return super.getCapability(capability, facing);
	}

	// IC2
	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return true;
	}

	@Override
	public double getOfferedEnergy() {
		return energy.getEnergyStored() / Rfeuconvert.multiplier;
	}

	@Override
	public void drawEnergy(double amount) {
		int ene = energy.extractEnergy((int) ((amount * Rfeuconvert.multiplier) - 1), false);
	}

	@Override
	public int getSourceTier() {
		return tier;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
	}

	@Override
	public void invalidate() {
		super.invalidate();
		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
	}
	// IC2 ENDE
}
