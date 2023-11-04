package agent.search.crawling;

import agent.search.properties.CrawlingProperties;
import agent.search.properties.IndustryProperties;
import agent.search.properties.MilitaryExcelProperties;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

import static org.openqa.selenium.By.*;

@Component
@RequiredArgsConstructor
@JobScope
public class MilitaryCompanyCrawlingService implements JobExecutionListener {

    private final WebDriver driver;

    private final CrawlingProperties properties;

    private final MilitaryExcelProperties militaryExcelProperties;

    @Value("#{jobParameters['excelFileName']}")
    public String excelFileName;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        IndustryProperties industryProperties = militaryExcelProperties.getIndustry();
        String path = new StringBuilder(properties.getDownloadDirectory())
                .append("\\")
                .append(excelFileName)
                .toString();
        File crawlingFile = new File(path);
        if (crawlingFile.exists()) {
            return;
        }
        driver.get(industryProperties.getUrl());
        industryProperties.getFilterCheckBtnCss().forEach((cssSelector) ->
                driver.findElement(cssSelector(cssSelector)).click()
        );
        driver.findElement(cssSelector(industryProperties.getFilterSubmitBtnCss())).click();
        driver.findElement(cssSelector(industryProperties.getExcelDownloadBtnCss())).click();

        new WebDriverWait(driver, Duration.ofMinutes(properties.getWaitingTime()))
                .until((d) -> crawlingFile.exists());
    }


}
