package ltd.rymc.bedrock.common.module;

import co.aikar.commands.PaperCommandManager;
import ltd.rymc.bedrock.plugin.BedrockSupporter;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.logging.Logger;

public class Module extends ModuleBase {

    protected File dataFolder = plugin.getDataFolder().toPath().resolve("modules").resolve(moduleInfo.getName()).toFile();
    protected String dataPath = "modules/" + moduleInfo.getName() + "/";

    public String getInfoString() {
        StringBuilder builder = new StringBuilder();
        builder.append(enabled ? ChatColor.GREEN : isModuleCanEnable(moduleInfo) ? ChatColor.RED : ChatColor.GOLD);
        builder.append(moduleInfo.getName()).append(ChatColor.WHITE).append(" version ").append(moduleInfo.getVersion()).append(" by ");
        String[] authors = moduleInfo.getAuthors();
        for (int i = 0, j = authors.length; i < j; i++) {
            String author = authors[i];
            builder.append(author);
            if (i + 1 < j) builder.append(", ");
        }
        return builder.toString();
    }

    public Logger getLogger(){
        return plugin.getLogger();
    }

    public PaperCommandManager getCommandManager(){
        return BedrockSupporter.getCommandManager();
    }

    public File getDataFolder(){
        return dataFolder;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getName(){
        return moduleInfo.getName();
    }

    public ModuleInfo getModuleInfo(){
        return moduleInfo;
    }

}
