package parkourHelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import parkourHelper.ParkourHelper;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class Path {
    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);

    private ArrayList<Vec3> pathNodes;
    private int color = 0xFFAA00;

    public Path() {
        pathNodes = new ArrayList<Vec3>();
    }

    public Path(ArrayList<Vec3> nodes) {
        pathNodes = nodes;
    }

    public void addNode(Vec3 node) {
        pathNodes.add(node);
    }

    public ArrayList<Vec3> getNodes() {
        return pathNodes;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void drawPath(RenderWorldLastEvent event) {
        if (pathNodes.size() <= 1) return;

        try {
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.color(1F, 0F, 0F);

            WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();

            // Usually the player
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            //Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.partialTicks;
            //Apply 0-our transforms to set everything back to 0,0,0
            renderer.setTranslation(-d0, -d1, -d2);

            //GL_LINES makes dashed lines (depending on speed)
            renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

            Vec3 start = pathNodes.get(0);
            renderer.pos(start.xCoord, start.yCoord, start.zCoord).color(1.0F, 0F, 0F, 0.5F).endVertex();

            //connect to next node
            for (int i = 1; i < pathNodes.size(); i++) {
                Vec3 toNode = pathNodes.get(i);
                renderer.pos(toNode.xCoord, toNode.yCoord, toNode.zCoord).color(1F, 0F, 0F, 0.5F).endVertex();
            }

            LOGGER.log(Level.INFO, "drew" + renderer.getVertexCount() + " vertices, state: " + renderer.getVertexState());
            Tessellator.getInstance().draw();

            //unset the transition, making things normal again.
            renderer.setTranslation(0, 0, 0);
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        } catch (Exception e) {
            LOGGER.log(Level.FATAL, e);
        }
    }
}
