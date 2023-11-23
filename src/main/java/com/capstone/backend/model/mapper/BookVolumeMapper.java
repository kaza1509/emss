package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;

public class BookVolumeMapper {
    public static BookVolumeDTOResponse toBookVolumeDTOResponse(BookVolume bookVolume) {
        return BookVolumeDTOResponse.builder()
                .name(bookVolume.getName())
                .id(bookVolume.getId())
                .active(bookVolume.getActive())
                .createdAt(bookVolume.getCreatedAt())
                .bookSeriesSubject(BookSeriesSubjectMapper.toBookSeriesSubjectDTOResponse(bookVolume.getBookSeriesSubject()))
                .build();
    }
    public static BookVolumeDTOResponse toBookVolumeDTOResponse(BookVolume bookVolume, String userName) {
        return BookVolumeDTOResponse.builder()
                .name(bookVolume.getName())
                .id(bookVolume.getId())
                .active(bookVolume.getActive())
                .createdAt(bookVolume.getCreatedAt())
                .bookSeriesSubject(BookSeriesSubjectMapper.toBookSeriesSubjectDTOResponse(bookVolume.getBookSeriesSubject()))
                .createBy(userName)
                .build();
    }
}
