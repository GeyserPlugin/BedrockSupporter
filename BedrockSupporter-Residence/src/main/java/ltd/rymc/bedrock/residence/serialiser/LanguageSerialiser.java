package ltd.rymc.bedrock.residence.serialiser;

import ltd.rymc.bedrock.common.language.Serialiser;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.modules.ResidenceForm;
import ltd.rymc.bedrock.residence.configs.LanguageConfig;

import java.util.Arrays;
import java.util.List;

public class LanguageSerialiser extends Serialiser {

    @Override
    protected Class<?> getTargetConfig(){
        return LanguageConfig.class;
    }

    @Override
    protected Module getModule() {
        return ResidenceForm.getInstance();
    }

    @Override
    protected List<String> getAvailableLanguages() {
        return Arrays.asList("zh_CN", "en_US");
    }
}
