package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.mapper.BookSeriesMapper;
import com.capstone.backend.repository.ClassRepository;
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
public class BookSeriesCriteria {
    EntityManager em;
    ClassRepository classRepository;
    UserRepository userRepository;
    MessageException messageException;

    public PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter, Long classId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from BookSeries t where t.classObject.id = :classId ");
        params.put("classId", classId);

        if (bookSeriesDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + bookSeriesDTOFilter.getName() + "%");
        }

        if (bookSeriesDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", bookSeriesDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = bookSeriesDTOFilter.getPageIndex();
        Long pageSize = bookSeriesDTOFilter.getPageSize();

        TypedQuery<BookSeries> bookSeriesTypedQuery = em.createQuery(sql.toString(), BookSeries.class);

        // Set param to query
        params.forEach((k, v) -> {
            bookSeriesTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        bookSeriesTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        bookSeriesTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<BookSeries> bookSeriesList = bookSeriesTypedQuery.getResultList();

        Long totalBookSeries = (Long) countQuery.getSingleResult();
        Long totalPage = totalBookSeries / pageSize;
        if (totalBookSeries % pageSize != 0) {
            totalPage++;
        }
        List<BookSeriesDTOResponse> bookSeriesDTOResponseList = new ArrayList<>();
        for (BookSeries entity : bookSeriesList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            BookSeriesDTOResponse bookSeriesDTOResponse =
                    BookSeriesMapper.toBookseriesDTOResponse(entity, user.getUsername());
            bookSeriesDTOResponseList.add(bookSeriesDTOResponse);
        }
        return PagingDTOResponse.builder()
                .totalElement(totalBookSeries)
                .totalPage(totalPage)
                .data(bookSeriesDTOResponseList)
                .build();
    }
}
