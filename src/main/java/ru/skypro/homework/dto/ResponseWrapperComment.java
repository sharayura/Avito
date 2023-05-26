package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;


@Data
public class ResponseWrapperComment {
    private final int count;
    private final List<CommentDto> results;
}
