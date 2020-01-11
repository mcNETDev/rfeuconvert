package de.dwdev.rfeuconvert;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RFtoEUBlock extends Block implements ITileEntityProvider {

	public static final IProperty<Integer> tier = PropertyInteger.create("tier", 1, 5);
	public static final int GUI_ID = 1;

	public RFtoEUBlock() {
		super(Material.ROCK);
		setRegistryName(Rfeuconvert.MODID, "rftoeu");
		setUnlocalizedName(Rfeuconvert.MODID + ".rftoeu");
		setCreativeTab(CreativeTabs.TOOLS);
		setHardness(2);
		setDefaultState(this.blockState.getBaseState().withProperty(tier, 1));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileRFtoEU(meta + 1);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, tier);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(tier) - 1;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return blockState.getBaseState().withProperty(tier, meta + 1);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(tier) - 1;
	}

	public ItemBlock getItemBlock() {
		ItemBlock result = new ItemBlock(this) {
			public int getMetadata(int damage) {
				return damage;
			};
		};
		result.setRegistryName(getRegistryName());
		return result;
	}

//	@Override
//	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
//		items.add(new ItemStack(this, 1, 0));
//		items.add(new ItemStack(this, 1, 1));
//		items.add(new ItemStack(this, 1, 2));
//		items.add(new ItemStack(this, 1, 3));
//		items.add(new ItemStack(this, 1, 4));
//	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileRFtoEU)) {
			return false;
		}
		player.openGui(Rfeuconvert.INSTANCE, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

}
