package agent.search.jobs;

import agent.search.crawling.MilitaryCompanyCrawlingService;
import agent.search.entity.MilitaryCompany;
import agent.search.properties.CrawlingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MilitaryCompanyCrawlJob {

    private static final String jobName = "MILITARY_COMPANY_CRAWL_JOB";
    private static final String stepName = "MILITARY_COMPANY_CRAWL_STEP";

    @Bean(name = jobName)
    @Order(value = 1)
    public Job job(JobRepository jobRepository,
                   @Qualifier(value = stepName) Step step,
                   MilitaryCompanyCrawlingService service
    ) {
        return new JobBuilder(jobName, jobRepository)
                .listener(service)
                .start(step)
                .build();
    }

    @Bean(name = stepName)
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<MilitaryCompany> reader,
                     ItemWriter militaryCompanyWriter
    ) {
        return new StepBuilder(stepName, jobRepository)
                .chunk(10, transactionManager)
                .reader(reader)
                .writer(militaryCompanyWriter)
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<MilitaryCompany> excelReader(@Value("#{jobParameters['excelFileName']}") String excelFileName,
                                                      CrawlingProperties properties) {
        String path = new StringBuilder()
                .append(properties.getDownloadDirectory())
                .append("\\")
                .append(excelFileName)
                .toString();

        PoiItemReader<MilitaryCompany> reader = new PoiItemReader<>();
        reader.setResource(new FileSystemResource(path));
        reader.setRowMapper(rowMapper());
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public RowMapper<MilitaryCompany> rowMapper() {
        return (rs) -> {
            String[] row = rs.getCurrentRow();
            return MilitaryCompany.fromExcelRow(row);
        };
    }

}
