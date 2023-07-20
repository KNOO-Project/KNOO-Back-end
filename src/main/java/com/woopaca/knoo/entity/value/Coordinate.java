package com.woopaca.knoo.entity.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Coordinate {

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;
}
