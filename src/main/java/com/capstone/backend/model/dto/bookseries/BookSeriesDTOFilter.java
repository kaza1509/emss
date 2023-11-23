package com.capstone.backend.model.dto.bookseries;

import com.capstone.backend.model.dto.BaseFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSeriesDTOFilter extends BaseFilter {
    String name = "";
    Boolean active;
}
