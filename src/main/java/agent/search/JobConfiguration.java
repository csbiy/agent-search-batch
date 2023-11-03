package agent.search;

import agent.search.crawling.GovCrawlingService;
import agent.search.reader.ExcelItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    @Bean
    public Job testJob(JobRepository jobRepository, Step step){
        return new JobBuilder("job",jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step",jobRepository)
                .chunk(10,transactionManager)
                .listener(new GovCrawlingService())
                .reader(new ExcelItemReader())
                .processor(item -> item)
                .writer(System.out::println)
                .build();
    }
}
