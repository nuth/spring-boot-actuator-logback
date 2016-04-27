package no.nhl.spring.boot.actuator.logback;

import java.util.List;
import no.nhl.slf4j.runtime.logger.level.LogUtils;
import no.nhl.slf4j.runtime.logger.level.LoggerInfo;
import no.nhl.spring.boot.actuator.logback.resource.LogbackAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class LoggerEndpoint extends AbstractEndpoint<LogbackAdmin> {

    @Autowired
    private LogUtils logUtils;

    public LoggerEndpoint() {
        super("loggers");
    }

    @Override
    public LogbackAdmin invoke() {
        LogbackAdmin admin = new LogbackAdmin();

        admin.setLoggers(logUtils.getLoggers().size());
        admin.setConfigured(logUtils.getLoggers().stream().filter(t -> t.getConfigured()).count());

        return admin;
    }

    public List<LoggerInfo> loggers() {
        return logUtils.getLoggers();
    }

}
