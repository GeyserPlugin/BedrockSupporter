package ltd.rymc.bedrock.common.config.loggers;

import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class PluginConfigLogger implements ConfigLogger {

    private final Logger logger;

    private PluginConfigLogger(Logger logger){
        this.logger = logger;
    }

    public static ConfigLogger of(Logger logger){
        return new PluginConfigLogger(logger);
    }

    public static ConfigLogger of(Plugin plugin){
        return new PluginConfigLogger(plugin.getLogger());
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warning(String message) {
        logger.warning(message);
    }

    @Override
    public void severe(String message) {
        logger.severe(message);
    }
}
