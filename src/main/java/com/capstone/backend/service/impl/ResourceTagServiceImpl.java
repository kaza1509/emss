package com.capstone.backend.service.impl;

import com.capstone.backend.entity.ResourceTag;
import com.capstone.backend.entity.Tag;
import com.capstone.backend.entity.type.TableType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOResponse;
import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOUpdate;
import com.capstone.backend.model.dto.resourcetag.ResourceTagDetailDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOResponse;
import com.capstone.backend.model.mapper.ResourceTagMapper;
import com.capstone.backend.repository.ResourceRepository;
import com.capstone.backend.repository.ResourceTagRepository;
import com.capstone.backend.repository.TagRepository;
import com.capstone.backend.service.ResourceTagService;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceTagServiceImpl implements ResourceTagService {
    ResourceTagRepository resourceTagRepository;
    MessageException messageException;
    TagRepository tagRepository;
    ResourceRepository resourceRepository;

    public List<ResourceTagDTOResponse> getAllResourceTagByTableTypeAndID(String tableType, long detailId) {
        List<ResourceTag> resourceTagList = resourceTagRepository.getAllResourceTagByTableTypeAndID(TableType.valueOf(tableType), detailId);
        return resourceTagList.stream()
                .map(ResourceTagMapper::toResourceTagDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceTagDTOResponse applyTagToResource(TagDTOResponse tagDTOResponse, String tableType, long detailId) {
        // Check if this tag name has been applied before
//        if (resourceTagRepository.findByTagIdAndByTableNameAndRowID(TableType.valueOf(tableType), detailId, tagDTOResponse.getId()).isPresent()) {
//            throw ApiException.badRequestException(messageException.MSG_TAG_APPLIED);
//        }
//        ResourceTag resourceTag = ResourceTag.builder()
//                .detailId(detailId)
//                .tableType(TableType.valueOf(tableType))
//                .createdAt(LocalDateTime.now())
//                .active(true)
//                .tag(Tag.builder()
//                        .id(tagDTOResponse.getId())
//                        .name(tagDTOResponse.getName())
//                        .build())
//                .build();
//        resourceTagRepository.save(resourceTag);
        return null;
    }

    @Override
    public ResourceTagDTOResponse disableTagFromResource(long id) {
        ResourceTag resourceTag = resourceTagRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        resourceTag.setActive(false);
        resourceTagRepository.save(resourceTag);
        return ResourceTagMapper.toResourceTagDTOResponse(resourceTag);
    }

    @Override
    public ResourceTagDetailDTOResponse updateResourceTag(ResourceTagDTOUpdate request) {
        List<Tag> tagList = resourceTagRepository.getAllTagByTableTypeAndID(TableType.valueOf(request.getTableType()), request.getDetailId());
        List<Tag> listAdded = request.getTagList().stream()
                .map(tag ->
                        tagRepository.
                                findByIdAndActiveTrue(tag)
                                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND))
                )
                .filter(tag -> !tagList.contains(tag))
                .toList();
        List<Tag> listDeleted = tagList.stream()
                .filter(tag -> !request.getTagList().contains(tag.getId()))
                .toList();

        //xóa listDelete
        deleteListTag(listDeleted, request);
        //thêm listAdded
        addListTag(listAdded, request);
        return null;
    }

    private void addListTag(List<Tag> listAdded, ResourceTagDTOUpdate request) {
        listAdded.forEach(tag -> {
            Tag aTag = tagRepository
                    .findByIdAndActiveTrue(tag.getId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
            ResourceTag resourceTag = ResourceTagMapper.toResourceTag(aTag,request);
            resourceTagRepository.save(resourceTag);
        });
    }

    private void deleteListTag(List<Tag> listDeleted, ResourceTagDTOUpdate request) {
        listDeleted.forEach(tag -> {
            var resourceTag = resourceTagRepository
                    .findByTagIdAndByTableNameAndRowID(TableType.valueOf(request.getTableType()), request.getDetailId(), tag.getId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TAG_NOT_FOUND));
            resourceTag.setActive(false);
            resourceTagRepository.save(resourceTag);
        });
    }

}
