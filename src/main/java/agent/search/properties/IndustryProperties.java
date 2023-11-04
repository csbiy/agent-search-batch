package agent.search.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "crawling.military.industry")
public class IndustryProperties {

    private String url;

    private List<String> filterCheckBtn;

    private String filterSubmitBtn;

    private String excelDownloadBtn;
}
