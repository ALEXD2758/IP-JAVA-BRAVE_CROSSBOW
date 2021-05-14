package be.app.sb.configuration;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;

//Configuration class for activating the HTTP Trace for actuator
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public HttpTraceRepository httpTraceRepository()
    {
        return new InMemoryHttpTraceRepository();
    }
}