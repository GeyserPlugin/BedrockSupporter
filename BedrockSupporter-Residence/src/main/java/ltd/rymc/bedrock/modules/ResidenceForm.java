package ltd.rymc.bedrock.modules;

import com.bekvon.bukkit.residence.commands.form;
import ltd.rymc.bedrock.common.annote.depend.DependPlugins;
import ltd.rymc.bedrock.common.annote.info.ModuleAuthors;
import ltd.rymc.bedrock.common.annote.info.ModuleName;
import ltd.rymc.bedrock.common.annote.info.ModuleVersion;
import ltd.rymc.bedrock.common.config.ConfigManager;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.common.utils.LanguageUtils;
import ltd.rymc.bedrock.residence.commands.ResidenceFormCommand;
import ltd.rymc.bedrock.residence.configs.Config;
import ltd.rymc.bedrock.residence.utils.hook.ResidenceHook;

import java.io.File;

@ModuleName("ResidenceForm")
@ModuleVersion("2.3.0")
@ModuleAuthors({ "RENaa_FD" })
@DependPlugins({ "Residence" })
public class ResidenceForm extends Module {

    private static ResidenceForm instance;
    private static ConfigManager<Config> mainConfigManager;

    public static ResidenceForm getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();

        Config config = mainConfigManager.getConfigData();

        loadResidenceHook(config.hookResidence());

        getCommandManager().registerCommand(new ResidenceFormCommand());
    }

    @Override
    public void onReload() {
        ResidenceForm.getMainConfigManager().reloadConfig();
    }

    public static ConfigManager<Config> getMainConfigManager() {
        return mainConfigManager;
    }

    public static Config getMainConfig() {
        return mainConfigManager.getConfigData();
    }

    public static Language getLanguage(){
        return mainConfigManager.getConfigData().language();
    }

    private void loadConfig() {
        boolean firstLoad = !new File(getDataFolder(), "config.yml").exists();

        mainConfigManager = defaultConfig(Config.class);
        mainConfigManager.reloadConfig();

        if (firstLoad) mainConfigManager.getRawDataHelper().setData("language", LanguageUtils.translateLanguage(LanguageUtils.getSystemLanguage()));

        Language language = mainConfigManager.getConfigData().language();
        getLogger().info("LanguageConfig: " + language.getConfigManager().getConfigName());
    }

    private void loadResidenceHook(boolean load) {
        if (!load) return;
        if (ResidenceHook.hook(new form(), 3300)) {
            getLogger().info("Residence hooked!");
        } else {
            getLogger().warning("Residence hook failed!");
        }
    }

}
