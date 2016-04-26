package no.nhl.spring.boot.actuator.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import java.util.List;
import no.nhl.spring.boot.actuator.logback.resource.LogbackAdmin;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class LoggerEndpoint extends AbstractEndpoint<LogbackAdmin> {

    public LoggerEndpoint() {
        super("loggers");
    }

    @Override
    public LogbackAdmin invoke() {
        LogbackAdmin admin = new LogbackAdmin();

        admin.setLoggers(((LoggerContext) LoggerFactory.getILoggerFactory()).getLoggerList().size());
        admin.setConfigured(((LoggerContext) LoggerFactory.getILoggerFactory()).getLoggerList().stream().filter((Logger t) -> t.iteratorForAppenders().hasNext()).count());

        return admin;
    }

    public List<Logger> loggers() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        return loggerContext.getLoggerList();
    }

}
