package com.capstone.backend.model.dto.bookvolume;

import com.capstone.backend.model.dto.BaseFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookVolumeDTOFilter extends BaseFilter {
    String name = "";
    Boolean active;
}
