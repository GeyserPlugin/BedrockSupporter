package ltd.rymc.bedrock.common.module;

import co.aikar.commands.PaperCommandManager;
import ltd.rymc.bedrock.common.config.ConfigBuilder;
import ltd.rymc.bedrock.common.config.ConfigManager;
import ltd.rymc.bedrock.plugin.BedrockSupporter;
import org.bukkit.ChatColor;

import java.io.File;

public class Module extends ModuleBase {

    public String getInfoString(){
        return moduleInfo.getInfoString(enabled ? ChatColor.GREEN : isModuleCanEnable(moduleInfo) ? ChatColor.RED : ChatColor.GOLD);
    }

    public ModuleLogger getLogger(){
        return moduleLogger;
    }

    public PaperCommandManager getCommandManager(){
        return BedrockSupporter.getCommandManager();
    }

    public File getDataFolder(){
        return moduleDataPath.getDataFolder();
    }

    public String getResourceDataPath() {
        return moduleDataPath.getResourceDataPath();
    }

    public String getName(){
        return moduleInfo.getName();
    }

    public ModuleInfo getModuleInfo(){
        return moduleInfo;
    }

    public <T> ConfigManager<T> defaultConfig(Class<T> configClass){
        return ConfigBuilder.builder(configClass,"config.yml").module(this).name("config").buildNormal();
    }

}
