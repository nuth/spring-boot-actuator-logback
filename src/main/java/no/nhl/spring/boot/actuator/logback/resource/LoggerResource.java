package no.nhl.spring.boot.actuator.logback.resource;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Nikolai Luthman <nikolai dot luthman at inmeta dot no>
 */
public class LoggerResource extends ResourceSupport {

    private String name;
    private LogLevel level;
    private LogLevel effectiveLevel;
    private Boolean additive;
    private Boolean configured;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public LogLevel getEffectiveLevel() {
        return effectiveLevel;
    }

    public void setEffectiveLevel(LogLevel effectiveLevel) {
        this.effectiveLevel = effectiveLevel;
    }

    public Boolean getAdditive() {
        return additive;
    }

    public void setAdditive(Boolean additive) {
        this.additive = additive;
    }

    public Boolean getConfigured() {
        return configured;
    }

    public void setConfigured(Boolean configured) {
        this.configured = configured;
    }

}
