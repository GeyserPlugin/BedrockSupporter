package ltd.rymc.bedrock.common.module.depend;

import ltd.rymc.bedrock.common.utils.ModuleUtils;
import ltd.rymc.bedrock.common.utils.PluginUtils;

public enum ModuleCheckerType {
    Modules(ModuleUtils::hasModule, "module"),
    Plugins(PluginUtils::hasPlugin, "plugin");

    private final ModuleDependChecker checker;
    private final String name;

    ModuleCheckerType(ModuleDependChecker checker, String name){
        this.checker = checker;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean check(String name){
        return checker.check(name);
    }
}
