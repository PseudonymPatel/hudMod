package parkourHelper.util;

import net.minecraft.util.Vec3;

import java.awt.Color;

public class PathNode extends Vec3 {
    Color pathColor = new Color(0x910000);

    public PathNode(double x, double y, double z) {
        super(x, y, z);

    }

    public PathNode setColor(Color color) {
        this.pathColor = color;
        return this;
    }
}
