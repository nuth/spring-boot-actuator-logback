package no.nhl.spring.boot.actuator.logback.resource;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@Data
public class LogbackAdmin extends ResourceSupport {
    private long loggers;
    private long configured;
}
