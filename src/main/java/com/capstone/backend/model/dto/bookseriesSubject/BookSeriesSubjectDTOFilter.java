package com.capstone.backend.model.dto.bookseriesSubject;

import com.capstone.backend.model.dto.BaseFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSeriesSubjectDTOFilter extends BaseFilter {
    String name;
}
