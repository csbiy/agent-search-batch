package agent.search.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Getter
public enum MilitaryCompanyExcel {
    SEQ_NO(0, "순번"),
    COMPANY_NAME(1, "업체명"),
    CREATE_YEAR(2, "선정년도"),
    GOV(3, "지방청"),
    COMPANY_ADDRESS(4, "주소"),
    COMPANY_NUMBER(5, "전화번호"),
    COMPANY_FAX(6, "팩스번호"),
    COMPANY_DOMAIN(7, "업종"),
    COMPANY_SIZE(8, "기업규모"),
    COMPANY_PRODUCT(9, "주생산품목"),
    COMPANY_INVESTIGATE_DOMAIN(10, "연구분야"),
    SUPP_ASSIGN_NUM(11, "보충역 배정인원"),
    SUPP_ENLIST_NUM(12, "보충역 편입인원"),
    SUPP_WORK_NUM(13, "보충역 복무인원"),
    ACTIVE_ASSIGN_NUM(14, "현역 배정인원"),
    ACTIVE_ENLIST_NUM(15, "현역 편입인원"),
    ACTIVE_WORK_NUM(16, "현역 복무인원");


    private final int columnNumber;

    private final String label;


    public static <T> T cast(String[] row, int columnIndex, Class<T> classType) {
        if (columnIndex < 0 && columnIndex > row.length) {
            throw new IllegalArgumentException();
        }
        String value = row[columnIndex];
        if (!StringUtils.hasText(value)) {
            return null;
        }
        if (classType.equals(Integer.class)) {
            return classType.cast(Integer.valueOf(value));
        }
        if (classType.equals(Long.class)) {
            return classType.cast(Long.valueOf(value));
        }
        return classType.cast(value);
    }

}
