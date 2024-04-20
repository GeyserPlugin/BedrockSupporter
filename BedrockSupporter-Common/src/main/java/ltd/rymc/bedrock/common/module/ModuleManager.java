package ltd.rymc.bedrock.common.module;

import ltd.rymc.bedrock.plugin.BedrockSupporter;
import ltd.rymc.bedrock.plugin.configs.Config;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    private final BedrockSupporter plugin;
    private Reflections reflections = new Reflections("ltd.rymc.bedrock.modules");
    private Map<String,Module> modules = new HashMap<>();

    public ModuleManager(BedrockSupporter plugin){
        this.plugin = plugin;
    }

    public List<Class<? extends Module>> getBuiltInModule(){
        return new ArrayList<>(reflections.getSubTypesOf(Module.class));
    }

    public Module loadModule(Class<? extends Module> moduleClazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Module module = moduleClazz.getConstructor().newInstance();
        modules.put(module.getName(), module);
        return module;
    }

    public Module getModule(String moduleName){
        return modules.get(moduleName);
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules.values());
    }

    public void enableAll(){
        for (Module module : modules.values()) {
            module.enable();
        }
    }

    public void disableAll(){
        for (Module module : modules.values()) {
            module.disable();
        }
    }

    public void reloadAll(){
        for (Module module : modules.values()) {
            module.reload();
        }

    }

    public boolean isModuleCanEnabled(String moduleName){
        Config.State state = plugin.getPluginConfig().state().get(moduleName);
        return state != null && state.enable();
    }



}
