package no.nhl.spring.boot.actuator.logback.autoconfigure;

import javax.servlet.Servlet;
import no.nhl.spring.boot.actuator.logback.LoggerEndpoint;
import no.nhl.spring.boot.actuator.logback.LoggerMvcEndpoint;
import org.springframework.boot.actuate.autoconfigure.ManagementContextConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@ManagementContextConfiguration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, Link.class, Page.class})
@ConditionalOnWebApplication
@AutoConfigureAfter(EndpointAutoConfiguration.class)
public class MvcEndpointAutoConfiguration {

    @Bean
    public LoggerMvcEndpoint loggerMvcEndpoint(LoggerEndpoint loggerEndpoint) {
        return new LoggerMvcEndpoint(loggerEndpoint);
    }
    /*
    @ControllerAdvice(assignableTypes = LoggerEndpoint.class)
    public static class MvcEndpointAdvice implements ResponseBodyAdvice<Object> {

        @Value("${management.context-path:/}")
        private String managementContextPath;

        @Override
        public boolean supports(MethodParameter mp, Class<? extends HttpMessageConverter<?>> type) {
            return LoggerEndpoint.class.isAssignableFrom(mp.getDeclaringClass());
        }

        @Override
        public Object beforeBodyWrite(Object t, MethodParameter mp, MediaType mt, Class<? extends HttpMessageConverter<?>> type, ServerHttpRequest shr, ServerHttpResponse shr1) {
            Resource<Object> resource = new Resource(t);
            resource.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping()
                    .path(managementContextPath)
                    .pathSegment("loggers").pathSegment("list").toUriString(), "list"));
            return resource;
        }

    }
     */
}
