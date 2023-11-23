package com.capstone.backend.service.impl;

import com.capstone.backend.entity.BookSeriesSubject;
import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOFilter;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTORequest;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.mapper.BookVolumeMapper;
import com.capstone.backend.repository.BookSeriesSubjectRepository;
import com.capstone.backend.repository.BookVolumeRepository;
import com.capstone.backend.repository.ChapterRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.BookVolumeCriteria;
import com.capstone.backend.service.BookVolumeService;
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
public class BookVolumeServiceImpl implements BookVolumeService {
    BookVolumeRepository bookVolumeRepository;
    MessageException messageException;
    UserRepository userRepository;
    ChapterRepository chapterRepository;
    BookSeriesSubjectRepository bookSeriesSubjectRepository;
    BookVolumeCriteria bookVolumeCriteria;
    UserHelper userHelper;

    @Override
    public BookVolumeDTOResponse createBookVolume(BookVolumeDTORequest request, Long bookSeriesSubjectId) {
        User userLogged = userHelper.getUserLogin();

        Optional<BookVolume> optionalLesson = bookVolumeRepository.findByName(request.getName(), 0L, bookSeriesSubjectId);
        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate book volume name in subject");
        }

        BookSeriesSubject bookSeriesSubject = bookSeriesSubjectRepository.findById(bookSeriesSubjectId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SUBJECT_NOT_FOUND));
        // add entity
        BookVolume bookVolume = BookVolume.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .bookSeriesSubject(bookSeriesSubject)
                .build();
        User user = userRepository.findById(bookVolume.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        bookVolume = bookVolumeRepository.save(bookVolume);
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume, user.getUsername());
    }

    @Override
    public BookVolumeDTOResponse updateBookVolume(Long id, BookVolumeDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));

        Optional<BookVolume> optionalLesson = bookVolumeRepository.findByName(
                request.getName(),
                id,
                bookVolume.getBookSeriesSubject().getId()
        );

        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate book volume name in subject");
        }

        User user = userRepository
                .findById(bookVolume.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        //update BookVolume
        bookVolume.setName(request.getName());
        bookVolume.setUserId(userLogged.getId());

        bookVolume = bookVolumeRepository.save(bookVolume);
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume, user.getUsername());
    }

    public void changeStatus(Long id) {
        //find BookVolume want delete
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        if (isCanChangeStatus(bookVolume)) {
            bookVolume.setActive(!bookVolume.getActive());
            bookVolumeRepository.save(bookVolume);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not change status BookVolume because Chapter already exists");
        }

    }

    @Override
    public PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter, Long bookSeriesSubjectId) {
        BookSeriesSubject bookSeriesSubject = bookSeriesSubjectRepository.findById(bookSeriesSubjectId)
                .orElseThrow(() -> ApiException.notFoundException("Subject in book series is not found"));
        return bookVolumeCriteria.searchBookVolume(bookVolumeDTOFilter, bookSeriesSubjectId);
    }


    @Override
    public BookVolumeDTOResponse viewBookVolumeById(Long id) {
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        User user = userRepository.findById(bookVolume.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume, user.getUsername());
    }

    @Override
    public List<BookVolumeDTOResponse> getListBookVolumeBySubjectId(Long subjectId, Long bookSeriesId) {
        List<BookVolume> bookVolumes = new ArrayList<>();
        if(subjectId == null || bookSeriesId == null) {
            bookVolumes = bookVolumeRepository.findBookVolumeByActiveTrue();
        }
        else {
            bookVolumes = bookVolumeRepository.findAllBySubjectId(subjectId, bookSeriesId);
        }
        return bookVolumes.stream().map(BookVolumeMapper::toBookVolumeDTOResponse).toList();
    }

    @Override
    public List<BookVolumeDTOResponse> getListBookVolumeByBookSeriesSubjectId(Long bookSeriesSubjectId) {
        List<BookVolume> bookVolumes = new ArrayList<>();
        if(bookSeriesSubjectId == null) {
            bookVolumes = bookVolumeRepository.findBookVolumeByActiveTrue();
        }
        else {
            bookVolumes = bookVolumeRepository.findBookVolumeByBookSeriesSubjectId(bookSeriesSubjectId);
        }
        return bookVolumes.stream()
                .map(BookVolumeMapper::toBookVolumeDTOResponse)
                .toList();
    }

    boolean isCanChangeStatus(BookVolume bookVolume) {
        return bookVolume.getChapterList().isEmpty();
    }
}