package parkourHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import parkourHelper.util.Path;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class PathDrawer {
    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);

    boolean doPathDrawing = true;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private Vec3 prevPos = new Vec3(0,0,0);

    private Path currentPath;

    public PathDrawer() {
        Path newPath = new Path();
        paths.add(newPath);
        currentPath = newPath;
    }

    //todo: do line stuff when: player changes dimension,
    @SubscribeEvent
    public void renderPath(RenderWorldLastEvent event) {
        if (!Minecraft.getMinecraft().inGameHasFocus) return;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        if (doPathDrawing && paths.size() > 0) {
            Vec3 playerLocation = new Vec3(player.posX, player.posY, player.posZ);

            if (!areVectorsEqual(playerLocation, prevPos)) {
                currentPath.addNode(playerLocation);
                prevPos = playerLocation;
            }
            paths.get(0).drawPath(event);
        }
    }

    //todo: test this (3/14/21)
    public void startNewPath() {
        Path newPath = new Path();
        paths.add(newPath);
        currentPath = newPath;
    }

    private boolean areVectorsEqual(Vec3 a, Vec3 b) {
        return (areDoublesEqualish(a.xCoord, b.xCoord) && areDoublesEqualish(a.yCoord, b.yCoord) && areDoublesEqualish(a.zCoord, b.zCoord));
    }
    private boolean areDoublesEqualish(double a, double b) {
        return (Math.abs(a - b) < 0.0000001);
    }
}
