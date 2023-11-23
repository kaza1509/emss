package com.capstone.backend.repository;

import com.capstone.backend.entity.BookVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookVolumeRepository extends JpaRepository<BookVolume, Long> {
//    Page<BookVolume> findBookVolumeByNameContainsAndActiveTrue(String name, Pageable pageable);
    Optional<BookVolume> findByIdAndActiveTrue(Long id);

    @Query("select bv from BookVolume bv where bv.name = :bookVolumeName and bv.id != :bookVolumeIdPresent and bv.bookSeriesSubject.id = :bookSeriesSubjectId")
    public Optional<BookVolume> findByName(String bookVolumeName, Long bookVolumeIdPresent, Long bookSeriesSubjectId);

    @Query("select bv from BookVolume bv join bv.bookSeriesSubject bss join bss.subject s " +
            "where bv.active = true and bss.active = true and s.active = true and s.id = :subjectId and bss.bookSeries.id = :bookSeriesId")
    public List<BookVolume> findAllBySubjectId(Long subjectId, Long bookSeriesId);

    public List<BookVolume> findBookVolumeByActiveTrue();

    @Query("select bv from BookVolume bv where bv.bookSeriesSubject.id = :bookSeriesSubjectId")
    public List<BookVolume> findBookVolumeByBookSeriesSubjectId(Long bookSeriesSubjectId);
}

