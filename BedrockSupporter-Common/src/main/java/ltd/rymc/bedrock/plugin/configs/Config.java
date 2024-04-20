package ltd.rymc.bedrock.plugin.configs;

import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.plugin.BedrockSupporter;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public interface Config {

    @ConfDefault.DefaultBoolean(true)
    @ConfKey("metrics")
    @ConfComments({
            "",
            "# 启用数据统计",
            "# Enable metrics"
    })
    @AnnotationBasedSorter.Order(10)
    boolean metrics();

    static Map<String, State> defaultState(){
        Map<String, State> stateMap = new HashMap<>();
        for (Module module : BedrockSupporter.getModuleManager().getModules()) {
            stateMap.put(module.getName(), State.of(module.getModuleInfo().getState()));
        }
        return stateMap;
    }

    @ConfDefault.DefaultObject("defaultState")
    @ConfKey("modules")
    @AnnotationBasedSorter.Order(20)
    @ConfComments({
            "",
            "# 启用模块",
            "# Enable modules"
    })
    Map<String, @SubSection State> state();

    interface State {

        @ConfKey("enable")
        boolean enable();

        static State of(boolean enable) {
            return () -> enable;
        }
    }

}


