package agent.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static agent.search.enumeration.MilitaryCompanyExcel.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer year;

    private String manageLocation;

    private String location;

    private String phoneNumber;

    private String size;

    private Integer suppAssignedNum;

    private Integer suppEnlistNum;

    private Integer suppWorkingNum;

    private Integer activeAssignedNum;

    private Integer activeEnlistNum;

    private Integer activeWorkingNum;

    public static MilitaryCompany fromExcelRow(String[] row) {
        return builder()
                .name(cast(row, COMPANY_NAME.getColumnNumber(), String.class))
                .year(cast(row, CREATE_YEAR.getColumnNumber(), Integer.class))
                .manageLocation(cast(row, GOV.getColumnNumber(), String.class))
                .location(cast(row, COMPANY_ADDRESS.getColumnNumber(), String.class))
                .phoneNumber(cast(row, COMPANY_NUMBER.getColumnNumber(), String.class))
                .size(cast(row, COMPANY_SIZE.getColumnNumber(), String.class))
                .suppAssignedNum(cast(row, SUPP_ASSIGN_NUM.getColumnNumber(), Integer.class))
                .suppEnlistNum(cast(row, SUPP_ENLIST_NUM.getColumnNumber(), Integer.class))
                .suppWorkingNum(cast(row, SUPP_WORK_NUM.getColumnNumber(), Integer.class))
                .activeAssignedNum(cast(row, ACTIVE_ASSIGN_NUM.getColumnNumber(), Integer.class))
                .activeEnlistNum(cast(row, ACTIVE_ENLIST_NUM.getColumnNumber(), Integer.class))
                .activeWorkingNum(cast(row, ACTIVE_WORK_NUM.getColumnNumber(), Integer.class))
                .build();
    }

    public void update(MilitaryCompany newMilitaryCompany) {
        this.year = newMilitaryCompany.year;
        this.manageLocation = newMilitaryCompany.manageLocation;
        this.location = newMilitaryCompany.location;
        this.phoneNumber = newMilitaryCompany.phoneNumber;
        this.size = newMilitaryCompany.size;
        this.suppAssignedNum = newMilitaryCompany.suppAssignedNum;
        this.suppEnlistNum = newMilitaryCompany.suppEnlistNum;
        this.suppWorkingNum = newMilitaryCompany.suppWorkingNum;
        this.activeAssignedNum = newMilitaryCompany.activeAssignedNum;
        this.activeEnlistNum = newMilitaryCompany.activeEnlistNum;
        this.activeWorkingNum = newMilitaryCompany.activeWorkingNum;
    }
}
