package com.capstone.backend.repository;

import com.capstone.backend.entity.Chapter;
import com.capstone.backend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findByIdAndActiveTrue(Long id);

    @Query("select le from Lesson le where le.name = :lessonName and le.id != :lessonIdPresent and le.chapter.id = :chapterId")
    public Optional<Lesson> findByName(String lessonName, Long lessonIdPresent, Long chapterId);

    boolean existsLessonByChapterAndActiveTrue(Chapter chapter);

    @Query("select le from Lesson le join le.chapter cha where le.active = true and " +
            "cha.active = true and cha.id = :chapterId")
    public List<Lesson> findAllByChapterId(Long chapterId);

    public List<Lesson> findLessonByActiveTrue();
}
