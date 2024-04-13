package ltd.rymc.bedrock.common.module;

import ltd.rymc.bedrock.common.annote.DefaultEnabledState;
import ltd.rymc.bedrock.common.annote.DependPlugins;
import ltd.rymc.bedrock.common.annote.ModuleAuthors;
import ltd.rymc.bedrock.common.annote.ModuleName;
import ltd.rymc.bedrock.common.annote.ModuleVersion;

import java.util.regex.Pattern;

public class ModuleInfo {

    private final String name;
    private final String version;
    private final String[] authors;
    private final String[] depend;
    private final boolean state;

    public ModuleInfo(String name, String version, String[] authors, String[] depend, boolean state) {
        this.name = Pattern.compile("[\\\\/:*?\"<>|]").matcher(name).replaceAll("_");
        this.version = version;
        this.authors = authors;
        this.depend = depend;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String[] getDepend() {
        return depend;
    }

    public boolean getState() {
        return state;
    }

    public static ModuleInfo of(Class<? extends ModuleBase> clazz){

        ModuleName moduleName = clazz.getAnnotation(ModuleName.class);
        ModuleVersion moduleVersion = clazz.getAnnotation(ModuleVersion.class);
        ModuleAuthors moduleAuthors = clazz.getAnnotation(ModuleAuthors.class);
        DependPlugins dependPlugins = clazz.getAnnotation(DependPlugins.class);
        DefaultEnabledState defaultEnabledState = clazz.getAnnotation(DefaultEnabledState.class);

        String name = moduleName == null ? clazz.getSimpleName() : moduleName.value();
        String version = moduleVersion == null ? "Unknown" : moduleVersion.value();
        String[] author = moduleAuthors == null ? new String[]{"Unknown"} : moduleAuthors.value();
        String[] depend = dependPlugins == null ? new String[]{} : dependPlugins.value();
        boolean state = defaultEnabledState == null || defaultEnabledState.value();

        return new ModuleInfo(name, version, author, depend, state);
    }
}
