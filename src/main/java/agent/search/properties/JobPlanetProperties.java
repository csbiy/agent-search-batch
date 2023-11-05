package agent.search.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "crawling.jobplanet")
public class JobPlanetProperties {

    private String url;

    private String queryParameter;

    private String companyCardCss;

    private String companyReviewScoreCss;

    private String companyCategoryCss;

    private String companyCategoryLabel;
}
