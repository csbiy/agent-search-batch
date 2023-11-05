package agent.search.jobs;

import agent.search.crawling.JobPlanetCrawlingService;
import agent.search.crawling.WantedCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobPlanetCrawlJob {

    public static final String jobName = "JOBPLANET_CRAWL_JOB";
    private static final String stepName = "JOBPLANET_CRAWL_STEP";

    @Bean(name = jobName)
    @Order(value = 3)
    public Job job(JobRepository jobRepository,
                   @Qualifier(value = stepName) Step jobPlanetCrawlStep
    ) {
        return new JobBuilder(jobName, jobRepository)
                .start(jobPlanetCrawlStep)
                .build();
    }

    @Bean(name = stepName)
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     JobPlanetCrawlingService tasklet
    ) {
        return new StepBuilder(stepName, jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
