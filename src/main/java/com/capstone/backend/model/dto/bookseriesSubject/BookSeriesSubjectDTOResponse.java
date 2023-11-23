package com.capstone.backend.model.dto.bookseriesSubject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSeriesSubjectDTOResponse {
    Long id;
    Boolean active;
    LocalDateTime createdAt;
    String subjectName;
}
