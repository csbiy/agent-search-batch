package agent.search.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.core.ItemReadListener;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GovCrawlingService implements ItemReadListener {

    public static final String FILE_DIR = "C:\\Users\\User\\Desktop\\search-batch\\src\\main\\resources";

    @Override
    public void beforeRead() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver-win64/chromedriver.exe");
        Map<String, Object> prefs = new HashMap<>();

        prefs.put("download.default_directory", FILE_DIR);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=chrome");
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://work.mma.go.kr/caisBYIS/search/byjjecgeomsaek.do?eopjong_gbcd_yn=1&eopjong_gbcd=1&menu_id=m_m6_1");

        List.of(driver.findElement(By.id("eopjong_cd12")),
                driver.findElement(By.id("eopjong_cd13")),
                driver.findElement(By.id("eopjong_cd14")),
                driver.findElement(By.id("eopjong_cd15"))).forEach(WebElement::click);
        driver.findElement(By.cssSelector(".icon_search")).findElement(By.tagName("a")).click();
        driver.findElement(By.cssSelector(".icon_print")).findElement(By.tagName("a")).click();

        new WebDriverWait(driver, Duration.ofMinutes(5L))
                .until((d) -> new File(FILE_DIR + "\\병역지정업체검색_20231104.xls").exists());
    }

}
