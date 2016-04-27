package no.nhl.spring.boot.actuator.logback;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import no.nhl.slf4j.runtime.logger.level.LogUtils;
import no.nhl.slf4j.runtime.logger.level.LoggerInfo;
import no.nhl.spring.boot.actuator.logback.resource.LoggerResource;
import no.nhl.spring.boot.actuator.logback.resource.LoggerResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class LoggerMvcEndpoint extends EndpointMvcAdapter {

    @Value("${management.context-path:/}")
    private String managementContextPath;

    private final LoggerEndpoint delegate;

    private final LoggerResourceAssembler loggerResourceAssembler;

    @Autowired
    private HateoasPageableHandlerMethodArgumentResolver hphmar;

    @Autowired
    private LogUtils logUtils;

    @Autowired
    public LoggerMvcEndpoint(LoggerEndpoint delegate) {
        super(delegate);
        this.delegate = delegate;
        this.loggerResourceAssembler = new LoggerResourceAssembler(delegate);
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

        Supplier<Stream<LoggerInfo>> supply = () -> {
            Stream<LoggerInfo> list = delegate.loggers().stream().parallel();

            if (configured != null) {
                list = list.filter(
                        t -> t.getConfigured() == configured);
            }

            return list;
        };

        List<LoggerInfo> loggers = supply.get()
                .skip(pageable.getOffset()).limit(pageable.getPageSize())
                .collect(Collectors.toList());

        long count = supply.get().count();

        Page<LoggerInfo> page = new PageImpl<>(
                loggers,
                pageable,
                count);

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

        LoggerInfo loggerInfo = logUtils.getLogger(id);
        loggerInfo.setLevel(resource.getLevel());
        return ResponseEntity.ok(
                loggerResourceAssembler.
                toResource(
                        logUtils.setLoggerLevel(loggerInfo)));
    }

    @RequestMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public final ResponseEntity<LoggerResource> get(@PathVariable final String id) {
        return ResponseEntity.ok(loggerResourceAssembler.toResource(logUtils.getLogger(id)));
    }

}
