package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOFilter;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectShowDTOResponse;

import java.util.List;

public interface BookSeriesSubjectService {
    public List<SubjectShowDTOResponse> viewBookSeriesSubjectById(Long bookSeriesId);

    public PagingDTOResponse searchBookSeriesSubject(BookSeriesSubjectDTOFilter bookSeriesSubjectDTOFilter, Long bookSeriesId);

    public List<BookSeriesSubjectDTOResponse> getListBookSeriesSubject();

    public Boolean changeSubjectInBookSeries(List<Long> subjects, Long bookSeriesId);
}
