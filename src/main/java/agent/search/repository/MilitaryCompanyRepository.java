package agent.search.repository;

import agent.search.entity.MilitaryCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MilitaryCompanyRepository extends JpaRepository<MilitaryCompany,Long> {

    Optional<MilitaryCompany> findByName(String name);
}
