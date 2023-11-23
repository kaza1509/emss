package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Lesson;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import com.capstone.backend.model.mapper.LessonMapper;
import com.capstone.backend.repository.ChapterRepository;
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
public class LessonCriteria {
    EntityManager em;
    ChapterRepository chapterRepository;
    MessageException messageException;
    UserRepository userRepository;

    public PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter,Long chapterId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from Lesson t where t.chapter.id = :chapterId");
        params.put("chapterId", chapterId);

        if(lessonDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + lessonDTOFilter.getName() + "%");
        }

        if(lessonDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active",  lessonDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = lessonDTOFilter.getPageIndex();
        Long pageSize = lessonDTOFilter.getPageSize();

        TypedQuery<Lesson> lessonTypedQuery = em.createQuery(sql.toString(), Lesson.class);

        // Set param to query
        params.forEach((k, v) -> {
            lessonTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        lessonTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        lessonTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Lesson> lessonList = lessonTypedQuery.getResultList();

        Long totalLesson = (Long) countQuery.getSingleResult();
        Long totalPage = totalLesson / pageSize;
        if (totalLesson % pageSize != 0) {
            totalPage++;
        }

        List<LessonDTOResponse> lessonDTOResponseList = new ArrayList<>( );
        for (Lesson entity: lessonList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            LessonDTOResponse lessonDTOResponse =
                    LessonMapper.toLessonDTOResponse(entity, user.getUsername());
            lessonDTOResponseList.add(lessonDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalLesson)
                .totalPage(totalPage)
                .data(lessonDTOResponseList)
                .build();
    }
}
