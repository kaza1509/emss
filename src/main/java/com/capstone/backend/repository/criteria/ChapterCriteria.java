package com.capstone.backend.repository.criteria;


import com.capstone.backend.entity.Chapter;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.capstone.backend.model.mapper.ChapterMapper;
import com.capstone.backend.repository.BookVolumeRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterCriteria {
    EntityManager em;
    BookVolumeRepository bookVolumeRepository;
    MessageException messageException;
    UserRepository userRepository;

    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter, Long bookVolumeId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from Chapter t where t.bookVolume.id = :bookVolumeId ");
        params.put("bookVolumeId", bookVolumeId);

        if (chapterDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + chapterDTOFilter.getName() + "%");
        }

        if(chapterDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", chapterDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = chapterDTOFilter.getPageIndex();
        Long pageSize = chapterDTOFilter.getPageSize();

        TypedQuery<Chapter> chapterTypedQuery = em.createQuery(sql.toString(), Chapter.class);

        // Set param to query
        params.forEach((k, v) -> {
            chapterTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        chapterTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        chapterTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Chapter> chapterList = chapterTypedQuery.getResultList();

        Long totalChapter = (Long) countQuery.getSingleResult();
        Long totalPage = totalChapter / pageSize;
        if (totalChapter % pageSize != 0) {
            totalPage++;
        }

        List<ChapterDTOResponse> chapterDTOResponseList = new ArrayList<>();
        for (Chapter entity : chapterList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            ChapterDTOResponse chapterDTOResponse =
                    ChapterMapper.toChapterDTOResponse(entity, user.getUsername());
            chapterDTOResponseList.add(chapterDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalChapter)
                .totalPage(totalPage)
                .data(chapterDTOResponseList)
                .build();
    }
}
