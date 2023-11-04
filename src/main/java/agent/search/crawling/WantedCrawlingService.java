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
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.openqa.selenium.By.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class WantedCrawlingService implements Tasklet {

    public static final int SCROLL_DOWN_NUMBER = 20_000;

    private final WebDriver driver;

    private final WantedProperties properties;

    private final MilitaryCompanyRepository militaryCompanyRepository;

    private final RecruitmentRepository recruitmentRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        recruitmentRepository.deleteAll();
        driver.get(properties.getJobUrl());
        int currentScrollNumber = 0;
        while (currentScrollNumber < SCROLL_DOWN_NUMBER) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            currentScrollNumber++;
        }
        List<WebElement> foundJobs = driver.findElements(cssSelector("ul[data-cy='job-list'] li"));
        foundJobs.forEach(this::handleFoundJob);
        return RepeatStatus.FINISHED;
    }

    private void handleFoundJob(WebElement foundJob) {
        String logo;
        String position;
        String companyName;
        String originLink;
        try {
            String rawLogo = foundJob.findElement(tagName(properties.getCompanyLogoImgTag())).getCssValue("background-image");
            logo = rawLogo.replaceAll("^url\\([\"']?", "")
                    .replaceAll("[\"']?\\)$", "");
            originLink = foundJob.findElement(cssSelector(properties.getJobDetailLinkCss())).getDomAttribute("href");
            position = foundJob.findElement(cssSelector(properties.getJobPositionCss())).getText();
            companyName = foundJob.findElement(cssSelector(properties.getCompanyNameCss())).getText();
        } catch (NoSuchElementException e) {
            log.warn("wanted html markup might change", e);
            return;
        }
        List<MilitaryCompany> foundCompanies = militaryCompanyRepository.findByNameContaining(companyName);
        if (foundCompanies.isEmpty()) {
            return;
        }
        Recruitment recruitment = Recruitment.builder()
                .originLink(properties.getUrl() + originLink)
                .companyLogoPath(logo)
                .jobPosition(position)
                .companyName(companyName)
                .company(foundCompanies.get(0))
                .build();
        recruitmentRepository.save(recruitment);
    }

}
