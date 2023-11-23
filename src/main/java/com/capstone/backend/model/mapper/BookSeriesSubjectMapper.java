package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.BookSeriesSubject;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectShowDTOResponse;

public class BookSeriesSubjectMapper {


    public static BookSeriesSubjectDTOResponse toBookSeriesSubjectDTOResponse(BookSeriesSubject bookSeriesSubject) {
        if(bookSeriesSubject == null) {
            return null;
        }
        return BookSeriesSubjectDTOResponse.builder()
                .id(bookSeriesSubject.getId())
                .active(bookSeriesSubject.getActive())
                .createdAt(bookSeriesSubject.getCreatedAt())
                .subjectName(bookSeriesSubject.getSubject().getName())
                .build();
    }

    public static SubjectShowDTOResponse toSubjectShowDTOResponse(BookSeriesSubject bss) {
        return SubjectShowDTOResponse.builder()
                .name(bss.getSubject().getName())
                .id(bss.getSubject().getId())
                .isDelete(bss.getBookVolumes().isEmpty())
                .build();
    }
}
