package com.woopaca.knoo.controller.dto;

import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class PageDto {

    @Min(value = 1, message = "유효하지 않은 페이지")
    private final int page;

    public PageDto(int page) {
        this.page = page;
    }
}
