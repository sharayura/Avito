package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

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

    @Transactional(readOnly = true)
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
        comment.setUser(userRepository.findByUsername(userService.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        comment.setCreatedAt(System.currentTimeMillis());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Transactional
    public void deleteCommentsByAdId(Integer adId) {
        commentRepository.deleteCommentsByAdId(adId);
    }
}
