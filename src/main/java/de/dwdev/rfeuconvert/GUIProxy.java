package de.dwdev.rfeuconvert;

import ic2.api.item.IC2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileRFtoEU) {
        	return new RftoeuContainer(player.inventory, (TileRFtoEU) te);
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRFtoEU) {
        	TileRFtoEU containerTileEntity = (TileRFtoEU) te;
            return new RftoeuGUIContainer(containerTileEntity, new RftoeuContainer(player.inventory, containerTileEntity));
        }
        return null;
	}

}
