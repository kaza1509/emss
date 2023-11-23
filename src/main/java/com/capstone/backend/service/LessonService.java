package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTORequest;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;

import java.util.List;

public interface LessonService {
    public LessonDTOResponse createLesson(LessonDTORequest request, Long chapterId);

    public LessonDTOResponse updateLesson(Long id, LessonDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter,Long chapterId);
    public LessonDTOResponse viewLessonById(Long id);

    public List<LessonDTOResponse> getListLessonsByChapterId(Long chapterId);
}
