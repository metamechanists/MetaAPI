package org.metamechanists.metaapi.util;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import org.bukkit.entity.Player;
import org.metamechanists.metaapi.config.ConfigUtil;
import org.metamechanists.metaapi.config.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public class TextUtil {

    private static String prefix = "";

    public static void initialise() {
        String pluginColor = getLanguageEntry("general.plugin_color");
        prefix  = getLanguageEntry("general.prefix", "plugin_color", pluginColor);
    }

    private static Map<String, Object> getPlaceHolderMap(Object...args) {
        //Prepare the Map to Return
        Map<String, Object> returnMap = new HashMap<>();

        //Loop through the Provided Arguments going Every Other Argument
        int i = 0;
        boolean skip = false;
        for (Object object : args) {
            //If the Argument should not Be Skipped, Make sure it is a String
            //If it is add it to the Map
            if (!skip && object instanceof String string) {
                skip = true;
                i++;
                returnMap.put(string, args[i]);
            } else {
                skip = false;
                i++;
            }
        }

        return returnMap;
    }

    public static String fillPlaceholders(String message, Object... args) {
        //Create the Return Message
        String filledMessage = message;

        //Get the Map with the Keys to Values
        Map<String, Object> fillVars = getPlaceHolderMap(args);

        for (Map.Entry<String, Object> placeholderInfo : fillVars.entrySet()) {
            //Get Important Variables
            String replace = placeholderInfo.getKey();
            Object replaceWith = placeholderInfo.getValue();
            //Go Through Each Type an Auto Fill Can Be :D
            if (replaceWith instanceof Player player) {
                filledMessage = filledMessage.replace("{" + replace + "}", player.getName());
            } else if (replaceWith instanceof String string) {
                filledMessage = filledMessage.replace("{" + replace + "}", string);
            }
        }

        return filledMessage;
    }

    public static String getLanguageEntry(String path, Object...args) {
        // Load the message and check that it actually exists - if not, substitute an error message
        String entry = ConfigUtil.getString("language", ResourceLoader.getLanguage(), path);
        if (entry == null) {
            entry = " {player} '" + path + "' does not Exist";
        }
        String message = fillPlaceholders(prefix + entry, args);

        //Return a Colored Version of the attained Message
        return ChatColors.color(message);
    }
}
