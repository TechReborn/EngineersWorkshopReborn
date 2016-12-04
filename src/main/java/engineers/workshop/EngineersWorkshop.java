package engineers.workshop;

import engineers.workshop.items.ItemUpgrade;
import engineers.workshop.proxies.CommonProxy;
import engineers.workshop.table.BlockTable;
import engineers.workshop.table.TileTable;
import engineers.workshop.util.Logger;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "engineersworkshop")
public class EngineersWorkshop {

	@SidedProxy(serverSide = "engineers.workshop.proxies.CommonProxy", clientSide = "engineers.workshop.proxies.ClientProxy")
	public static CommonProxy proxy;

	public static Item itemUpgrade;
	public static Block blockTable;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		itemUpgrade = new ItemUpgrade();
		blockTable  = new BlockTable();
		GameRegistry.registerTileEntity(TileTable.class, "engineersworkshop:table");
		proxy.preInit();
		
		Logger.debug("FMLPreInitialization done.");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		Logger.debug("FMLInitialization done.");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
		Logger.debug("FMLPostInitialization done.");
	}

	// Creative Tab
	public static CreativeTabs tabWorkshop = new CreativeTabs("engineersworkshop") {
		@Override
		public Item getTabIconItem() {
			return itemUpgrade;
		}
	};

}
