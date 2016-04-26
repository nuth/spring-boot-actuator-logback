package no.nhl.spring.boot.actuator.logback.resource;

import no.nhl.spring.boot.actuator.logback.resource.LoggerResource;
import ch.qos.logback.classic.Logger;
import no.nhl.spring.boot.actuator.logback.LoggerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
public class LoggerResourceAssembler extends ResourceAssemblerSupport<Logger, LoggerResource> {
    
    @Value("${management.context-path:/}")
    private String managementContextPath;
    
    @Autowired
    private LoggerEndpoint endpoint;
    
    public LoggerResourceAssembler() {
        super(LoggerEndpoint.class, LoggerResource.class);
    }

    @Override
    public LoggerResource toResource(Logger t) {
        LoggerResource resource = new LoggerResource();
        UriComponents self = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path(managementContextPath).pathSegment(endpoint.getId(), t.getName()).build();
        
        resource.add(new Link(self.toUriString(), "self"));
        
        resource.setAdditive(t.isAdditive());
        if (t.getLevel() != null) {
            resource.setLevel(LogLevel.valueOf(t.getLevel().toString()));
        }
        if (t.getEffectiveLevel() != null) {
            resource.setEffectiveLevel(LogLevel.valueOf(t.getEffectiveLevel().levelStr));
        }
        resource.setName(t.getName());
        
        if (t.iteratorForAppenders().hasNext()) {
            resource.setConfigured(Boolean.TRUE);
        }
        
        return resource;
    }
    
}
