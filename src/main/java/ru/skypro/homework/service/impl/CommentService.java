package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {

    public final CommentMapper commentMapper;
    public final CommentRepository commentRepository;
    public final UserRepository userRepository;
    public final UserService userService;
    public final AdRepository adRepository;

    public CommentService(CommentMapper commentMapper, CommentRepository commentRepository, UserRepository userRepository, UserService userService, AdRepository adRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.adRepository = adRepository;
    }

    @org.springframework.transaction.annotation.Transactional
    public ResponseWrapperComment getComments(Integer id) {
        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        List<Comment> commentList = commentRepository.findAllByAdId(id);
        responseWrapperComment.setResults(commentMapper.commentListToCommentDtoList(commentList));
        responseWrapperComment.setCount(commentList.size());
        return responseWrapperComment;
    }

    @Transactional
    public CommentDto addComment(Integer id, CreateCommentDto createCommentDto) {
        Comment comment = commentMapper.toComment(createCommentDto);
        comment.setAd(adRepository.findById(id).orElse(null));
        comment.setUser(userRepository.findByUsername(userService.getCurrentUsername()));
        comment.setCreatedAt(System.currentTimeMillis());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
}
