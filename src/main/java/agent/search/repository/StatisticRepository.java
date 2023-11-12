package agent.search.repository;

import agent.search.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic,String> {

    List<Statistic> findByPropertyAndCreatedAtBetween(String property,LocalDateTime startDateTime, LocalDateTime endDateTime);
}
