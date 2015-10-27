package no.nb.microservices.catalogplaylist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "playlist")
public class ApplicationSettings {
    private String streamService;

    public String getStreamService() {
        return streamService;
    }

    public void setStreamService(String streamService) {
        this.streamService = streamService;
    }
}
