package agent.search;

import agent.search.crawling.GovCrawlingService;
import agent.search.dto.MilitaryCompany;
import agent.search.properties.CrawlingProperties;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    @Bean
    public Job militaryCompanyCrawlJob(JobRepository jobRepository, Step step,
                                       GovCrawlingService crawlingService,
                                       DailyExcelFileParamIncrementer incrementer
    ) {
        return new JobBuilder("militaryCompanyCrawlJob", jobRepository)
                .listener(crawlingService)
                .incrementer(incrementer)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     GovCrawlingService crawlingService,
                     ItemReader<MilitaryCompany> excelReader
    ) {
        return new StepBuilder("step", jobRepository)
                .chunk(10, transactionManager)
                .listener(crawlingService)
                .reader(excelReader)
                .writer(System.out::println)
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<MilitaryCompany> excelReader(@Value("#{jobParameters['excelFileName']}") String excelFileName,
                                                      CrawlingProperties properties) {
        String path = new StringBuilder().append(properties.getDownloadDirectory())
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

    @Bean
    public WebDriver chromeDriver(CrawlingProperties properties) {
        System.setProperty("webdriver.chrome.driver", properties.getDriverPath());
        ChromeOptions options = new ChromeOptions();
        options.addArguments(properties.getChromeOptions());
        options.setExperimentalOption("prefs", Map.of("download.default_directory", properties.getDownloadDirectory()));
        return new ChromeDriver(options);
    }
}
