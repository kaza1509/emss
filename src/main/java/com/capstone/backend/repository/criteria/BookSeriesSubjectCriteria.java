package com.capstone.backend.repository.criteria;


import com.capstone.backend.entity.BookSeriesSubject;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOFilter;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOResponse;
import com.capstone.backend.model.mapper.BookSeriesSubjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookSeriesSubjectCriteria {
    EntityManager em;

    public PagingDTOResponse searchBookSeriesSubject(BookSeriesSubjectDTOFilter bookSeriesSubjectDTOFilter, Long bookSeriesId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from BookSeriesSubject t where t.bookSeries.id = :bookSeriesId");
        params.put("bookSeriesId", bookSeriesId);

        if(bookSeriesSubjectDTOFilter.getName() != null) {
            sql.append(" and t.subject.name like :name");
            params.put("name", "%" + bookSeriesSubjectDTOFilter.getName() + "%");
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = bookSeriesSubjectDTOFilter.getPageIndex();
        Long pageSize = bookSeriesSubjectDTOFilter.getPageSize();

        TypedQuery<BookSeriesSubject> bookSeriesSubjectTypedQuery = em.createQuery(sql.toString(), BookSeriesSubject.class);

        // Set param to query
        params.forEach((k, v) -> {
            bookSeriesSubjectTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        bookSeriesSubjectTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        bookSeriesSubjectTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<BookSeriesSubject> bookSeriesSubjectList = bookSeriesSubjectTypedQuery.getResultList();

        Long totalBookSeriesSubject = (Long) countQuery.getSingleResult();
        Long totalPage = totalBookSeriesSubject / pageSize;
        if (totalBookSeriesSubject % pageSize != 0) {
            totalPage++;
        }

        List<BookSeriesSubjectDTOResponse> bookSeriesDTOResponseList = bookSeriesSubjectList.stream()
                .map(BookSeriesSubjectMapper::toBookSeriesSubjectDTOResponse)
                .toList();

        return PagingDTOResponse.builder()
                .totalElement(totalBookSeriesSubject)
                .totalPage(totalPage)
                .data(bookSeriesDTOResponseList)
                .build();
    }
}

