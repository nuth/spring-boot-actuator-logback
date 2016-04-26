package no.nhl.spring.boot.actuator.logback.resource;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class LogbackAdmin {

    private long loggers;
    private long configured;

    public long getLoggers() {
        return loggers;
    }

    public void setLoggers(long loggers) {
        this.loggers = loggers;
    }

    public long getConfigured() {
        return configured;
    }

    public void setConfigured(long configured) {
        this.configured = configured;
    }

}
