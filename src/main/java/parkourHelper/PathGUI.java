package parkourHelper;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;

public class PathGUI extends GuiScreen {

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, 50, 50, "True"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
