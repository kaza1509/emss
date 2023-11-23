package com.capstone.backend.model.dto.bookvolume;

import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookVolumeDTOResponse {
    Long id;
    String name;
    Boolean active;
    LocalDateTime createdAt;
    @JsonIgnore
    SubjectDTOResponse subjectDTOResponse;
    @JsonIgnore
    BookSeriesSubjectDTOResponse bookSeriesSubject;
    String createBy;
}
