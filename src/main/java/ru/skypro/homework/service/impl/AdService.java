package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.List;


@Service
public class AdService {
    private final AdRepository adRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AdMapper adMapper;

    public AdService(AdRepository adRepository, ImageRepository imageRepository, UserRepository userRepository, UserService userService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.adMapper = adMapper;
    }

    public ResponseWrapperAds getAllAds() {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        List<Ad> adList = adRepository.findAll();
        responseWrapperAds.setResults(adMapper.adListToAdsDtoList(adList));
        responseWrapperAds.setCount(adList.size());
        return responseWrapperAds;
    }

    @Transactional(readOnly = true)
    public Image getAdImage(Integer adId) {
        return adRepository.findById(adId).map(Ad::getImage).orElse(null);
    }

   @Transactional
    public AdsDto addAd(CreateAdsDto properties, MultipartFile file) throws IOException {
        Ad ad = adMapper.toAd(properties);
        Image image = new Image();

        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());
        image.setData(file.getBytes());
        imageRepository.save(image);
        ad.setImage(image);
        ad.setUser(userRepository.findByUsername(userService.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        adRepository.save(ad);
        return adMapper.toAdsDto(ad);
    }

    @Transactional
    public FullAdsDto getAds(Integer id) {
        return adRepository.findById(id).map(adMapper::toFullAdsDto).orElse(null);
    }

    @Transactional
    public ResponseWrapperAds getAdsMe() {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        User user = userRepository.findByUsername(userService.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Ad> adList = adRepository.findAllByUserId(user.getId());
        responseWrapperAds.setResults(adMapper.adListToAdsDtoList(adList));
        responseWrapperAds.setCount(adList.size());
        return responseWrapperAds;
    }

    @Transactional
    public void removeAd(Integer id) {
        adRepository.deleteById(id);
    }

}
