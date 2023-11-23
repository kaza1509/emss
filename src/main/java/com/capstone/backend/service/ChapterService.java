package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTORequest;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;

import java.util.List;

public interface ChapterService {
    public ChapterDTOResponse createChapter(ChapterDTORequest request, Long bookVolumeId);

    public ChapterDTOResponse updateChapter(Long id, ChapterDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter,Long bookVolumeId);

    public ChapterDTOResponse viewChapterById(Long id);

    public List<ChapterDTOResponse> getListChapterByBookVolumeId(Long bookVolumeId);
}
