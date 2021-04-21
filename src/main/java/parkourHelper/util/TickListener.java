package parkourHelper.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import parkourHelper.PathGUI;

public class TickListener {

    public boolean shouldOpenGui = false;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (shouldOpenGui) {
            Minecraft.getMinecraft().displayGuiScreen(new PathGUI());
            shouldOpenGui = false;
        }
    }
}
