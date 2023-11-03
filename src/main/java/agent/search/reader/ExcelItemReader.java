package agent.search.reader;

import agent.search.crawling.GovCrawlingService;
import agent.search.dto.MilitaryCompany;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExcelItemReader implements ItemReader<MilitaryCompany> {

    @Override
    public MilitaryCompany read() throws UnexpectedInputException, ParseException, NonTransientResourceException, IOException {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String path = GovCrawlingService.FILE_DIR + "\\병역지정업체검색_" + date + ".xls";

        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));


        return null;
    }
}
