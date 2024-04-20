package ltd.rymc.bedrock.common.utils;

import ltd.rymc.bedrock.plugin.BedrockSupporter;

public class ModuleUtils {

    public static boolean hasModule(String... modules){
        for (String module : modules) if (hasModule(module)) return false;
        return true;
    }

    public static boolean hasModule(String module){
        return BedrockSupporter.getModuleManager().getModule(module) != null;
    }
}
