package agent.search.jobs;

import agent.search.crawling.WantedCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class WantedCrawlJob {

    private static final String jobName = "WANTED_CRAWL_JOB";
    private static final String stepName = "WANTED_CRAWL_STEP";

    @Bean(name = jobName)
    @Order(value = 2)
    public Job job(JobRepository jobRepository,
                   @Qualifier(value = stepName) Step wantedCrawlJob,
                   WantedCrawlingService crawlingService
    ) {
        return new JobBuilder(jobName, jobRepository)
                .listener(crawlingService)
                .start(wantedCrawlJob)
                .build();
    }

    @Bean(name = stepName)
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder(stepName, jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED,transactionManager)
                .build();
    }
}
