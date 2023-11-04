package agent.search.dto;

import agent.search.enumeration.MilitaryCompanyExcel;
import lombok.Builder;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@Getter
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

    public static MilitaryCompany fromExcelRow(Row row){
        return builder().name(row.getCell(MilitaryCompanyExcel.COMPANY_NAME.getColumnNumber()).getStringCellValue())
                .year((int) row.getCell(MilitaryCompanyExcel.CREATE_YEAR.getColumnNumber()).getNumericCellValue())
                .manageLocation(row.getCell(MilitaryCompanyExcel.GOV.getColumnNumber()).getStringCellValue())
                .companyLocation(row.getCell(MilitaryCompanyExcel.COMPANY_ADDRESS.getColumnNumber()).getStringCellValue())
                .companyNumber(row.getCell(MilitaryCompanyExcel.COMPANY_NUMBER.getColumnNumber()).getStringCellValue())
                .companySize(row.getCell(MilitaryCompanyExcel.COMPANY_SIZE.getColumnNumber()).getStringCellValue())
                .suppAssignedNum((int) row.getCell(MilitaryCompanyExcel.SUPP_ASSIGN_NUM.getColumnNumber()).getNumericCellValue())
                .suppEnlistNum((int)row.getCell(MilitaryCompanyExcel.SUPP_ENLIST_NUM.getColumnNumber()).getNumericCellValue())
                .suppWorkingNum((int)row.getCell(MilitaryCompanyExcel.SUPP_WORK_NUM.getColumnNumber()).getNumericCellValue())
                .suppAssignedNum((int)row.getCell(MilitaryCompanyExcel.ACTIVE_ASSIGN_NUM.getColumnNumber()).getNumericCellValue())
                .suppEnlistNum((int)row.getCell(MilitaryCompanyExcel.ACTIVE_ENLIST_NUM.getColumnNumber()).getNumericCellValue())
                .suppWorkingNum((int)row.getCell(MilitaryCompanyExcel.ACTIVE_WORK_NUM.getColumnNumber()).getNumericCellValue())
                .build();


    }


}
