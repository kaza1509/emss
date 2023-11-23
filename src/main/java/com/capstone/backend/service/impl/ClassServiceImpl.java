package com.capstone.backend.service.impl;


import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.dto.classes.ClassDTORequest;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.mapper.ClassMapper;
import com.capstone.backend.repository.ClassRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.ClassesCriteria;
import com.capstone.backend.service.ClassService;
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
public class ClassServiceImpl implements ClassService {
    ClassRepository classRepository;
    ClassesCriteria classCriteria;
    UserHelper userHelper;
    MessageException messageException;
    UserRepository userRepository;

    @Override
    public ClassDTOResponse createClass(ClassDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Class> classOptional = classRepository.findByName(request.getName(), 0L);
        if (classOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate class name");
        }

        // add entity
        Class classEntity = Class.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .build();
        User user = userRepository.findById(classEntity.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        classEntity = classRepository.save(classEntity);
        return ClassMapper.toClassDTOResponse(classEntity, user.getUsername());
    }

    @Override
    public ClassDTOResponse updateClass(Long id, ClassDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Class> classOptional = classRepository.findByName(request.getName(), id);
        if (classOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate class name");
        }

        // find Class id
        Class classObject = classRepository.findById(id).get();
        User user = userRepository
                .findById(classObject.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        // update class
        classObject.setName(request.getName());
        classObject.setUserId(userLogged.getId());


        classObject = classRepository.save(classObject);
        return ClassMapper.toClassDTOResponse(classObject, user.getUsername());
    }

    @Override
    public Boolean changeStatus(Long id) {
        // find Class id
        Class classObject = classRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        //change active to false
        if (classObject.getBookSeriesList().isEmpty()) {
            classObject.setActive(!classObject.getActive());
        }
        else {
            throw ApiException.conflictResourceException("Can not change status Class because Book Series already exists");
        }
        classObject = classRepository.save(classObject);
        return true;
    }


    @Override
    public ClassDTOResponse viewClassById(Long id) {
        Class classObject = classRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        User user = userRepository.findById(classObject.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return ClassMapper.toClassDTOResponse(classObject, user.getUsername());
    }

    @Override
    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter) {
        return classCriteria.searchClass(classDTOFilter);
    }

    @Override
    public List<ClassDTOResponse> getListClasses() {
        return classRepository.findClassByActiveIsTrue()
                .stream().map(ClassMapper::toClassDTOResponse)
                .toList();
    }

    @Override
    public List<ClassDTOResponse> getListClassesBySubjectId(Long subjectId) {
        List<Class> classes = new ArrayList<>();
        if(subjectId == null) {
            classes = classRepository.findClassByActiveIsTrue();
        }
        else classes = classRepository.findAllBySubjectId(subjectId);
        return classes.stream().map(ClassMapper::toClassDTOResponse).toList();
    }
}
