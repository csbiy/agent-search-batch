package agent.search.entity;

import jakarta.persistence.*;
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

    private String originLink;

    private String companyName;

    private String companyLogoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    private MilitaryCompany company;
}
