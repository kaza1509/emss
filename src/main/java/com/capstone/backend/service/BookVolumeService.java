package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOFilter;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTORequest;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;

import java.util.List;

public interface BookVolumeService {
    BookVolumeDTOResponse createBookVolume(BookVolumeDTORequest request, Long bookSeriesSubjectId);

    BookVolumeDTOResponse updateBookVolume(Long id, BookVolumeDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter, Long bookSeriesSubjectId);


    BookVolumeDTOResponse viewBookVolumeById(Long id);

    public List<BookVolumeDTOResponse> getListBookVolumeBySubjectId(Long subjectId, Long bookSeriesId);

    public List<BookVolumeDTOResponse> getListBookVolumeByBookSeriesSubjectId(Long bookSeriesSubjectId);
}
