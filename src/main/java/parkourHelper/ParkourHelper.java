package parkourHelper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parkourHelper.util.KeyBindingHandler;
import parkourHelper.util.TickListener;

import java.io.File;

@SideOnly(Side.CLIENT)
@Mod(modid = ParkourHelper.MODID, version = ParkourHelper.VERSION, clientSideOnly = true, name = "ParkourHelper")
public class ParkourHelper {

    public static final String MODID = "parkourhelper";
    public static final String VERSION = "0.0.2";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    private static File configDir;

    public static final PathDrawer pathDrawer = new PathDrawer();
    public static final TickListener listener = new TickListener();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.log(Level.INFO, "preInit time");
        configDir = new File(event.getModConfigurationDirectory() + "/" + MODID);
        configDir.mkdirs();
        ConfigHandler.init(new File(configDir.getPath(), MODID + ".cfg"));

        //register keybinds for new, start, stop
        KeyBindingHandler.registerKeyBindings();
        MinecraftForge.EVENT_BUS.register(new KeyBindingHandler());
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        LOGGER.log(Level.WARN, "init time - registering keycommand");
        ClientCommandHandler.instance.registerCommand(new CommandHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.log(Level.INFO, "postinit time");
        MinecraftForge.EVENT_BUS.register(new CountingHUD(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(pathDrawer);
        MinecraftForge.EVENT_BUS.register(listener);
    }

    @Mod.EventHandler
    public void stop(FMLModDisabledEvent e) {
        LOGGER.log(Level.INFO, "Stopped mod.");
    }

}
