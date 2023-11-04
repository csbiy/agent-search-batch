package agent.search.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MilitaryCompanyExcel {
    SEQ_NO(1, "순번"),
    COMPANY_NAME(2, "업체명"),
    CREATE_YEAR(3, "선정년도"),
    GOV(4, "지방청"),
    COMPANY_ADDRESS(5, "주소"),
    COMPANY_NUMBER(6, "전화번호"),
    COMPANY_FAX(7, "팩스번호"),
    COMPANY_DOMAIN(8, "업종"),
    COMPANY_SIZE(9, "기업규모"),
    COMPANY_PRODUCT(10, "주생산품목"),
    COMPANY_INVESTIGATE_DOMAIN(11, "연구분야"),
    SUPP_ASSIGN_NUM(12, "보충역 배정인원"),
    SUPP_ENLIST_NUM(13, "보충역 편입인원"),
    SUPP_WORK_NUM(14, "보충역 복무인원"),
    ACTIVE_ASSIGN_NUM(15, "현역 배정인원"),
    ACTIVE_ENLIST_NUM(16, "현엋 편입인원"),
    ACTIVE_WORK_NUM(17, "현역 복무인원");


    private final int columnNumber;

    private final String label;
}
