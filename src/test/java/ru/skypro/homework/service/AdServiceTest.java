package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.CommentService;
import ru.skypro.homework.service.impl.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private AdMapper adMapper;

    @Mock
    private CommentService commentService;

    private AdService adService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        adService = new AdService(adRepository, imageRepository, userRepository, userService, adMapper, commentService);
    }



    @Test
    public void testRemoveAd() {
        Integer id = 1;
        Ad ad = new Ad();
        Image image = new Image();
        ad.setImage(image);

        when(adRepository.findById(id)).thenReturn(Optional.of(ad));

        adService.removeAd(id);

        verify(imageRepository).delete(image);
        verify(commentService).deleteCommentsByAdId(id);
        verify(adRepository).deleteById(id);
    }

    @Test
    public void testUpdateAdImage() throws IOException {
        Integer adId = 1;
        MultipartFile image = new MockMultipartFile("image.jpg", new byte[0]);
        when(adRepository.findById(adId)).thenReturn(Optional.of(new Ad()));
        when(imageRepository.findById(adId)).thenReturn(Optional.of(new Image()));
        when(imageRepository.save(any(Image.class))).thenReturn(new Image());
        adService.updateAdImage(adId, image);

        verify(imageRepository).save(any(Image.class));
    }

}