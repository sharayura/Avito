package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.entity.Ad;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdMapper {

    Ad toAd(CreateAdsDto createAdsDto);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", expression="java(getImage(ad))")
    AdsDto toAdsDto(Ad ad);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "image", expression="java(getImage(ad))")
    FullAdsDto toFullAdsDto(Ad ad);

    default String getImage(Ad ad) {
        if (ad.getImage() == null) {
            return null;
        }
        return "/ads/image/" + ad.getId() + "/from-db";
    }

    List<AdsDto> adListToAdsDtoList(List<Ad> adList);


}
