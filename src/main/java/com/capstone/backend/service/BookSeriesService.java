package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTORequest;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;

import java.util.List;

public interface BookSeriesService {
    public BookSeriesDTOResponse createBookSeries(BookSeriesDTORequest request, Long classId);

    public BookSeriesDTOResponse updateBookSeries(Long id, BookSeriesDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter, Long classId);

    public BookSeriesDTOResponse viewBookSeriesById(Long id);

    public List<BookSeriesDTOResponse> getListBookSeriesByClassesSubjectId(Long subjectId, Long classId);

    public List<BookSeriesDTOResponse> getListBookSeriesByClassId(Long classId);
}
