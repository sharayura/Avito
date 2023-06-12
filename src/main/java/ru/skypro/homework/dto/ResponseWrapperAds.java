package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;


@Data
public class ResponseWrapperAds {
    private int count;
    private List<AdsDto> results;
}
