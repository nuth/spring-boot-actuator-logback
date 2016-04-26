package no.nhl.spring.boot.actuator.logback.resource;

import ch.qos.logback.classic.Logger;
import no.nhl.spring.boot.actuator.logback.LoggerEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class LoggerResourceAssembler extends ResourceAssemblerSupport<Logger, LoggerResource> {

    @Value("${management.context-path:/}")
    private String managementContextPath;
    private LoggerEndpoint endpoint;

    public LoggerResourceAssembler(LoggerEndpoint loggerEndpoint) {
        super(LoggerEndpoint.class, LoggerResource.class);
        this.endpoint = loggerEndpoint;
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
