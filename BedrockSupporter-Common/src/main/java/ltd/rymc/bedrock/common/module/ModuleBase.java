package ltd.rymc.bedrock.common.module;

import ltd.rymc.bedrock.common.module.depend.ModuleDepend;
import ltd.rymc.bedrock.plugin.BedrockSupporter;

public abstract class ModuleBase {

    protected static BedrockSupporter plugin = BedrockSupporter.getInstance();

    protected ModuleInfo moduleInfo = ModuleInfo.of(this.getClass());
    protected ModuleDepend moduleDepend = ModuleDepend.of(moduleInfo.getName(), this.getClass());
    protected ModuleDataPath moduleDataPath = ModuleDataPath.of(moduleInfo);
    protected ModuleLogger moduleLogger = ModuleLogger.of(moduleInfo);

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
            if (!moduleDepend.checkDepend()) return;
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



    protected static boolean isModuleCanEnable(ModuleInfo moduleInfo){
        return BedrockSupporter.getModuleManager().isModuleCanEnabled(moduleInfo.getName());
    }


}
