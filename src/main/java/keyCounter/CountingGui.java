package keyCounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CountingGui extends Gui {

    private Minecraft mc;

    private int counts;
    private String prefix = "Jumps: ";
    private Boolean isJumping = true;

    //height and width of minecraft window
    private int height;
    private int width;

    //position of gui on screen
    private int xPos;
    private int yPos;

    public CountingGui(Minecraft mc) {
        ScaledResolution scaled = new ScaledResolution(mc);
        this.width = scaled.getScaledWidth();
        this.height = scaled.getScaledHeight();
        this.mc = mc;

        this.counts = 0;

        //todo: load from config
        this.xPos = 100;
        this.yPos = 0;
    }

    @SubscribeEvent
    public void drawGui(RenderGameOverlayEvent.Post event) {
        drawCenteredString(mc.fontRendererObj, (prefix + counts), xPos, yPos, Integer.parseInt("FFAA00", 16));
    }

    public void incrementCount() {
        counts++;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.entity instanceof EntityPlayer) {
            if (isJumping) {
                incrementCount();
                isJumping = false;
            } else {
                isJumping = true;
            }
        }
    }
}
