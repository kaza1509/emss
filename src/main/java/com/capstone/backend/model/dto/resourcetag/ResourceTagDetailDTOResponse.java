package com.capstone.backend.model.dto.resourcetag;

import com.capstone.backend.entity.type.TableType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ResourceTagDetailDTOResponse {
    Long detailId;
    TableType tableType;
    Long resourceId;
    Boolean active;
    LocalDateTime createdAt;
    List<Long> tagList;
}
