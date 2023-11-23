package com.capstone.backend.repository;

import com.capstone.backend.entity.BookSeriesSubject;
import com.capstone.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookSeriesSubjectRepository extends JpaRepository<BookSeriesSubject, Long> {
    public Optional<BookSeriesSubject> findByIdAndActiveTrue(Long id);
    @Query("select bss from BookSeriesSubject bss where bss.active = true and bss.bookSeries.id = :bookSeriesId and bss.subject.id = :subjectId")
    public BookSeriesSubject findBySubjectAndBookSeries(Long bookSeriesId, Long subjectId);

    @Query("select bss.subject from BookSeriesSubject bss where bss.active = true and bss.bookSeries.id = :bookSeriesId")
    public List<Subject> findAllByBookSeries(Long bookSeriesId);
}
