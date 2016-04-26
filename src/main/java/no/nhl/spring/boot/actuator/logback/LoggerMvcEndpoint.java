package no.nhl.spring.boot.actuator.logback;

import no.nhl.spring.boot.actuator.logback.resource.LoggerResourceAssembler;
import no.nhl.spring.boot.actuator.logback.resource.LoggerResource;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Component
@ExposesResourceFor(LoggerResource.class)
public class LoggerMvcEndpoint extends EndpointMvcAdapter {

    @Value("${management.context-path:/}")
    private String managementContextPath;

    private final LoggerEndpoint delegate;

    @Autowired
    private LoggerResourceAssembler loggerResourceAssembler;

    @Autowired
    private HateoasPageableHandlerMethodArgumentResolver hphmar;

    @Autowired
    public LoggerMvcEndpoint(LoggerEndpoint delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Resources<Resource<LoggerResource>>> list(
            @PageableDefault final Pageable pageable,
            @RequestParam(required = false) final Boolean configured) {
        final PagedResourcesAssembler pagedResourcesAssembler
                = new PagedResourcesAssembler(
                        hphmar,
                        ServletUriComponentsBuilder.fromCurrentServletMapping()
                        .path(managementContextPath)
                        .pathSegment(delegate.getId())
                        .pathSegment("list")
                        .build().encode());
        Stream<Logger> list = delegate.loggers().stream().parallel();

        if (configured != null) {
            list = list.filter(
                    t -> t.iteratorForAppenders().hasNext() == configured);
        }

        final AtomicLong count = new AtomicLong(0);
        list = list.peek(t -> count.incrementAndGet());

        list = list.skip(pageable.getOffset()).limit(pageable.getPageSize());

        Page<Logger> page = new PageImpl<>(
                list.collect(Collectors.toList()),
                pageable,
                count.intValue());

        return ResponseEntity.ok(
                pagedResourcesAssembler.toResource(
                        page,
                        loggerResourceAssembler));
    }

    @RequestMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    @ResponseBody
    public final ResponseEntity<LoggerResource> post(
            @PathVariable final String id,
            @RequestBody final LoggerResource resource) {
        Logger logger = (Logger) LoggerFactory.getLogger(id);
        logger.setLevel(Level.toLevel(resource.getLevel().getLevel()));
        logger.setAdditive(resource.getAdditive());
        return ResponseEntity.ok(loggerResourceAssembler.toResource(logger));
    }

    @RequestMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public final ResponseEntity<LoggerResource> get(@PathVariable final String id) {
        Logger logger = (Logger) LoggerFactory.getLogger(id);
        return ResponseEntity.ok(loggerResourceAssembler.toResource(logger));
    }

}
