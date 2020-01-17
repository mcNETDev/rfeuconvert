package de.dwdev.rfeuconvert;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Logger;

import cofh.CoFHCore;
import cofh.core.init.CoreProps;
import de.dwdev.rfeuconvert.proxy.ProxyCommon;

@Mod(modid = Rfeuconvert.MODID, name = Rfeuconvert.NAME, version = Rfeuconvert.VERSION, dependencies = Rfeuconvert.DEPENDENCIES)
public class Rfeuconvert {
	public static final String MODID = "rfeuconvert";
	public static final String NAME = "rfeuconvert";
	public static final String VERSION = "1.0";

	public static final RFtoEUBlock blockrftoeu = new RFtoEUBlock();

	@SidedProxy(clientSide = "de.dwdev.rfeuconvert.proxy.ProxyClient", serverSide = "de.dwdev.rfeuconvert.proxy.ProxyCommon")
	public static ProxyCommon PROXY;

	@Mod.Instance
	public static Rfeuconvert INSTANCE;

	private static Logger logger;
	public static int multiplier = 4;
	public static int storage = 1000000;

	public static final String DEPENDENCIES = CoFHCore.VERSION_GROUP + "required-after:ic2";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		PROXY.preInit(event);
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		multiplier = config.getInt("multiplier", "general", 4, 1, 9999, "X RF/t = 1 EU/t");
		storage = config.getInt("rfstorage", "general", 1000000, 100000, Integer.MAX_VALUE, "RF Storage");
		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);
	}
}
