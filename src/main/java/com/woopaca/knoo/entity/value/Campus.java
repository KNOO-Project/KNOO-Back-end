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

    public static Campus of(String campusName) {
        switch (campusName) {
            case "천안":
            case "cheonan": {
                return CHEONAN;
            }
            case "공주":
            case "gongju": {
                return GONGJU;
            }
        }
        return null;
    }
}
