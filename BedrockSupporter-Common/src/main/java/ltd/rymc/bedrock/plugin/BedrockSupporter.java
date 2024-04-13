package ltd.rymc.bedrock.plugin;

import co.aikar.commands.PaperCommandManager;
import ltd.rymc.bedrock.common.config.ConfigManager;
import ltd.rymc.bedrock.common.config.PluginNormalConfigManager;
import ltd.rymc.bedrock.common.metrics.Metrics;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.common.utils.PluginUtils;
import ltd.rymc.bedrock.plugin.commands.MainCommand;
import ltd.rymc.bedrock.plugin.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import space.arim.morepaperlib.MorePaperLib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BedrockSupporter extends JavaPlugin {

    private static BedrockSupporter instance;
    private static PaperCommandManager commandManager;
    private static ConfigManager<Config> config;
    private static MorePaperLib morePaperLib;

    public static BedrockSupporter getInstance() {
        return instance;
    }

    public static PaperCommandManager getCommandManager(){
        return commandManager;
    }

    public static MorePaperLib getMorePaperLib() {
        return morePaperLib;
    }

    private final Map<String,Module> modules = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        morePaperLib = new MorePaperLib(this);
        commandManager = new PaperCommandManager(this);

        if (!PluginUtils.hasPlugin("floodgate")) {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        commandManager.registerCommand(new MainCommand());

        Reflections reflections = new Reflections("ltd.rymc.bedrock.modules");
        Set<Class<? extends Module>> classes = reflections.getSubTypesOf(Module.class);
        for (Class<? extends Module> clazz : classes) {
            try {
                Module module = clazz.getDeclaredConstructor().newInstance();
                modules.put(module.getName(),module);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        config = PluginNormalConfigManager.create(this, "config", "config.yml", Config.class);

        config.reloadConfig();

        for (Module module : modules.values()) {
            module.enable();
        }

        if (config.getConfigData().metrics()){
            new Metrics(this, 16703);
        }
    }

    public void reload(){
        config.reloadConfig();

        for (Module module : modules.values()) {
            module.reload();
        }
    }

    @Override
    public void onDisable() {
        for (Module module : modules.values()) {
            module.disable();
        }
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules.values());
    }


    public List<String> getModulesName() {
        return new ArrayList<>(modules.keySet());
    }

    public boolean isModuleCanEnabled(String moduleName){
        Config.State state = config.getConfigData().state().get(moduleName);
        return state != null && state.enable();
    }
}