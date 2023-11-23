package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Subject;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTORequest;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.capstone.backend.model.mapper.SubjectMapper;
import com.capstone.backend.repository.BookSeriesSubjectRepository;
import com.capstone.backend.repository.SubjectRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.criteria.SubjectCriteria;
import com.capstone.backend.service.SubjectService;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    MessageException messageException;
    UserRepository userRepository;
    BookSeriesSubjectRepository bookSeriesSubjectRepository;
    SubjectCriteria subjectCriteria;
    UserHelper userHelper;

    @Override
    public SubjectDTOResponse createSubject(SubjectDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Subject> subjectOptional = subjectRepository.findByName(request.getName(), 0L);
        if (subjectOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate subject name");
        }

        // add entity
        Subject subject = Subject.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .build();
        User user = userRepository.findById(subject.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        subject = subjectRepository.save(subject);
        return SubjectMapper.toSubjectDTOResponse(subject, user.getUsername());
    }

    @Override
    public SubjectDTOResponse updateSubject(Long id, SubjectDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Subject> subjectOptional = subjectRepository.findByName(request.getName(), id);
        if (subjectOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate subject name");
        }

        // find subject id want update
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        User user = userRepository.findById(subject.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        // update subject
        subject.setName(request.getName());
        subject.setUserId(userLogged.getId());

        subject = subjectRepository.save(subject);
        return SubjectMapper.toSubjectDTOResponse(subject, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        // find Subject id
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        if (isCanChangeStatus(subject)) {
            subject.setActive(!subject.getActive());
            subjectRepository.save(subject);
        } else {
            // throw exception warning
            throw ApiException.conflictResourceException("Can not change status Subject because BookVolume already exists");
        }
    }

    @Override
    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter) {
        return subjectCriteria.searchSubject(subjectDTOFilter);
    }


    @Override
    public SubjectDTOResponse viewSubjectById(Long id) {
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));

        User user = userRepository.findById(subject.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return SubjectMapper.toSubjectDTOResponse(subject, user.getUsername());
    }

    @Override
    public List<SubjectDTOResponse> getListSubjects() {
        return subjectRepository.findAllByActiveTrue().stream()
                .map(SubjectMapper::toSubjectDTOResponse)
                .toList();
    }

    @Override
    public List<SubjectDTOResponse> getListSubjectsByBookSeries(Long bookSeriesId) {
        List<Subject> subjects = new ArrayList<>();
        if(bookSeriesId == null) {
            subjects = subjectRepository.findAllByActiveTrue();
        }
        else {
            subjects = subjectRepository.findSubjectByBookSeries(bookSeriesId);
        }
        return subjects.stream()
                .map(SubjectMapper::toSubjectDTOResponse)
                .toList();
    }

    // Check exist book volume in subject
    boolean isCanChangeStatus(Subject subject) {
        return subject.getBookSeriesSubjects().isEmpty();
    }
}
