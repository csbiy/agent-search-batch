package agent.search.crawling;

import agent.search.entity.MilitaryCompany;
import agent.search.entity.Recruitment;
import agent.search.properties.WantedProperties;
import agent.search.repository.MilitaryCompanyRepository;
import agent.search.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WantedCrawlingService implements JobExecutionListener {

    public static final int SCROLL_DOWN_NUMBER = 20_000;

    private final WebDriver driver;

    private final WantedProperties properties;

    private final MilitaryCompanyRepository militaryCompanyRepository;

    private final RecruitmentRepository recruitmentRepository;

    @Override
    @Transactional
    public void beforeJob(JobExecution jobExecution) {
        recruitmentRepository.deleteAll();
        driver.get(properties.getUrl());
        for (int i = 0; i < SCROLL_DOWN_NUMBER; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
        List<WebElement> foundJobs = driver.findElements(By.cssSelector("ul[data-cy='job-list'] li"));
        foundJobs.forEach(this::handleFoundJob);
    }

    private void handleFoundJob(WebElement foundJob) {
        String logo;
        String position;
        String companyName;
        try {
            String rawLogo = foundJob.findElement(By.tagName("header")).getCssValue("background-image");
            logo = rawLogo.replaceAll("^url\\([\"']?", "")
                    .replaceAll("[\"']?\\)$", "");
            position = foundJob.findElement(By.cssSelector(".job-card-position")).getText();
            companyName = foundJob.findElement(By.cssSelector(".job-card-company-name")).getText();
        } catch (NoSuchElementException e) {
            log.warn("wanted html markup might change", e);
            return;
        }
        List<MilitaryCompany> foundCompanies = militaryCompanyRepository.findByNameContaining(companyName);
        if (foundCompanies.isEmpty()) {
            return;
        }
        Recruitment recruitment = Recruitment.builder()
                .companyLogoPath(logo)
                .jobPosition(position)
                .companyName(companyName)
                .build();
        recruitmentRepository.save(recruitment);
    }
}
