package agent.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Statistic {
    @Id
    private String property;

    private String value;

    public static Statistic ofPropertyAndValue(String property, Object value){
        Statistic statistic = new Statistic();
        statistic.property = property;
        statistic.value = String.valueOf(value);
        return statistic;
    }

    @CreatedDate
    private LocalDateTime createdAt;

    public int getDifference(int value){
        return Math.abs(Integer.parseInt(this.value) - value);
    }
}
