package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;


import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorImage", expression = "java(getImage(comment))")
    CommentDto toCommentDto(Comment comment);

    default String getImage(Comment comment) {
        if (comment.getUser().getImage() == null) {
            return null;
        }
        return "/users/image/" + comment.getUser().getId() + "/from-db";
    }

    List<CommentDto> commentListToCommentDtoList(List<Comment> commentList);
}
