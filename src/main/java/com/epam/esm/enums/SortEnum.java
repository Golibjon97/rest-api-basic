package com.epam.esm.enums;

public enum SortEnum {
    NAME_ASC,
    NAME_DESC,
    DATE_ASC,
    DATE_DESC,
    NAME_DATE_ASC,
    NAME_DATE_DESC,
    NOT_SORT,
    UNKNOWN
    ;

    public static SortEnum findByName(String sortingType){
        if (sortingType==null)
            return NOT_SORT;

        for (SortEnum value : values()) {
            if (value.name().equalsIgnoreCase(sortingType))
                return value;
        }
        return UNKNOWN;
    }

}
