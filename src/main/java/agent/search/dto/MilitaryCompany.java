package agent.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static agent.search.enumeration.MilitaryCompanyExcel.*;

@Getter
@ToString
@Builder
public class MilitaryCompany {

    private String name;

    private Integer year;

    private String manageLocation;

    private String companyLocation;

    private String companyNumber;

    private String companySize;

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
                .companyLocation(cast(row, COMPANY_ADDRESS.getColumnNumber(), String.class))
                .companyNumber(cast(row, COMPANY_NUMBER.getColumnNumber(), String.class))
                .companySize(cast(row, COMPANY_SIZE.getColumnNumber(), String.class))
                .suppAssignedNum(cast(row, SUPP_ASSIGN_NUM.getColumnNumber(), Integer.class))
                .suppEnlistNum(cast(row, SUPP_ENLIST_NUM.getColumnNumber(), Integer.class))
                .suppWorkingNum(cast(row, SUPP_WORK_NUM.getColumnNumber(), Integer.class))
                .activeAssignedNum(cast(row, ACTIVE_ASSIGN_NUM.getColumnNumber(), Integer.class))
                .activeEnlistNum(cast(row, ACTIVE_ENLIST_NUM.getColumnNumber(), Integer.class))
                .activeWorkingNum(cast(row, ACTIVE_WORK_NUM.getColumnNumber(), Integer.class))
                .build();
    }


}
