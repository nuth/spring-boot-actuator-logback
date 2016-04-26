package no.nhl.spring.boot.actuator.logback;

import no.nhl.spring.boot.actuator.logback.resource.LogbackAdmin;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class LoggerEndpoint extends AbstractEndpoint<LogbackAdmin> {

    @Value("${management.context-path:/}")
    private String managementContextPath;
    
    @Autowired
    private EntityLinks entityLinks;
    
    public LoggerEndpoint() {
        super("loggers");
    }

    @Override
    public LogbackAdmin invoke() {
        LogbackAdmin admin =  new LogbackAdmin();
        
        admin.setLoggers(((LoggerContext) LoggerFactory.getILoggerFactory()).getLoggerList().size());
        admin.setConfigured(((LoggerContext) LoggerFactory.getILoggerFactory()).getLoggerList().stream().filter((Logger t) -> t.iteratorForAppenders().hasNext()).count());
        
        
        admin.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping()
                        .path(managementContextPath)
                        .pathSegment(this.getId()).pathSegment("list").toUriString(), "list"));
        return admin;
    }
    
    public List<Logger> loggers() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        return loggerContext.getLoggerList();
    }
    
}
