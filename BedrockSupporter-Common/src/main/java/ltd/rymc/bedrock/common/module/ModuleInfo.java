package ltd.rymc.bedrock.common.module;

import ltd.rymc.bedrock.common.annote.info.DefaultEnabledState;
import ltd.rymc.bedrock.common.annote.info.ModuleAuthors;
import ltd.rymc.bedrock.common.annote.info.ModuleName;
import ltd.rymc.bedrock.common.annote.info.ModuleVersion;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public class ModuleInfo {

    private final String name;
    private final String version;
    private final String[] authors;
    private final boolean state;

    public ModuleInfo(String name, String version, String[] authors, boolean state) {
        this.name = Pattern.compile("[\\\\/:*?\"<>|]").matcher(name).replaceAll("_");
        this.version = version;
        this.authors = authors;
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

    public boolean getState() {
        return state;
    }

    public static ModuleInfo of(Class<? extends ModuleBase> clazz){

        ModuleName moduleName = clazz.getAnnotation(ModuleName.class);
        ModuleVersion moduleVersion = clazz.getAnnotation(ModuleVersion.class);
        ModuleAuthors moduleAuthors = clazz.getAnnotation(ModuleAuthors.class);
        DefaultEnabledState defaultEnabledState = clazz.getAnnotation(DefaultEnabledState.class);

        String name = moduleName == null ? clazz.getSimpleName() : moduleName.value();
        String version = moduleVersion == null ? "Unknown" : moduleVersion.value();
        String[] author = moduleAuthors == null ? new String[]{"Unknown"} : moduleAuthors.value();
        boolean state = defaultEnabledState == null || defaultEnabledState.value();

        return new ModuleInfo(name, version, author, state);
    }

    public String getInfoString(ChatColor state) {
        StringBuilder builder = new StringBuilder();
        builder.append(state);
        builder.append(getName()).append(ChatColor.WHITE).append(" version ").append(getVersion()).append(" by ");
        String[] authors = getAuthors();
        for (int i = 0, j = authors.length; i < j; i++) {
            String author = authors[i];
            builder.append(author);
            if (i + 1 < j) builder.append(", ");
        }
        return builder.toString();
    }
}
