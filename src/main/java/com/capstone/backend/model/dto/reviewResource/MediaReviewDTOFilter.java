package com.capstone.backend.model.dto.reviewResource;

import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaReviewDTOFilter {
    String name;
    Long subjectId;
    ApproveType approveType;
    TabResourceType tabResourceType;
    VisualType visualType;
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
    Long pageSize = Constants.DEFAULT_PAGE_SIZE;
}
