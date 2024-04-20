package ltd.rymc.bedrock.common.module;

import ltd.rymc.bedrock.plugin.BedrockSupporter;

import java.util.logging.Logger;

public class ModuleLogger {

    private static Logger logger = BedrockSupporter.getInstance().getLogger();

    private String prefix;
    private final String defaultPrefix;

    private ModuleLogger(String prefix){
        this.prefix = prefix;
        this.defaultPrefix = prefix;
    }

    public static ModuleLogger of(ModuleInfo moduleInfo){
        return new ModuleLogger("[" + moduleInfo.getName() + "] ");
    }

    public void info(String message){
        logger.info(prefix + message);
    }

    public void warning(String message){
        logger.warning(prefix + message);
    }

    public void severe(String message){
        logger.severe(prefix + message);
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public void resetPrefix(){
        prefix = defaultPrefix;
    }


}
