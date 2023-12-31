package agent.search.crawling;

import agent.search.entity.JobPlanetCompany;
import agent.search.entity.JobPlanetReview;
import agent.search.entity.MilitaryCompany;
import agent.search.properties.JobPlanetProperties;
import agent.search.repository.JobPlanetCompanyRepository;
import agent.search.repository.JobPlanetReviewRepository;
import agent.search.repository.MilitaryCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.openqa.selenium.By.*;
import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobPlanetCrawlingService implements Tasklet {

    private final WebDriver driver;

    private final JobPlanetProperties properties;

    private final MilitaryCompanyRepository militaryCompanyRepository;

    private final JobPlanetCompanyRepository jobPlanetCompanyRepository;

    private final JobPlanetReviewRepository reviewRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        militaryCompanyRepository.findAll().forEach((company) -> {
            String url = getSourceUrl(company);
            driver.get(url);
            handle(company);
        });

        return RepeatStatus.FINISHED;
    }

    private String getSourceUrl(MilitaryCompany company) {
        String companyPureName = company.getPureName();
        return new StringBuilder()
                .append(properties.getUrl())
                .append("?")
                .append(properties.getQueryParameter())
                .append("=")
                .append(companyPureName)
                .toString();
    }

    private void handle(MilitaryCompany company) {
        String originLink = null;
        String reviewScore = null;
        try {
            List<WebElement> elements = driver.findElements(cssSelector(properties.getCompanyCardCss()));
            for (WebElement element : elements) {
                if (element.findElement(cssSelector(properties.getCompanyCategoryCss())).getText().equals(properties.getCompanyCategoryLabel())) {
                    originLink = element.findElement(tagName("a")).getAttribute("href");
                    reviewScore = element.findElement(cssSelector(properties.getCompanyReviewScoreCss())).getText();
                    break;
                }
            }
        } catch (NoSuchElementException ex) {
            return;
        }
        if (!hasText(reviewScore) || !hasText(originLink)) {
            return;
        }

        JobPlanetCompany jobPlanetCompany = JobPlanetCompany.builder()
                .company(company)
                .originLink(originLink)
                .build();
        if (jobPlanetCompanyRepository.findByCompany(company) == null) {
            jobPlanetCompanyRepository.save(jobPlanetCompany);
        }
        JobPlanetReview review = JobPlanetReview.builder()
                .jobPlanetCompany(jobPlanetCompany)
                .reviewScore(Float.valueOf(reviewScore))
                .build();
        reviewRepository.save(review);
    }

}
