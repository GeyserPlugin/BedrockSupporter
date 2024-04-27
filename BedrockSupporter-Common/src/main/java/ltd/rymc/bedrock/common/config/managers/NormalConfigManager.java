package ltd.rymc.bedrock.common.config.managers;

import ltd.rymc.bedrock.common.config.ConfigManager;
import ltd.rymc.bedrock.common.config.loggers.ConfigLogger;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.factory.CommentedWrapper;
import space.arim.dazzleconf.helper.ConfigurationHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class NormalConfigManager<C> implements ConfigManager<C> {

    private static Field declaredField = null;
    private static Method fromRawMap = null;
    private static Method toRawMap = null;

    static {
        try {
            declaredField = Class.forName("space.arim.dazzleconf.factory.AbstractConfigurationFactory").getDeclaredField("delegate");
            fromRawMap = Class.forName("space.arim.dazzleconf.factory.ConfigurationFormatFactory").getDeclaredMethod("fromRawMap", Map.class);
            toRawMap = Class.forName("space.arim.dazzleconf.factory.ConfigurationFormatFactory").getDeclaredMethod("toRawMap", Object.class);
            declaredField.setAccessible(true);
            fromRawMap.setAccessible(true);
            toRawMap.setAccessible(true);
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private final NormalRawDataHelper rawDataHelper = new NormalRawDataHelper();

    private final ConfigurationHelper<C> configHelper;
    private final Path configFile;
    private final String configName;
    private final ConfigLogger configLogger;
    private volatile C configData;

    public NormalConfigManager(ConfigurationHelper<C> configHelper, String configName, ConfigLogger configLogger, Path configFile) {
        this.configFile = configFile;
        this.configName = configName;
        this.configLogger = configLogger;
        this.configHelper = configHelper;
    }

    public void reloadConfig() {
        try {
            configData = configHelper.reloadConfigData();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);

        } catch (ConfigFormatSyntaxException ex) {
            configData = configHelper.getFactory().loadDefaults();
            configLogger.severe("The yaml syntax in your configuration is invalid. "
                                                           + "Check your YAML syntax with a tool such as https://yaml-online-parser.appspot.com/");
            ex.printStackTrace();

        } catch (InvalidConfigException ex) {
            configData = configHelper.getFactory().loadDefaults();
            configLogger.severe("One of the values in your configuration is not valid. "
                                                     + "Check to make sure you have specified the right data types.");
            ex.printStackTrace();
        }
    }

    public C getConfigData() {
        C configData = this.configData;
        if (configData == null) {
            throw new IllegalStateException("Configuration has not been loaded yet");
        }
        return configData;
    }

    public boolean isLoaded(){
        return configData != null;
    }

    public String getConfigName(){
        return configName;
    }

    public RawDataHelper getRawDataHelper() {
        return rawDataHelper;
    }

    public class NormalRawDataHelper implements RawDataHelper {

        public void setData(String fullKey, Object data) {
            Map<String, Object> rawMap = toRawMap();
            removeAllCommentedWrapper(rawMap);
            Queue<String> queue = new ConcurrentLinkedQueue<>(Arrays.asList(fullKey.split("\\.")));
            setData(rawMap, queue, data);
            C newData = fromRawMap(rawMap);

            try (FileChannel fileChannel = FileChannel.open(configFile, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                configHelper.getFactory().write(newData, fileChannel);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            reloadConfig();
        }

        public void printData() {
            printMap(toRawMap(), null);
        }

        private void removeAllCommentedWrapper(Map<String, Object> map) {

            HashMap<String,Object> tempMap = new HashMap<>();

            for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof CommentedWrapper) {
                    CommentedWrapper wrapper = (CommentedWrapper) value;
                    tempMap.put(key, wrapper.getValue());
                    iterator.remove();
                }

                if (value instanceof Map<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> subMap = (Map<String, Object>) value;
                    removeAllCommentedWrapper(subMap);
                }
            }

            map.putAll(tempMap);

        }

        private void setData(Map<String, Object> rawMap, Queue<String> queue, Object data) {
            String key = queue.remove();

            if (queue.isEmpty()) {
                rawMap.remove(key);
                rawMap.put(key, data);
                return;
            }

            Object value = rawMap.get(key);
            if (value instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked") Map<String, Object> subMap = (Map<String, Object>) value;
                setData(subMap, queue, data);
            } else {
                throw new IllegalStateException("Wrong key");
            }
        }

        private void printMap(Map<String, Object> map, String parentKey) {

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = parentKey == null ? entry.getKey() : parentKey + "." + entry.getKey();
                Object value = entry.getValue();

                configLogger.info("Key: " + key + " , Value:" + value + " , Type: " + value.getClass().getName());

                // Check subsection
                if (!(value instanceof Map<?, ?>)) continue;

                // Print subsection
                @SuppressWarnings("unchecked") Map<String, Object> subMap = (Map<String, Object>) value;
                printMap(subMap, key);

            }
        }

        private Map<String, Object> toRawMap() {
            if (toRawMap == null || declaredField == null) {
                throw new IllegalStateException("Reflection not loaded properly!");
            }

            try {
                @SuppressWarnings("unchecked") Map<String, Object> rawMap = (Map<String, Object>) toRawMap.invoke(declaredField.get(configHelper.getFactory()), getConfigData());
                return rawMap;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("An error occurred while converting ConfigData to RawData", e);
            }

        }

        private C fromRawMap(Map<String, Object> rawMap) {
            if (fromRawMap == null || declaredField == null) {
                throw new IllegalStateException("Reflection not loaded properly!");
            }

            try {
                @SuppressWarnings("unchecked") C configData = (C) fromRawMap.invoke(declaredField.get(configHelper.getFactory()), rawMap);
                return configData;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("An error occurred while converting RawData to ConfigData", e);
            }

        }
    }


}