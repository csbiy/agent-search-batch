package agent.search;

import agent.search.properties.CrawlingProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Map;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan(value = "agent.search.properties")
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
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
