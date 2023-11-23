package com.capstone.backend.service.impl;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.entity.BookSeriesSubject;
import com.capstone.backend.entity.Subject;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOFilter;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectShowDTOResponse;
import com.capstone.backend.model.mapper.BookSeriesSubjectMapper;
import com.capstone.backend.repository.BookSeriesRepository;
import com.capstone.backend.repository.BookSeriesSubjectRepository;
import com.capstone.backend.repository.SubjectRepository;
import com.capstone.backend.repository.criteria.BookSeriesSubjectCriteria;
import com.capstone.backend.service.BookSeriesSubjectService;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookSeriesSubjectServiceImpl implements BookSeriesSubjectService {
    BookSeriesRepository bookSeriesRepository;
    SubjectRepository subjectRepository;
    BookSeriesSubjectRepository bookSeriesSubjectRepository;
    BookSeriesSubjectCriteria bookSeriesSubjectCriteria;
    MessageException messageException;
    UserHelper userHelper;

    @Override
    public Boolean changeSubjectInBookSeries(List<Long> subjects, Long bookSeriesId) {
        log.info("request subject: {}",subjects);

        BookSeries bookSeries = bookSeriesRepository.findById(bookSeriesId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_BOOK_SERIES_NOT_FOUND));
        List<Long> subjectPresents = bookSeries.getBookSeriesSubjects().stream()
                .map(BookSeriesSubject::getSubject)
                .map(Subject::getId)
                .toList();
        log.info("subject present: {}", subjectPresents);


        List<Long> listAdded = subjects.stream()
                .filter(s -> !subjectPresents.contains(s))
                .toList();

        List<Long> listDeleted = subjectPresents.stream()
                .filter(s -> !subjects.contains(s))
                .toList();

        log.info("list subject added: {}",listAdded);
        log.info("list subject deleted: {}", listDeleted);

        if(!listAdded.isEmpty()) {
            List<BookSeriesSubject> listBookSeriesSubjectAdd = listAdded.stream()
                    .map(s -> toBookSeriesSubject(bookSeries, s))
                    .toList();
            bookSeriesSubjectRepository.saveAll(listBookSeriesSubjectAdd);
        }

        if(!listDeleted.isEmpty()) {
            List<BookSeriesSubject> listBookSeriesSubjectDelete = listDeleted.stream()
                    .map(s -> bookSeriesSubjectRepository.findBySubjectAndBookSeries(bookSeries.getId(), s))
                    .toList();
            bookSeriesSubjectRepository.deleteAll(listBookSeriesSubjectDelete);
        }
        return true;
    }

    public BookSeriesSubject toBookSeriesSubject(BookSeries bs, Long subjectId) {
        User userLoggedIn = userHelper.getUserLogin();
        Subject subject = subjectRepository.findById(subjectId).get();
        return BookSeriesSubject.builder()
                .active(true)
                .subject(subject)
                .bookSeries(bs)
                .userId(userLoggedIn.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public List<SubjectShowDTOResponse> viewBookSeriesSubjectById(Long bookSeriesId) {
        BookSeries bookSeries = bookSeriesRepository
                .findById(bookSeriesId).orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        List<BookSeriesSubject> bookSeriesSubjects =bookSeries.getBookSeriesSubjects();
        return bookSeriesSubjects.stream()
                .map(BookSeriesSubjectMapper::toSubjectShowDTOResponse)
                .toList();
    }

    @Override
    public PagingDTOResponse searchBookSeriesSubject(BookSeriesSubjectDTOFilter bookSeriesSubjectDTOFilter, Long bookSeriesId) {
        BookSeries bs = bookSeriesRepository.findById(bookSeriesId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_BOOK_SERIES_NOT_FOUND));
        return bookSeriesSubjectCriteria.searchBookSeriesSubject(bookSeriesSubjectDTOFilter, bookSeriesId);
    }

    @Override
    public List<BookSeriesSubjectDTOResponse> getListBookSeriesSubject() {
        return bookSeriesSubjectRepository.findAll()
                .stream().map(BookSeriesSubjectMapper::toBookSeriesSubjectDTOResponse)
                .toList();
    }
}
