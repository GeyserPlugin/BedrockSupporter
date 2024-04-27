package ltd.rymc.bedrock.common.config.loggers;

import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.common.module.ModuleLogger;

public class ModuleConfigLogger implements ConfigLogger {

    private final ModuleLogger logger;

    private ModuleConfigLogger(ModuleLogger logger){
        this.logger = logger;
    }

    public static ConfigLogger of(ModuleLogger logger){
        return new ModuleConfigLogger(logger);
    }

    public static ConfigLogger of(Module module){
        return new ModuleConfigLogger(module.getLogger());
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
