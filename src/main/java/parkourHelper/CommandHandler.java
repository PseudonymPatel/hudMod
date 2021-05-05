package parkourHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class CommandHandler extends CommandBase {

    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);

    private boolean doingPath = false;

    @Override
    public String getCommandName() {
        return ParkourHelper.MODID;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("pkh");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pkh set <variable> <value>\n" +
                "Setable values: xPos, yPos, lineWidth, pointWidth\n" +
                "\txPos and yPos set the location of the jump counter\n" +
                "\tpointWidth is the size of the dots made when you jump\n" +
                "/pkh toggleDraw -- shows or hides paths\n" +
                "/pkh start -- starts a new path\n" +
                "/pkh stop -- stops tracking the current path\n" +
                "/pkh toggle -- stops tracking a path if tracking one, or starts tracking if no current path\n\n" +
                "**You can also use numpad 0 (configureable in controls) to toggle.**";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("set")) {
                Integer number = null;
                if (args.length >= 3) {
                    try {
                        number = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ex) {
                        throw new CommandException("message.notUse");
                    }
                }
                if (number == null) {
                    throw new CommandException("message.failed");
                }

                if (args[1].equalsIgnoreCase("xPos")) {
                    ConfigHandler.xPos = number;
                    ConfigHandler.saveConfig();
                    LOGGER.log(Level.INFO, "saving xPos");
                } else if (args[1].equalsIgnoreCase("yPos")) {
                    ConfigHandler.yPos = number;
                    ConfigHandler.saveConfig();
                    LOGGER.log(Level.INFO, "saving yPos");
                } else if (args[1].equalsIgnoreCase("lineWidth")) {
                    ConfigHandler.lineWidth = number;
                    ConfigHandler.saveConfig();
                    LOGGER.log(Level.INFO, "saving lineWidth");
                } else if (args[1].equalsIgnoreCase("pointWidth")) {
                    ConfigHandler.pointWidth = number;
                    ConfigHandler.saveConfig();
                    LOGGER.log(Level.INFO, "saving point width");
                } else {
                    LOGGER.log(Level.ERROR, "Key was wrong, not xPos or yPos");
                    throw new CommandException("message.invalKey");
                }
                player.addChatMessage(new ChatComponentText("Attempted to change " + args[1] + " to " + args[2]));


            } else if (args[0].equalsIgnoreCase("toggleDraw")) {
                ParkourHelper.pathDrawer.doPathDrawing = !ParkourHelper.pathDrawer.doPathDrawing;
                player.addChatMessage(new ChatComponentText("Toggled path drawing. Now " + (ParkourHelper.pathDrawer.doPathDrawing ? "on"  : "off")));

//            } else if (args[0].equalsIgnoreCase("newPath")) {
//                ParkourHelper.pathDrawer.startNewPath();
//                player.addChatMessage(new ChatComponentText(I18n.format("message.startedTracking")));
//
//            } else if (args[0].equalsIgnoreCase("toggleTrack")) {
//                ParkourHelper.pathDrawer.doPathTracking = !ParkourHelper.pathDrawer.doPathTracking;
//                player.addChatMessage(new ChatComponentText("Toggled path tracking. Now " + (ParkourHelper.pathDrawer.doPathTracking ? "on"  : "off")));


            } else if (args[0].equalsIgnoreCase("start")) {
                ParkourHelper.pathDrawer.startNewPath();
                ParkourHelper.pathDrawer.doPathTracking = true;
                player.addChatMessage(new ChatComponentText(I18n.format("message.startedTracking")));

            } else if (args[0].equalsIgnoreCase("stop")) {
                ParkourHelper.pathDrawer.doPathTracking = false;
                player.addChatMessage(new ChatComponentText(I18n.format("message.stoppedTracking")));

            } else if (args[0].equalsIgnoreCase("toggle")) {
                PathDrawer.togglePathTracking(player);

            } else if (args[0].equalsIgnoreCase("openSettings")) {
                ParkourHelper.listener.shouldOpenGui = true;

            } else {
                player.addChatMessage(new ChatComponentText(getCommandUsage(null)));
            }
        } else {
            player.addChatMessage(new ChatComponentText(getCommandUsage(null)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
