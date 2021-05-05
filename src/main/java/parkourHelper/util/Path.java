package parkourHelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import parkourHelper.ConfigHandler;
import parkourHelper.ParkourHelper;

import java.awt.*;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class Path {
    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);

    private ArrayList<PathNode> pathNodes = new ArrayList<PathNode>();
    private ArrayList<Vec3> jumpNodes = new ArrayList<Vec3>();

    public Color pathColor = new Color(0x910000);
    private Color jumpColor = new Color(0x3EFF00);

    public Path() {

    }

    public Path(Color color) {
        this.pathColor = color;
    }

    public void addNode(PathNode node) {
        pathNodes.add(node);
    }

    public void addJumpNode(Vec3 node) {
        jumpNodes.add(node);
    }

    public ArrayList<PathNode> getNodes() {
        return pathNodes;
    }

    public void setColor(int colorHex) {
        this.pathColor = new Color(colorHex);
    }
    public void setColor(Color color) {
        this.pathColor = color;
    }

    public void drawPath(RenderWorldLastEvent event) {
        if (pathNodes.size() <= 1) return;

        try {
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

            // GL settings to configure
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glDisable(GL11.GL_TEXTURE_2D);

            //GlStateManager.color(pathColor.getRed(), pathColor.getGreen(), pathColor.getBlue());

            WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();

            // Usually the player
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            //Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.partialTicks;
            //Apply 0-our transforms to set everything back to 0,0,0
            renderer.setTranslation(-d0, -d1, -d2);

            //---- Draw path line
            //GL_LINES makes dashed lines (depending on speed)
            renderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            GL11.glLineWidth((float) ConfigHandler.lineWidth);

            PathNode start = pathNodes.get(0);
            renderer.pos(start.xCoord, start.yCoord + 0.001F, start.zCoord).color(start.pathColor.getRed(), start.pathColor.getGreen(), start.pathColor.getBlue(), start.pathColor.getAlpha()).endVertex();

            //connect to next node
            for (int i = 1; i < pathNodes.size(); i++) {
                PathNode toNode = pathNodes.get(i);
                renderer.pos(toNode.xCoord, toNode.yCoord + 0.001F, toNode.zCoord).color(toNode.pathColor.getRed(), toNode.pathColor.getGreen(), toNode.pathColor.getBlue(), toNode.pathColor.getAlpha()).endVertex();
            }

            Tessellator.getInstance().draw();

            //---- Draw jump dots
            if (jumpNodes.size() > 0) {
                renderer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
                GL11.glPointSize((float) ConfigHandler.pointWidth);

                for (Vec3 point : jumpNodes) {
                    renderer.pos(point.xCoord, point.yCoord, point.zCoord).color(jumpColor.getRed(), jumpColor.getGreen(), jumpColor.getBlue(), jumpColor.getAlpha()).endVertex();
                }

                Tessellator.getInstance().draw();
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "[ParkourHelper] Problem drawing paths: " + e);
            e.printStackTrace();

            WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();

            try {
                renderer.finishDrawing(); //make sure to clean up for other things.
            } catch (IllegalStateException err) {
                LOGGER.log(Level.ERROR, "[ParkourHelper] Error trying to clean up after erroring: " + err.getMessage());
            }
        } finally {
            WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();

            //unset the transition, making things normal again.
            renderer.setTranslation(0, 0, 0);

            //unconfigure our settings
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
