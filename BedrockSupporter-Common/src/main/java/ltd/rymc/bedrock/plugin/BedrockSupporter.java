package ltd.rymc.bedrock.plugin;

import co.aikar.commands.PaperCommandManager;
import ltd.rymc.bedrock.common.config.ConfigManager;
import ltd.rymc.bedrock.common.config.PluginNormalConfigManager;
import ltd.rymc.bedrock.common.metrics.Metrics;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.common.module.ModuleManager;
import ltd.rymc.bedrock.common.utils.PluginUtils;
import ltd.rymc.bedrock.plugin.commands.MainCommand;
import ltd.rymc.bedrock.plugin.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.morepaperlib.MorePaperLib;

import java.lang.reflect.InvocationTargetException;

public class BedrockSupporter extends JavaPlugin {

    private static BedrockSupporter instance;
    private static PaperCommandManager commandManager;
    private static ConfigManager<Config> config;
    private static MorePaperLib morePaperLib;
    private static ModuleManager moduleManager;

    public static BedrockSupporter getInstance() {
        return instance;
    }

    public static PaperCommandManager getCommandManager(){
        return commandManager;
    }

    public static MorePaperLib getMorePaperLib() {
        return morePaperLib;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        morePaperLib = new MorePaperLib(this);
        commandManager = new PaperCommandManager(this);

        if (!PluginUtils.hasPlugin("floodgate")) {
            Bukkit.getPluginManager().disablePlugin(this);
        }



        commandManager.registerCommand(new MainCommand());
        moduleManager = new ModuleManager(this);

        for (Class<? extends Module> clazz : moduleManager.getBuiltInModule()) {
            try {
                moduleManager.loadModule(clazz);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        config = PluginNormalConfigManager.create(this, "config", "config.yml", Config.class);

        config.reloadConfig();

        moduleManager.enableAll();



        if (config.getConfigData().metrics()){
            new Metrics(this, 16703);
        }
    }

    public void reload(){
        config.reloadConfig();

        moduleManager.reloadAll();
    }

    @Override
    public void onDisable() {
        moduleManager.disableAll();
    }

    public Config getPluginConfig() {
        return config.getConfigData();
    }
}