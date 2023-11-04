package agent.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String jobPosition;

    private String companyName;

    private String companyLogoPath;
}
