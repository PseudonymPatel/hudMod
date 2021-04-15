package parkourHelper;

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
import org.lwjgl.opengl.GL11;

public class CountingHUD extends Gui {

    private Minecraft mc;

    private int counts;
    private String prefix = "Jumps: ";
    private Boolean isJumping = true;

    //height and width of minecraft window
    private int height;
    private int width;

    public CountingHUD(Minecraft mc) {
        ScaledResolution scaled = new ScaledResolution(mc);
        this.width = scaled.getScaledWidth();
        this.height = scaled.getScaledHeight();
        this.mc = mc;

        this.counts = 0;
    }

    @SubscribeEvent
    public void drawGui(RenderGameOverlayEvent.Text event) {
        GL11.glPushMatrix();
        drawCenteredString(mc.fontRendererObj, (prefix + counts), ConfigHandler.xPos, ConfigHandler.yPos, Integer.parseInt("FFAA00", 16));
        GL11.glPopMatrix();
    }

    public void incrementCount() {
        counts++;
    }

    public void resetCount() {
        counts = 0;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.entity instanceof EntityPlayer) {
            if (Minecraft.getMinecraft().isSingleplayer()) {
                if (isJumping) {
                    incrementCount();
                    isJumping = false;
                } else {
                    isJumping = true;
                }
            } else {
                incrementCount();
            }
        }
    }
}
