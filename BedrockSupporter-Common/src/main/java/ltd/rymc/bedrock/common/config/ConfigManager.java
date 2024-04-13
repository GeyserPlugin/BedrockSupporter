package ltd.rymc.bedrock.common.config;

@SuppressWarnings("unused")
public interface ConfigManager<C> {

    void reloadConfig();

    C getConfigData();

    String getConfigName();

    boolean isLoaded();

    RawDataHelper getRawDataHelper();

    interface RawDataHelper {

        void setData(String fullKey, Object data);

        void printData();
    }
}
