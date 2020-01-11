package de.dwdev.rfeuconvert.proxy;

import de.dwdev.rfeuconvert.GUIProxy;
import de.dwdev.rfeuconvert.RFtoEUBlock;
import de.dwdev.rfeuconvert.Rfeuconvert;
import de.dwdev.rfeuconvert.TileRFtoEU;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

import static cofh.core.util.helpers.RecipeHelper.*;

public class ProxyCommon {
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> registry) {
		registry.getRegistry().register(Rfeuconvert.blockrftoeu);
		GameRegistry.registerTileEntity(TileRFtoEU.class, Rfeuconvert.MODID + ":rftoeu");
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> registry) {
		registry.getRegistry().register(
				new ItemBlock(Rfeuconvert.blockrftoeu).setRegistryName(Rfeuconvert.blockrftoeu.getRegistryName()));
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ItemStack transformer = IC2Items.getItem("upgrade", "transformer");
		ItemStack glasfiber = IC2Items.getItem("cable", "type:glass,insulation:0");
		ItemStack coppercable = IC2Items.getItem("cable", "type:copper,insulation:1");
		ItemStack advc = IC2Items.getItem("crafting", "advanced_circuit");
		ItemStack basc = IC2Items.getItem("crafting", "circuit");

		addShapedRecipe(new ItemStack(Rfeuconvert.blockrftoeu), "TFT", "ARN", "TCT", 'T', transformer, 'F', glasfiber,
				'A', advc, 'R', new ItemStack(Blocks.REDSTONE_BLOCK), 'N', basc, 'C', coppercable);
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Rfeuconvert.INSTANCE, new GUIProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
}
