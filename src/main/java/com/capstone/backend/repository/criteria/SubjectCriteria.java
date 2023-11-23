package com.capstone.backend.repository.criteria;


import com.capstone.backend.entity.Subject;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.capstone.backend.model.mapper.SubjectMapper;
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
public class SubjectCriteria {
    EntityManager em;
    MessageException messageException;
    UserRepository userRepository;
    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Subject t ");
        Map<String, Object> params = new HashMap<>();

        if(subjectDTOFilter.getName() != null) {
            sql.append(" where t.name like :name ");
            params.put("name", "%" + subjectDTOFilter.getName() + "%");
        }

        if(subjectDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active",  subjectDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = subjectDTOFilter.getPageIndex();
        Long pageSize = subjectDTOFilter.getPageSize();

        TypedQuery<Subject> subjectTypedQuery = em.createQuery(sql.toString(), Subject.class);

        // Set param to query
        params.forEach((k, v) -> {
            subjectTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        subjectTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        subjectTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Subject> subjectList = subjectTypedQuery.getResultList();

        Long totalSubject = (Long) countQuery.getSingleResult();
        Long totalPage = totalSubject / pageSize;
        if (totalSubject % pageSize != 0) {
            totalPage++;
        }
        List<SubjectDTOResponse> subjectDTOResponseList = new ArrayList<>();
        for (Subject entity: subjectList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            SubjectDTOResponse subjectDTOResponse =
                    SubjectMapper.toSubjectDTOResponse(entity, user.getUsername());
            subjectDTOResponseList.add(subjectDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalSubject)
                .totalPage(totalPage)
                .data(subjectDTOResponseList)
                .build();
    }
}
