package com.capstone.backend.repository;

import com.capstone.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    public Optional<Subject> findByIdAndActiveTrue(Long id);

    public List<Subject> findAllByActiveTrue();
    @Query("select s from Subject s where s.active = true")
    public List<Subject> findAllBySubjectActiveTrue();
    @Query("select bss.subject from BookSeriesSubject bss where bss.bookSeries.id = :bookSeriesId")
    public List<Subject> findSubjectByBookSeries(Long bookSeriesId);
    @Query("select s from Subject s where s.name = :subjectName and s.id != :subjectIdPresent")
    public Optional<Subject> findByName(String subjectName, Long subjectIdPresent);
}
