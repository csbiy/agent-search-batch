package agent.search;

import agent.search.crawling.JobPlanetCrawlingService;
import agent.search.jobs.JobPlanetCrawlJob;
import agent.search.jobs.MilitaryCompanyCrawlJob;
import agent.search.properties.MilitaryExcelProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrawlJobLauncher implements ApplicationRunner {

    private final List<? extends Job> jobs;

    private final JobLauncher jobLauncher;

    private final MilitaryExcelProperties properties;


    public void launchJobs() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("excelFileName", String.format(properties.getExcelFileName(), getCurrentDate()))
                .addString("randomParam",UUID.randomUUID().toString())
                .toJobParameters();
        jobs.forEach((job) -> {
            try {
                jobLauncher.run(job, jobParameters);
            } catch (Exception e) {
                log.error("error", e);
            }
        });
    }

    private String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(properties.getExcelFileDateFormat()));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        launchJobs();
    }


}
