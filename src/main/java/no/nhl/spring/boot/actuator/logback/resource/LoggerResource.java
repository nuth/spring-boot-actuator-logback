package no.nhl.spring.boot.actuator.logback.resource;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
public class LoggerResource extends ResourceSupport {
    private String name;
    private LogLevel level;
    private LogLevel effectiveLevel;
    private Boolean additive;
    private Boolean configured;
}
