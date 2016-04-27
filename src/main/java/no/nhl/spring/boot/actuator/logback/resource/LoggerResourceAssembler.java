package no.nhl.spring.boot.actuator.logback.resource;

import no.nhl.slf4j.runtime.logger.level.LoggerInfo;
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
public class LoggerResourceAssembler extends ResourceAssemblerSupport<LoggerInfo, LoggerResource> {

    @Value("${management.context-path:/}")
    private String managementContextPath;
    private LoggerEndpoint endpoint;

    public LoggerResourceAssembler(LoggerEndpoint loggerEndpoint) {
        super(LoggerEndpoint.class, LoggerResource.class);
        this.endpoint = loggerEndpoint;
    }

    @Override
    public LoggerResource toResource(LoggerInfo t) {
        LoggerResource resource = new LoggerResource();
        UriComponents self = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path(managementContextPath).pathSegment(endpoint.getId(), t.getName()).build();

        resource.add(new Link(self.toUriString(), "self"));

        resource.setAdditive(t.getAdditive());
        resource.setLevel(t.getLevel());
        resource.setEffectiveLevel(t.getEffectiveLevel());
        resource.setName(t.getName());
        resource.setConfigured(t.getConfigured());

        return resource;
    }

}
