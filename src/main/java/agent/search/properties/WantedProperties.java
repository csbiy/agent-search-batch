package agent.search.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "crawling.wanted")
public class WantedProperties {

    private String url;
}
