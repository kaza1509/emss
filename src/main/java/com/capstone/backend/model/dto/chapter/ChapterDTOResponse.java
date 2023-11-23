package com.capstone.backend.model.dto.chapter;

import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
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
public class ChapterDTOResponse {
    Long id;
    String name;
    Boolean active;
    LocalDateTime createdAt;
    @JsonIgnore
    BookVolumeDTOResponse bookVolumeDTOResponse;
    String createBy;
}
