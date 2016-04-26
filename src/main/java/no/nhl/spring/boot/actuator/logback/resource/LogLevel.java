package no.nhl.spring.boot.actuator.logback.resource;

import ch.qos.logback.classic.Logger;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public enum LogLevel {
    ERROR(Logger.ERROR_INT),
    DEBUG(Logger.DEBUG_INT),
    INFO(Logger.INFO_INT),
    TRACE(Logger.TRACE_INT),
    WARN(Logger.WARN_INT);
    
    private int level;

    private LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
