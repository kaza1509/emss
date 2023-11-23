package com.capstone.backend.model.dto.resourcetag;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceTagDTOUpdate {
    Long detailId;
    String tableType;
    List<Long> tagList;
}
