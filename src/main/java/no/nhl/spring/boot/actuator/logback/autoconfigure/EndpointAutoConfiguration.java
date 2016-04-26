package no.nhl.spring.boot.actuator.logback.autoconfigure;

import no.nhl.spring.boot.actuator.logback.LoggerEndpoint;
import org.springframework.boot.actuate.autoconfigure.ManagementContextConfiguration;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
@ManagementContextConfiguration
public class EndpointAutoConfiguration {

    @Bean
    public LoggerEndpoint loggerEndpoint() {
        return new LoggerEndpoint();
    }

}
