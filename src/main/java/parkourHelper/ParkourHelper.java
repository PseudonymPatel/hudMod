package parkourHelper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@SideOnly(Side.CLIENT)
@Mod(modid = ParkourHelper.MODID, version = ParkourHelper.VERSION)
public class ParkourHelper {

    public static final String MODID = "parkourhelper";
    public static final String VERSION = "0.0.2";

    private static Logger LOGGER = LogManager.getLogger(MODID);
    private static File configDir;

    public static PathDrawer pathDrawer = new PathDrawer();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.log(Level.INFO, "preInit time");
        configDir = new File(event.getModConfigurationDirectory() + "/" + MODID);
        configDir.mkdirs();
        ConfigHandler.init(new File(configDir.getPath(), MODID + ".cfg"));
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        LOGGER.log(Level.WARN, "init time - registering keycommand");
        ClientCommandHandler.instance.registerCommand(new CommandHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.log(Level.INFO, "postinit time");
        MinecraftForge.EVENT_BUS.register(new CountingGui(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(pathDrawer);
    }
}
