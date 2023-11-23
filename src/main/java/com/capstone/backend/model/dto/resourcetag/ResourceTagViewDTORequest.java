package com.capstone.backend.model.dto.resourcetag;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceTagViewDTORequest {
    Long detailId;
    String tableType;
}
