package ltd.rymc.bedrock.common.language;

import ltd.rymc.bedrock.common.config.ConfigBuilder;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.common.config.ConfigManager;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import java.util.List;

public abstract class Serialiser implements ValueSerialiser<Language> {

    private static String generateExceptionMessage(String language, List<String> availableLanguages) {
        StringBuilder message = new StringBuilder();
        message.append("The wrong language was entered. The value should be a name of an available language");
        message.append(", ");
        message.append("but it was ");
        message.append(language);
        message.append('\n');
        message.append("Some examples of valid input: ");
        for (int i = 0, j = Math.min(availableLanguages.size(), 3); i < j; i++) {
            message.append(availableLanguages.get(i));
            message.append(", ");
        }
        return message.toString();
    }

    @Override
    public Class<Language> getTargetClass() {
        return Language.class;
    }

    @Override
    public Language deserialise(FlexibleType flexibleType) throws BadValueException {
        String language = getValue(flexibleType);
        ConfigManager<?> configManager = ConfigBuilder
                .builder(getTargetConfig(), "lang\\" + language + ".yml")
                .module(getModule())
                .name(language)
                .buildResource("lang/" + language + ".yml");
        configManager.reloadConfig();
        return Language.of(configManager, getTargetConfig());
    }

    @Override
    public Object serialise(Language value, Decomposer decomposer) {
        return value.getConfigManager().getConfigName();
    }

    private String getValue(FlexibleType flexibleType) throws BadValueException {
        String language = flexibleType.getString();
        List<String> availableLanguages = getAvailableLanguages();
        if (availableLanguages.contains(language)) return language;

        throw new BadValueException.Builder().key(flexibleType.getAssociatedKey()).message(generateExceptionMessage(language, availableLanguages)).build();
    }

    protected Class<?> getTargetConfig() {
        return null;
    }

    protected Module getModule() { return null; }

    protected List<String> getAvailableLanguages() {
        return null;
    }
}
