package ltd.rymc.bedrock.common.config;

import ltd.rymc.bedrock.common.config.managers.NormalConfigManager;
import ltd.rymc.bedrock.common.config.managers.ResourceConfigManager;
import ltd.rymc.bedrock.common.config.loggers.ConfigLogger;
import ltd.rymc.bedrock.common.config.loggers.ModuleConfigLogger;
import ltd.rymc.bedrock.common.config.loggers.PluginConfigLogger;
import ltd.rymc.bedrock.common.config.loggers.SystemConfigLogger;
import ltd.rymc.bedrock.common.module.Module;
import org.bukkit.plugin.Plugin;
import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@SuppressWarnings("unused")
public class ConfigBuilder<T> {

    private final Class<T> configClass;
    private final String configPath;

    private String configName;
    private Path configFolderPath = Paths.get(System.getProperty("user.dir"));
    private ConfigLogger configLogger = SystemConfigLogger.of();
    private SnakeYamlOptions.Builder snakeYamlOptions = new SnakeYamlOptions.Builder();
    private ConfigurationOptions.Builder configurationOptions = new ConfigurationOptions.Builder();

    private String baseResourcePath = "";

    public static <T> ConfigBuilder<T> builder(Class<T> configClass, String configPath){
        Objects.requireNonNull(configClass, "configClass");
        Objects.requireNonNull(configPath, "configPath");
        return new ConfigBuilder<>(configClass, configPath);
    }


    public ConfigBuilder(Class<T> configClass, String configPath){
        this.configClass = configClass;
        this.configPath = configPath;

        this.configName = configPath;
        configurationOptions.sorter(new AnnotationBasedSorter());
        snakeYamlOptions.commentMode(CommentMode.alternativeWriter("%s"));
    }

    public ConfigBuilder<T> module(Module module){
        Objects.requireNonNull(module, "module");
        configFolderPath = module.getDataFolder().toPath();
        baseResourcePath = module.getResourceDataPath();
        configLogger = ModuleConfigLogger.of(module);
        return this;
    }

    public ConfigBuilder<T> plugin(Plugin plugin){
        Objects.requireNonNull(plugin, "plugin");
        configFolderPath = plugin.getDataFolder().toPath();
        configLogger = PluginConfigLogger.of(plugin);
        return this;
    }

    public ConfigBuilder<T> name(String name){
        Objects.requireNonNull(name, "name");
        configName = name;
        return this;
    }

    public ConfigBuilder<T> snakeYamlOptions(SnakeYamlOptions.Builder snakeYamlOptions){
        Objects.requireNonNull(snakeYamlOptions, "snakeYamlOptions");
        this.snakeYamlOptions = snakeYamlOptions;
        return this;
    }

    public ConfigBuilder<T> configurationOptions(ConfigurationOptions.Builder configurationOptions){
        Objects.requireNonNull(configurationOptions, "configurationOptions");
        this.configurationOptions = configurationOptions;
        return this;
    }

    public NormalConfigManager<T> buildNormal(){

        ConfigurationFactory<T> configFactory = SnakeYamlConfigurationFactory.create(
                configClass,
                configurationOptions.build(),
                snakeYamlOptions.build()
        );

        return new NormalConfigManager<>(
                new ConfigurationHelper<>(configFolderPath, configPath, configFactory),
                configName,
                configLogger,
                configFolderPath.resolve(configPath)
        );
    }

    public ResourceConfigManager<T> buildResource(String resourcePath){
        ConfigurationFactory<T> configFactory = SnakeYamlConfigurationFactory.create(
                configClass,
                configurationOptions.build(),
                snakeYamlOptions.build()
        );

        String finalResourcePath = baseResourcePath + resourcePath;

        return new ResourceConfigManager<>(
                configFactory,
                configName,
                configLogger,
                configFolderPath.resolve(configPath),
                finalResourcePath
        );
    }
}
