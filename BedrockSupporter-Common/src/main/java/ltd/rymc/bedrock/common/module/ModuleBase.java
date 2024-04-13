package ltd.rymc.bedrock.common.module;


import ltd.rymc.bedrock.common.utils.PluginUtils;
import ltd.rymc.bedrock.plugin.BedrockSupporter;

public abstract class ModuleBase {

    protected static BedrockSupporter plugin = BedrockSupporter.getInstance();

    protected ModuleInfo moduleInfo = ModuleInfo.of(this.getClass());
    protected boolean enabled = false;

    public void onEnable(){

    }

    public void onDisable() {

    }

    public void onReload() {

    }


    public void enable(){
        try {
            if (!isModuleCanEnable(moduleInfo)) return;
            plugin.getLogger().info("Enabling Module " + moduleInfo.getName());
            if (!checkDepend(moduleInfo)) return;
            onEnable();
            enabled = true;
        } catch (Exception e){
            e.printStackTrace();
            disable();
        }
    }

    public void disable(){
        try {
            plugin.getLogger().info("Disabling Module " + moduleInfo.getName());
            onDisable();
        } catch (Exception e){
            e.printStackTrace();
        }
        enabled = false;
    }

    public void reload(){

            if (!isModuleCanEnable(moduleInfo)) {
                if (enabled) disable();
                return;
            }

            if (!enabled) {
                enable();
                return;
            }

            try {
                plugin.getLogger().info("Reloading Module " + moduleInfo.getName());
                onReload();
            } catch (Exception e){
                e.printStackTrace();
                disable();
            }

    }

    protected static boolean checkDepend(ModuleInfo info){
        boolean check = true;
        c: for (String pluginsName : info.getDepend()){

            for (String pluginName : pluginsName.split("\\|")) {
                if (PluginUtils.hasPlugin(pluginName)) continue c;
            }

            plugin.getLogger().info("The " + info.getName() + " module depends on the " + pluginsName.replace("|"," or ") + " plugin which is not installed.");
            check = false;

        }
        if (!check) plugin.getLogger().info("The module will not be loaded.");
        return check;
    }

    protected static boolean isModuleCanEnable(ModuleInfo moduleInfo){
        return plugin.isModuleCanEnabled(moduleInfo.getName());
    }


}
