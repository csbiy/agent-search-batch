package agent.search.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "military")
public class MilitaryExcelProperties {

    private String excelFileName;

    private String excelFileDateFormat;

    private IndustryProperties industry;

}