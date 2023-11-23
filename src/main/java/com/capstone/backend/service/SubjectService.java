package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTORequest;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;

import java.util.List;

public interface SubjectService {
    public SubjectDTOResponse createSubject(SubjectDTORequest request);


    public SubjectDTOResponse updateSubject(Long id, SubjectDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter);

    public SubjectDTOResponse viewSubjectById(Long id);

    public List<SubjectDTOResponse> getListSubjects();

    public List<SubjectDTOResponse> getListSubjectsByBookSeries(Long bookSeriesId);
}
