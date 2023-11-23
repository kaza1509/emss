package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Chapter;
import com.capstone.backend.entity.Lesson;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTORequest;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import com.capstone.backend.model.mapper.LessonMapper;
import com.capstone.backend.repository.ChapterRepository;
import com.capstone.backend.repository.LessonRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.LessonCriteria;
import com.capstone.backend.service.LessonService;
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
public class LessonServiceImpl implements LessonService {
    LessonRepository lessonRepository;
    ChapterRepository chapterRepository;
    LessonCriteria lessonCriteria;
    UserHelper userHelper;
    MessageException messageException;
    UserRepository userRepository;

    @Override
    public LessonDTOResponse createLesson(LessonDTORequest request, Long chapterId) {
        User userLogged = userHelper.getUserLogin();

        Optional<Lesson> optionalLesson = lessonRepository.findByName(request.getName(), 0L, chapterId);
        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate lesson name in chapter");
        }

        //find chapter id
        Chapter chapter = chapterRepository.findById(chapterId).get();
        User user = userRepository.findById(chapter.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        Lesson lesson = Lesson.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .chapter(chapter)
                .build();
        lesson = lessonRepository.save(lesson);
        return LessonMapper.toLessonDTOResponse(lesson, user.getUsername());
    }

    @Override
    public LessonDTOResponse updateLesson(Long id, LessonDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        //find Lesson id want to update
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));

        Optional<Lesson> optionalLesson = lessonRepository
                .findByName(request.getName(), id, lesson.getChapter().getId());
        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate lesson name in chapter");
        }

        User user = userRepository.findById(lesson.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        //update
        lesson.setName(request.getName());
        lesson.setUserId(userLogged.getId());

        lesson = lessonRepository.save(lesson);
        return LessonMapper.toLessonDTOResponse(lesson, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        //find Lesson id want to delete
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        if (lesson.getResourceList().isEmpty()) {
            lesson.setActive(!lesson.getActive());
        } else {
            throw ApiException.conflictResourceException("Can not change status Lesson because Resource already exists");
        }
        lessonRepository.save(lesson);
    }

    @Override
    public PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter, Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_CHAPTER_NOT_FOUND));
        return lessonCriteria.searchLesson(lessonDTOFilter, chapter.getId());
    }


    @Override
    public LessonDTOResponse viewLessonById(Long id) {
        //find lesson id
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        User user = userRepository.findById(lesson.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return LessonMapper.toLessonDTOResponse(lesson, user.getUsername());
    }

    @Override
    public List<LessonDTOResponse> getListLessonsByChapterId(Long chapterId) {
        List<Lesson> lessons = new ArrayList<>();
        if(chapterId == null) {
            lessons = lessonRepository.findLessonByActiveTrue();
        }
        else {
            lessons = lessonRepository.findAllByChapterId(chapterId);
        }
        return lessons.stream().map(LessonMapper::toLessonDTOResponse).toList();
    }
}
