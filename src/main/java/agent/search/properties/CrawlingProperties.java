package agent.search.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "crawling")
public class CrawlingProperties {
    private String driverPath;

    private String downloadDirectory;

    private Long waitingTime;

    private List<String> chromeOptions;
}
