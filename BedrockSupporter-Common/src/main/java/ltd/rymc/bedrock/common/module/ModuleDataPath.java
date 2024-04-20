package ltd.rymc.bedrock.common.module;

import ltd.rymc.bedrock.plugin.BedrockSupporter;

import java.io.File;

public class ModuleDataPath {

    private final File dataFolder;
    private final String resourceDataPath;

    private ModuleDataPath(File dataFolder, String resourceDataPath){
        this.dataFolder = dataFolder;
        this.resourceDataPath = resourceDataPath;

    }

    public static ModuleDataPath of(ModuleInfo moduleInfo){

        File dataFolder = BedrockSupporter.getInstance().getDataFolder().toPath().resolve("modules").resolve(moduleInfo.getName()).toFile();
        String resourceDataPath = "modules/" + moduleInfo.getName() + "/";

        return new ModuleDataPath(dataFolder,resourceDataPath);
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public String getResourceDataPath() {
        return resourceDataPath;
    }
}
