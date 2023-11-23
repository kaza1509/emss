package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.model.dto.resource.PagingResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceReportDTOResponse;
import com.capstone.backend.model.dto.reviewResource.MaterialReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.MediaReviewDTOFilter;
import com.capstone.backend.model.mapper.ReportResourceMapper;
import com.capstone.backend.model.mapper.ResourceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResourceCriteria {
    final EntityManager em;

    public PagingResourceDTOResponse searchReviewMaterial(MaterialReviewDTOFilter materialReviewDTOFilter, boolean isReport) {
        em.createQuery("select re from Resource re left join re.lesson le left join"
                + " le.chapter cha left join cha.bookVolume bv left join bv.bookSeriesSubject bss left join"
                + " bss.subject su left join bss.bookSeries bs left join bs.classObject c where 1 = 1");
        StringBuilder sql = new StringBuilder("select re from Resource re left join re.lesson le left join"
                + " le.chapter cha left join cha.bookVolume bv left join bv.bookSeriesSubject bss left join"
                + " bss.subject su left join bss.bookSeries bs left join bs.classObject c where 1 = 1");
        if (isReport) {
            String newSql = Pattern.compile("where 1 = 1").matcher(sql).replaceAll(" join ReportResource rr on re.id = rr.resource.id where 1 = 1 and rr.active = true");
            sql = new StringBuilder(newSql);
        }
        Map<String, Object> params = new HashMap<>();
        sql.append(" and re.visualType NOT IN (:excludeVisualType)");
        params.put("excludeVisualType", VisualType.PRIVATE);

        if (materialReviewDTOFilter.getSubjectId() != null) {
            sql.append(" and su.id = :subjectId");
            params.put("subjectId", materialReviewDTOFilter.getSubjectId());
        }
        if (materialReviewDTOFilter.getClassId() != null) {
            sql.append(" and c.id = :classId");
            params.put("classId", materialReviewDTOFilter.getClassId());
        }
        if (materialReviewDTOFilter.getBookSeriesId() != null) {
            sql.append(" and bs.id = :bookSeriesId");
            params.put("bookSeriesId", materialReviewDTOFilter.getBookSeriesId());
        }
        if (materialReviewDTOFilter.getBookVolumeId() != null) {
            sql.append(" and bv.id = :bookVolumeId");
            params.put("bookVolumeId", materialReviewDTOFilter.getBookVolumeId());
        }
        if (materialReviewDTOFilter.getChapterId() != null) {
            sql.append(" and cha.id = :chapterId");
            params.put("chapterId", materialReviewDTOFilter.getChapterId());
        }
        if (materialReviewDTOFilter.getLessonId() != null) {
            sql.append(" and le.id = :lessonId");
            params.put("lessonId", materialReviewDTOFilter.getLessonId());
        }
        if (materialReviewDTOFilter.getTabResourceType() != null) {
            sql.append(" and re.tabResourceType = :tabResourceType");
            params.put("tabResourceType", materialReviewDTOFilter.getTabResourceType());
        }
        if (materialReviewDTOFilter.getVisualType() != null) {
            sql.append(" and re.visualType = :visualType");
            params.put("visualType", materialReviewDTOFilter.getVisualType());
        }
        if (materialReviewDTOFilter.getApproveType() != null) {
            sql.append(" and re.approveType = :approveType");
            params.put("approveType", materialReviewDTOFilter.getApproveType());
        }

        sql.append(" and re.name like :name ");
        params.put("name", "%" + materialReviewDTOFilter.getName() + "%");
        sql.append(" order by re.approveType ASC, re.createdAt ");

        Long pageIndex = materialReviewDTOFilter.getPageIndex();
        Long pageSize = materialReviewDTOFilter.getPageSize();
        return getPagingResourceDTOResponse(sql.toString(), pageIndex, pageSize, params, isReport);
    }

    public Object searchReviewMedia(MediaReviewDTOFilter mediaReviewDTOFilter, boolean isReport) {
        em.createQuery("select re from Resource re join Subject su on re.subject.id = su.id where 1=1 and re.visualType NOT IN (:excludeVisualType) and re.lesson.id = null and re.resourceType IN (:resourceTypes)");
        StringBuilder sql = new StringBuilder("select re from Resource re join Subject su on re.subject.id = su.id where 1=1");
        if (isReport) {
            String newSql = Pattern.compile("where 1 = 1").matcher(sql).replaceAll(" join ReportResource rr on re.id = rr.resource.id where 1 = 1 and rr.active = true");
            sql = new StringBuilder(newSql);
        }
        Map<String, Object> params = new HashMap<>();
        List<ResourceType> resourceTypes = Arrays.asList(ResourceType.PNG, ResourceType.JPG, ResourceType.JPEG, ResourceType.MP4);
        sql.append(" and re.visualType NOT IN (:excludeVisualType) and re.lesson.id = null and re.resourceType IN (:resourceTypes)");
        params.put("excludeVisualType", VisualType.PRIVATE);
        params.put("resourceTypes", resourceTypes);

        if (mediaReviewDTOFilter.getTabResourceType() != null) {
            sql.append(" and re.tabResourceType = :tabResourceType");
            params.put("tabResourceType", mediaReviewDTOFilter.getTabResourceType());
        }
        if (mediaReviewDTOFilter.getVisualType() != null) {
            sql.append(" and re.visualType = :visualType");
            params.put("visualType", mediaReviewDTOFilter.getVisualType());
        }
        if (mediaReviewDTOFilter.getApproveType() != null) {
            sql.append(" and re.approveType = :approveType");
            params.put("approveType", mediaReviewDTOFilter.getApproveType());
        }
        sql.append(" and re.name like :name ");
        params.put("name", "%" + mediaReviewDTOFilter.getName() + "%");
        sql.append(" order by re.approveType ASC, re.createdAt ");

        Long pageIndex = mediaReviewDTOFilter.getPageIndex();
        Long pageSize = mediaReviewDTOFilter.getPageSize();
        return getPagingResourceDTOResponse(sql.toString(), pageIndex, pageSize, params, isReport);
    }

    private PagingResourceDTOResponse getPagingResourceDTOResponse(String sql, Long pageIndex, Long pageSize, Map<String, Object> params, boolean isReport) {
        Query countQuery = em.createQuery(sql.replace("select re", "select count(re.id)"));
        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql, Resource.class);
        params.forEach((k, v) -> {
            resourceTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        resourceTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Resource> resourceList = resourceTypedQuery.getResultList();

        Long totalResource = (Long) countQuery.getSingleResult();
        long totalPage = totalResource / pageSize;
        if (totalResource % pageSize != 0) {
            totalPage++;
        }

        if (isReport) {
            List<ResourceReportDTOResponse> resourceReportDTOResponses
                    = resourceList.stream().map(ReportResourceMapper::toResourceReportDTOResponse).toList();
            return PagingResourceDTOResponse.builder().totalElement(totalResource).totalPage(totalPage).data(resourceReportDTOResponses).build();
        }
        List<ResourceDTOResponse> resourceDTOResponseList = resourceList.stream().map(ResourceMapper::toResourceDTOResponse).toList();
        return PagingResourceDTOResponse.builder().totalElement(totalResource).totalPage(totalPage).data(resourceDTOResponseList).build();
    }
}
