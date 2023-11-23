package com.capstone.backend.model.dto.bookseriesSubject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeSubjectDTORequest {
    List<Long> subjects;
}
