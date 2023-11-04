package agent.search;

import agent.search.properties.MilitaryExcelProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
@RequiredArgsConstructor
public class DailyExcelFileParamIncrementer implements JobParametersIncrementer {

    private final MilitaryExcelProperties properties;

    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder()
                .addString("excelFileName", String.format(properties.getExcelFileName(), getCurrentDate()))
                .toJobParameters();
    }

    public String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(properties.getExcelFileDateFormat()));
    }
}
