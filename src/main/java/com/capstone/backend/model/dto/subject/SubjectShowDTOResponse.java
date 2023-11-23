package com.capstone.backend.model.dto.subject;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectShowDTOResponse {
    Long id;
    String name;
    Boolean isDelete;
}
