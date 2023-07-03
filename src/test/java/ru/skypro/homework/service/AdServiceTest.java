package ru.skypro.homework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.ArgumentMatchers.*;

import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;

import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.CommentService;
import ru.skypro.homework.service.impl.UserService;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Test
    public void testGetAllAds() {
        List<Ad> mockAds = new ArrayList<>();
        Ad ad1 = new Ad();
        ad1.setId(1);
        ad1.setTitle("Title 1");
        ad1.setDescription("Description 1");
        mockAds.add(ad1);

        Ad ad2 = new Ad();
        ad2.setId(2);
        ad2.setTitle("Title 2");
        ad2.setDescription("Description 2");
        mockAds.add(ad2);

        List<AdsDto> expectedAdsDtoList = new ArrayList<>();
        AdsDto expectedAdDto1 = new AdsDto();
        expectedAdDto1.setTitle(ad1.getTitle());
        expectedAdsDtoList.add(expectedAdDto1);

        AdsDto expectedAdDto2 = new AdsDto();
        expectedAdDto2.setTitle(ad2.getTitle());
        expectedAdsDtoList.add(expectedAdDto2);

        ResponseWrapperAds expectedResponse = new ResponseWrapperAds();
        expectedResponse.setCount(mockAds.size());
        expectedResponse.setResults(expectedAdsDtoList);

        when(adRepository.findAll()).thenReturn(mockAds);
        when(adMapper.adListToAdsDtoList(mockAds)).thenReturn(expectedAdsDtoList);

        ResponseWrapperAds result = adService.getAllAds();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResults());
        assertEquals(mockAds.size(), result.getCount());
        assertEquals(expectedResponse, result);
    }
}

