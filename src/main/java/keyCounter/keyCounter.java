package keyCounter;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = keyCounter.MODID, version = keyCounter.VERSION)
public class keyCounter {
    public static final String MODID = "keycounter";
    public static final String VERSION = "0.0.1";

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CountingGui(Minecraft.getMinecraft()));
    }
}
