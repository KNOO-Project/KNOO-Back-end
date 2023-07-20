package com.woopaca.knoo.entity.value;

public enum CuisineType {

    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    FAST_FOOD("패스트 푸드");

    private final String typeName;

    CuisineType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
