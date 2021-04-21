package parkourHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parkourHelper.util.Path;
import parkourHelper.util.PathNode;
import scala.collection.parallel.ParIterableLike;

import java.awt.Color;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class PathDrawer {
    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);
    private static double MOVEMENT_THRESHOLD_FOR_NEW_PATH = 2;

    public boolean doPathDrawing = true;
    public boolean doPathTracking = false;

    private ArrayList<Path> paths = new ArrayList<Path>();

    private Vec3 prevPos = new Vec3(0,0,0);
    private Vec3 prevJumpPos = new Vec3(0,0,0);

    private Color colorToUse = new Color(0x910000);
    private Color inAirColor = new Color(0xFF0000);
    private Path currentPath;

    public PathDrawer() {
        Path newPath = new Path();
        paths.add(newPath);
        currentPath = newPath;
    }

    //todo: do line stuff when: player changes dimension,
    @SubscribeEvent
    public void renderPath(RenderWorldLastEvent event) {
        //another version of player to get
//        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
//        if (player == null) return;

        if (doPathDrawing && paths.size() > 0) {
            for (Path path : paths) {
                path.drawPath(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase player = event.entityLiving;
        if (player instanceof EntityPlayer && doPathTracking && currentPath != null) {

            if (player.getName() != Minecraft.getMinecraft().thePlayer.getName()) return;

            PathNode playerLocation = new PathNode(player.posX, player.posY, player.posZ);

            if (prevPos.distanceTo(playerLocation) > MOVEMENT_THRESHOLD_FOR_NEW_PATH) {
                startNewPath();
            }

            if (player.onGround) {
                playerLocation.setColor(colorToUse);
            } else {
                playerLocation.setColor(inAirColor);
            }

            if (areVectorsNotEqual(playerLocation, prevPos)) {
                currentPath.addNode(playerLocation);
                prevPos = playerLocation;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer && doPathTracking) {
            Vec3 playerPos = new Vec3(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
            if (areVectorsNotEqual(playerPos, prevJumpPos)) {
                currentPath.addJumpNode(playerPos);
                prevJumpPos = playerPos;
            }
        }
    }

    public void startNewPath() {
        if (ConfigHandler.useRandomColorForNewPaths) {
            colorToUse = Color.getHSBColor((float) Math.random(), 1, 1);
        } else {
            colorToUse = new Color(0x0C0091);
        }
        if (currentPath.getNodes().size() > 0) {
            Path newPath = new Path();
            paths.add(newPath);
            currentPath = newPath;
        }
    }

    public static void togglePathTracking(EntityPlayerSP player) {
        if (ParkourHelper.pathDrawer.doPathTracking) {
            ParkourHelper.pathDrawer.doPathTracking = false;
            player.addChatMessage(new ChatComponentText("Stopped tracking path"));
        } else {
            ParkourHelper.pathDrawer.startNewPath();
            ParkourHelper.pathDrawer.doPathTracking = true;
            player.addChatMessage(new ChatComponentText("Started tracking new path"));
        }
    }

    private boolean areVectorsNotEqual(Vec3 a, Vec3 b) {
        return (a.squareDistanceTo(b) > 0.00000001); //square distance for less calculations per frame
    }
}
