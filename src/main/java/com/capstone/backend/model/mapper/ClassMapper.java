package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Class;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;

public class ClassMapper {
    public static ClassDTOResponse toClassDTOResponse(Class classEntity) {
        return ClassDTOResponse.builder()
                .name(classEntity.getName())
                .id(classEntity.getId())
                .active(classEntity.getActive())
                .createdAt(classEntity.getCreatedAt())
                .build();
    }
    public static ClassDTOResponse toClassDTOResponse(Class classEntity, String userName) {
        return ClassDTOResponse.builder()
                .name(classEntity.getName())
                .id(classEntity.getId())
                .active(classEntity.getActive())
                .createdAt(classEntity.getCreatedAt())
                .createBy(userName)
                .build();
    }
}
