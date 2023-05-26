package ru.skypro.homework.dto;

import lombok.Data;


import java.util.List;


@Data
public class ResponseWrapperAds {
    private final int count;
    private final List<AdsDto> results;
}
