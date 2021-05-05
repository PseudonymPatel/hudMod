package parkourHelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import parkourHelper.ParkourHelper;
import parkourHelper.PathDrawer;

public class KeyBindingHandler {

    private static KeyBinding toggleKey;
    private static KeyBinding startKey;
    private static KeyBinding stopKey;

    public static void registerKeyBindings() {
        toggleKey = new KeyBinding("key.config.toggleTrackKey", Keyboard.KEY_NUMPAD0, "key.pkh");
        startKey = new KeyBinding("key.config.startTrackKey", Keyboard.KEY_NUMPAD2, "key.pkh");
        stopKey = new KeyBinding("key.config.stopTrackKey", Keyboard.KEY_NUMPAD3, "key.pkh");

        ClientRegistry.registerKeyBinding(toggleKey);
        ClientRegistry.registerKeyBinding(startKey);
        ClientRegistry.registerKeyBinding(stopKey);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

            if (toggleKey.isPressed()) {
                PathDrawer.togglePathTracking(player);
            } else if (startKey.isPressed()) {
                ParkourHelper.pathDrawer.startNewPath();
                ParkourHelper.pathDrawer.doPathTracking = true;
                player.addChatMessage(new ChatComponentText(I18n.format("message.startedTracking")));
            } else if (stopKey.isPressed()) {
                ParkourHelper.pathDrawer.doPathTracking = false;
                player.addChatMessage(new ChatComponentText(I18n.format("message.stoppedTracking")));
            }
        }
    }
}
