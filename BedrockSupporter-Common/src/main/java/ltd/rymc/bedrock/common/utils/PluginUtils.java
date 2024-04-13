package ltd.rymc.bedrock.common.utils;

import org.bukkit.Bukkit;


@SuppressWarnings("unused")
public class PluginUtils {
    public static boolean hasPlugin(String... plugins){
        for (String plugin : plugins) if (hasPlugin(plugin)) return false;
        return true;
    }

    public static boolean hasPlugin(String plugin){
        return Bukkit.getPluginManager().getPlugin(plugin) != null;
    }
}
