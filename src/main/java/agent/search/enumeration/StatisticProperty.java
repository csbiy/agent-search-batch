package agent.search.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatisticProperty {

    SUPP_ENLIST_NUMBER("전일 보충역 편입 인원"),
    ACTIVE_ENLIST_NUMBER("전일 현역 편입 인원");


    private final String description;
}
