package com.capstone.backend.model.dto.classes;

import com.capstone.backend.model.dto.BaseFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassDTOFilter extends BaseFilter {
    String name = "";
    Boolean active;
}
