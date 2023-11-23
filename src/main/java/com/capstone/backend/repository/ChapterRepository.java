package com.capstone.backend.repository;

import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByIdAndActiveTrue(Long id);

    @Query("select cha from Chapter cha where cha.name = :chapterName and cha.id != :chapterIdPresent and cha.bookVolume.id = :bookVolumeId")
    public Optional<Chapter> findByName(String chapterName, Long chapterIdPresent, Long bookVolumeId);
    boolean existsChapterByBookVolumeAndActiveTrue(BookVolume bookVolume);

    @Query("select c from Chapter c join c.bookVolume bv where c.active = true " +
            "and bv.active = true and bv.id = :bookVolumeId")
    public List<Chapter> findAllByBookVolumeId(Long bookVolumeId);

    public List<Chapter> findChapterByActiveTrue();
}
