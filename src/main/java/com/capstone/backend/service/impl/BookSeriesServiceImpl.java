package com.capstone.backend.service.impl;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTORequest;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.mapper.BookSeriesMapper;
import com.capstone.backend.repository.BookSeriesRepository;
import com.capstone.backend.repository.ClassRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.BookSeriesCriteria;
import com.capstone.backend.service.BookSeriesService;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookSeriesServiceImpl implements BookSeriesService {
    BookSeriesRepository bookSeriesRepository;
    ClassRepository classRepository;
    MessageException messageException;
    BookSeriesCriteria bookSeriesCriteria;
    UserHelper userHelper;
    UserRepository userRepository;

    @Override
    public BookSeriesDTOResponse createBookSeries(BookSeriesDTORequest request, Long classId) {
        User userLogged = userHelper.getUserLogin();

        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository.findByName(request.getName(),0L , classId);
        if(bookSeriesOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate book series name in class");
        }

        Class classObject = classRepository
                .findById(classId)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        User user = userRepository.findById(classObject.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );

        BookSeries bookSeries = BookSeries.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .classObject(classObject)
                .build();

        bookSeries = bookSeriesRepository.save(bookSeries);
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries, user.getUsername());
    }

    @Override
    public BookSeriesDTOResponse updateBookSeries(Long id, BookSeriesDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();

        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));

        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository
                .findByName(request.getName(),bookSeries.getId() , bookSeries.getClassObject().getId());

        if(bookSeriesOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate book series name in class");
        }

        User user = userRepository.findById(bookSeries.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        // update book series
        bookSeries.setName(request.getName());
        bookSeries.setUserId(userLoggedIn.getId());


        bookSeries = bookSeriesRepository.save(bookSeries);
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        // find BookSeries id
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        if (isCanChangeStatus(bookSeries)) {
            bookSeries.setActive(!bookSeries.getActive());
            bookSeriesRepository.save(bookSeries);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not change status BookSeries because Subject already exists");
        }
    }

    @Override
    public PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter, Long classId) {
        Class classObject = classRepository.findById(classId).orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        return bookSeriesCriteria.searchBookSeries(bookSeriesDTOFilter, classObject.getId());
    }


    @Override
    public BookSeriesDTOResponse viewBookSeriesById(Long id) {
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        User user = userRepository.findById(bookSeries.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries,user.getUsername());
    }

    // Check exist subject in book series
    boolean isCanChangeStatus(BookSeries bookSeries) {
        return bookSeries.getBookSeriesSubjects().isEmpty();
    }

    @Override
    public List<BookSeriesDTOResponse> getListBookSeriesByClassesSubjectId(Long subjectId, Long classId) {
        List<BookSeries> bookSeries = new ArrayList<>();
        if(subjectId == null || classId == null) {
            bookSeries = bookSeriesRepository.findBookSeriesByActiveTrue();
        }
        else bookSeries = bookSeriesRepository.findAllBySubjectIdClassId(subjectId, classId);
        return bookSeries.stream().map(BookSeriesMapper::toBookseriesDTOResponse).toList();
    }

    @Override
    public List<BookSeriesDTOResponse> getListBookSeriesByClassId(Long classId) {
        List<BookSeries> bookSeries = new ArrayList<>();
        if(classId == null) {
            bookSeries = bookSeriesRepository.findBookSeriesByActiveTrue();
        }
        else bookSeries = bookSeriesRepository.findAllByClassId(classId);
        return bookSeries.stream().map(BookSeriesMapper::toBookseriesDTOResponse).toList();
    }
}
