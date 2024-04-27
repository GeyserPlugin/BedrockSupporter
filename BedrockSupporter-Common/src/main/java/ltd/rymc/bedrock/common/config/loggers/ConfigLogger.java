package ltd.rymc.bedrock.common.config.loggers;

public interface ConfigLogger {
    void info(String message);

    void warning(String message);

    void severe(String message);
}
