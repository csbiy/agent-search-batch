package agent.search.crawling;

import agent.search.entity.MilitaryCompany;
import agent.search.entity.Statistic;
import agent.search.enumeration.StatisticProperty;
import agent.search.properties.CrawlingProperties;
import agent.search.properties.IndustryProperties;
import agent.search.properties.MilitaryExcelProperties;
import agent.search.repository.MilitaryCompanyRepository;
import agent.search.repository.StatisticRepository;
import jakarta.transaction.Transactional;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static agent.search.enumeration.StatisticProperty.*;
import static org.openqa.selenium.By.*;

@Component
@RequiredArgsConstructor
@JobScope
public class MilitaryCompanyCrawlingService implements JobExecutionListener {

    private final WebDriver driver;

    private final CrawlingProperties properties;

    private final MilitaryExcelProperties militaryExcelProperties;

    private final MilitaryCompanyRepository militaryCompanyRepository;

    private final StatisticRepository statisticRepository;

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

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        int activeEnlistTotal = militaryCompanyRepository.findAll().stream().mapToInt(MilitaryCompany::getActiveEnlistNum).sum();
        int suppEnlistTotal = militaryCompanyRepository.findAll().stream().mapToInt(MilitaryCompany::getSuppEnlistNum).sum();
        saveTodayEnlistNumber(activeEnlistTotal, suppEnlistTotal);
        saveDifferenceNumber(activeEnlistTotal, suppEnlistTotal);
    }

    private void saveDifferenceNumber(int activeEnlistTotal, int suppEnlistTotal) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT).minusSeconds(1l).plusDays(1l);
        LocalDateTime before = LocalDateTime.of(now.minusDays(1).toLocalDate(), LocalTime.MIDNIGHT);

        List<Statistic> yesterdayActiveEnlist = statisticRepository.findByPropertyAndCreatedAtBetween(ACTIVE_ENLIST_NUMBER.name(), before, after);
        List<Statistic> yesterdaySuppEnlist = statisticRepository.findByPropertyAndCreatedAtBetween(SUPP_ENLIST_NUMBER.name(), before, after);

        Optional.of(yesterdayActiveEnlist.get(0)).ifPresent((enlistNumber) -> {
            int difference = enlistNumber.getDifference(activeEnlistTotal);
            statisticRepository.save(Statistic.ofPropertyAndValue(ACTIVE_ENLIST_NUMBER.name(), difference));
        });
        Optional.of(yesterdaySuppEnlist.get(0)).ifPresent((enlistNumber) -> {
            int difference = enlistNumber.getDifference(suppEnlistTotal);
            statisticRepository.save(Statistic.ofPropertyAndValue(SUPP_ENLIST_NUMBER.name(), difference));
        });
    }

    private void saveTodayEnlistNumber(int activeEnlistTotal, int suppEnlistTotal) {
        Statistic dayActiveEnlist = Statistic.ofPropertyAndValue(ACTIVE_ENLIST_NUMBER.name(), activeEnlistTotal);
        Statistic daySuppEnlist = Statistic.ofPropertyAndValue(SUPP_ENLIST_NUMBER.name(), suppEnlistTotal);
        statisticRepository.save(dayActiveEnlist);
        statisticRepository.save(daySuppEnlist);
    }
}
