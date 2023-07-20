package com.woopaca.knoo.entity.value;

public enum Campus {

    CHEONAN("천안"),
    GONGJU("공주");

    private final String campusName;

    Campus(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusName() {
        return campusName;
    }
}
