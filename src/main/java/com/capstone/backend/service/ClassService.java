package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.dto.classes.ClassDTORequest;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;

import java.util.List;

public interface ClassService {
    public ClassDTOResponse createClass(ClassDTORequest request);

    public ClassDTOResponse updateClass(Long id, ClassDTORequest request);

    Boolean changeStatus(Long id);

    public ClassDTOResponse viewClassById(Long id);

    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter);

    public List<ClassDTOResponse> getListClasses();

    public List<ClassDTOResponse> getListClassesBySubjectId(Long subjectId);
}
