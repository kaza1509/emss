package com.capstone.backend.repository;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.ResourceTag;
import com.capstone.backend.entity.Tag;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.TableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ResourceTagRepository extends JpaRepository<ResourceTag, Long> {

    @Query("select rt.tag.name from ResourceTag rt where rt.resource.id = :resourceId and rt.active = true order by rt.tag.createdAt")
    public List<String> findAllTagName(Long resourceId);

    @Query("select distinct rt.resource from ResourceTag rt where rt.resource.resourceType = :resourceType and " +
            "rt.resource.id != :resourceId and rt.tag.name in (:tags) order by rt.resource.viewCount desc, rt.resource.createdAt desc")
    public List<Resource> findAllResourceByTagNameSameResourceType
            (ResourceType resourceType, Long resourceId, List<String> tags);

    @Query("select r from Resource r where r.resourceType = :resourceType and r.id != :resourceId and " +
            "r.lesson.id = :lessonId order by r.viewCount desc, r.createdAt desc")
    public List<Resource> findAllResourceByLessonIdSameResourceType(ResourceType resourceType, Long resourceId, Long lessonId);

    @Query("select rt from ResourceTag rt where rt.tag.id in (:tagIds) and  rt.active = true")
    public Set<ResourceTag> findResourceTagByTagId(List<Long> tagIds);

    @Query("SELECT rt FROM ResourceTag rt WHERE rt.tableType= :tableType AND rt.detailId = :id AND rt.tag.active = TRUE AND rt.active = TRUE")
    public List<ResourceTag> getAllResourceTagByTableTypeAndID(TableType tableType, long id);

    @Query("SELECT rt.tag FROM ResourceTag rt WHERE rt.tableType= :tableType AND rt.detailId = :id AND rt.tag.active = TRUE AND rt.active = TRUE")
    public List<Tag> getAllTagByTableTypeAndID(TableType tableType, long id);

    @Query("SELECT rt FROM ResourceTag rt WHERE rt.tableType=?1 AND rt.detailId=?2 AND rt.tag.id = ?3 AND rt.active=TRUE")
    public Optional<ResourceTag> findByTagIdAndByTableNameAndRowID(TableType tableType, long rowId, long tagId);

    @Query("select rt from ResourceTag rt where rt.resource.id = :resourceId and rt.tag.id = :tagId and rt.active = :active")
    public ResourceTag findByTagAndResource(Long tagId, Long resourceId, Boolean active);
}
