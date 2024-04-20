package ltd.rymc.bedrock.common.module.depend;

import ltd.rymc.bedrock.common.annote.depend.DependModules;
import ltd.rymc.bedrock.common.annote.depend.DependPlugins;
import ltd.rymc.bedrock.common.module.ModuleBase;
import ltd.rymc.bedrock.plugin.BedrockSupporter;

public class ModuleDepend {

    protected static BedrockSupporter plugin = BedrockSupporter.getInstance();

    private final String[] dependPlugins;
    private final String[] dependModules;
    private final String moduleName;

    private ModuleDepend(String moduleName, String[] dependPlugins, String[] dependModules){
        this.dependPlugins = dependPlugins;
        this.dependModules = dependModules;
        this.moduleName = moduleName;
    }

    public static ModuleDepend of(String moduleName,Class<? extends ModuleBase> clazz){
        DependPlugins dependPlugins = clazz.getAnnotation(DependPlugins.class);
        DependModules dependModules = clazz.getAnnotation(DependModules.class);

        String[] plugins = dependPlugins == null ? new String[]{} : dependPlugins.value();
        String[] modules = dependModules == null ? new String[]{} : dependModules.value();

        return new ModuleDepend(moduleName, plugins, modules);
    }


    public boolean checkDepend(){
        boolean plugin = checkDepend(ModuleCheckerType.Plugins, dependPlugins);
        boolean moudle = checkDepend(ModuleCheckerType.Modules, dependModules);

        boolean check = plugin && moudle;

        if (!check) {
            ModuleDepend.plugin.getLogger().info("The module will not be loaded.");
        }

        return check;
    }

    private boolean checkDepend(ModuleCheckerType checkerType, String... depends){
        boolean check = true;
        c: for (String dependsName : depends){

            for (String dependName : dependsName.split("\\|")) {
                if (checkerType.check(dependName)) continue c;
            }

            plugin.getLogger().info(
                    "The " + moduleName +
                    " module depends on the " + dependsName.replace("|"," or ") +
                    " " + checkerType.getName() +
                    " which is not installed."
            );

            check = false;

        }
        return check;
    }
}
