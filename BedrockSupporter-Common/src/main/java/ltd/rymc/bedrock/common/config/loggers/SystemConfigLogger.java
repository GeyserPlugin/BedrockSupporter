package ltd.rymc.bedrock.common.config.loggers;

public class SystemConfigLogger implements ConfigLogger {

    private SystemConfigLogger(){}

    public static ConfigLogger of(){
        return new SystemConfigLogger();
    }

    @Override
    public void info(String message) {
        System.out.println(message);
    }

    @Override
    public void warning(String message) {
        System.err.println(message);
    }

    @Override
    public void severe(String message) {
        System.err.println(message);
    }
}
