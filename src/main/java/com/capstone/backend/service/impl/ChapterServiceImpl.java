package com.capstone.backend.service.impl;

import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.entity.Chapter;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTORequest;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.capstone.backend.model.mapper.ChapterMapper;
import com.capstone.backend.repository.BookVolumeRepository;
import com.capstone.backend.repository.ChapterRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.ChapterCriteria;
import com.capstone.backend.service.ChapterService;
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
public class ChapterServiceImpl implements ChapterService {
    ChapterRepository chapterRepository;
    BookVolumeRepository bookVolumeRepository;
    ChapterCriteria chapterCriteria;
    UserHelper userHelper;
    MessageException messageException;
    UserRepository userRepository;

    @Override
    public ChapterDTOResponse createChapter(ChapterDTORequest request, Long bookVolumeId) {
        User userLogged = userHelper.getUserLogin();

        Optional<Chapter> chapterOptional = chapterRepository.findByName(request.getName(), 0L, bookVolumeId);
        if (chapterOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate chapter name in book volume");
        }

        BookVolume bookVolume = bookVolumeRepository
                .findById(bookVolumeId)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        //add chapter
        Chapter chapter = Chapter.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .bookVolume(bookVolume)
                .build();
        User user = userRepository.findById(bookVolume.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        chapter = chapterRepository.save(chapter);
        return ChapterMapper.toChapterDTOResponse(chapter, user.getUsername());
    }

    @Override
    public ChapterDTOResponse updateChapter(Long id, ChapterDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Chapter chapter = chapterRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));

        Optional<Chapter> chapterOptional = chapterRepository.findByName(request.getName(), id, chapter.getBookVolume().getId());
        if (chapterOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate chapter name in book volume");
        }
        // find chapter id want to update
        User user = userRepository.findById(chapter.getBookVolume().getId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        //update
        chapter.setName(request.getName());
        chapter.setUserId(user.getId());

        chapter = chapterRepository.save(chapter);
        return ChapterMapper.toChapterDTOResponse(chapter, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        //find id want to delete
        Chapter chapter = chapterRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        //delete
        // check can delete
        if (isCanChangeStatus(chapter)) {
            chapter.setActive(!chapter.getActive());
            chapterRepository.save(chapter);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not change status Chapter because Lesson already exists");
        }
    }

    @Override
    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter, Long bookVolumeId) {
        BookVolume bookVolume = bookVolumeRepository.findById(bookVolumeId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        return chapterCriteria.searchChapter(chapterDTOFilter, bookVolume.getId());
    }


    @Override
    public ChapterDTOResponse viewChapterById(Long id) {
        Chapter chapter = chapterRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        User user = userRepository.findById(chapter.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return ChapterMapper.toChapterDTOResponse(chapter, user.getUsername());
    }

    @Override
    public List<ChapterDTOResponse> getListChapterByBookVolumeId(Long bookVolumeId) {
        List<Chapter> chapters = new ArrayList<>();
        if(bookVolumeId == null) {
            chapters = chapterRepository.findChapterByActiveTrue();
        }
        else {
            chapters = chapterRepository.findAllByBookVolumeId(bookVolumeId);
        }
        return chapters.stream().map(ChapterMapper::toChapterDTOResponse).toList();
    }

    // Check exist lesson in chapter
    boolean isCanChangeStatus(Chapter chapter) {
        return chapter.getLessonList().isEmpty();
    }
}
