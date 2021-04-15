package parkourHelper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigHandler {

    private static Logger LOGGER = LogManager.getLogger(ParkourHelper.MODID);
    private static File configFile;
    private static JsonObject jsonConfig;

    public static int xPos;
    public static int yPos;

    public static int lineWidth;
    public static int pointWidth;

    //todo: add saving for:
    public static boolean useRandomColorForNewPaths = false;

    public static void init(File file) {
        configFile = file;
        getConfig();
    }

    private static void getConfig() {
        if (configFile.exists()) {

            try {
                FileReader reader = new FileReader(configFile);

                JsonElement fileElement = new JsonParser().parse(reader);

                if (fileElement == null || fileElement.isJsonNull()) {
                    throw new JsonParseException("File is null!");
                }
                jsonConfig = fileElement.getAsJsonObject();
            } catch (Exception ex) {

                ex.printStackTrace();
                LOGGER.log(Level.FATAL, "ParkourHelper: There was an error loading the config. Resetting all settings to default.");
                addDefaultsAndSave();
                return;
            }

            try {
                if (jsonConfig.has("xPos")) {
                    xPos = jsonConfig.get("xPos").getAsInt();
                } else {
                    xPos = 120;
                }

                if (jsonConfig.has("yPos")) {
                    yPos = jsonConfig.get("yPos").getAsInt();
                } else {
                    yPos = 1;
                }

                if (jsonConfig.has("lineWidth")) {
                    lineWidth = jsonConfig.get("lineWidth").getAsInt();
                } else {
                    lineWidth = 5;
                }

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.log(Level.FATAL, "Error doing json config parse:" + e);
            }
        }
    }

    private static void addDefaultsAndSave() {
        xPos = 140;
        yPos = 1;
        lineWidth = 5;
        saveConfig();
    }

    public static void saveConfig() {
        jsonConfig = new JsonObject();

        try {
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            jsonConfig.addProperty("xPos", xPos);
            jsonConfig.addProperty("yPos", yPos);
            jsonConfig.addProperty("lineWidth", lineWidth);

            bufferedWriter.write(jsonConfig.toString());
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error saving config:" + e);
            e.printStackTrace();
        }
    }
}
