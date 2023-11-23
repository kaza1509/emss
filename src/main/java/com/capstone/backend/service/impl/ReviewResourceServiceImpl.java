package com.capstone.backend.service.impl;

import com.capstone.backend.entity.ReportResource;
import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserResourcePermission;
import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.EmailInfo;
import com.capstone.backend.model.dto.resource.PagingResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceReportDTOResponse;
import com.capstone.backend.model.dto.reviewResource.MaterialReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.MediaReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.ReviewResourceDTORequest;
import com.capstone.backend.model.mapper.ReportResourceMapper;
import com.capstone.backend.model.mapper.ResourceMapper;
import com.capstone.backend.repository.ReportResourceRepository;
import com.capstone.backend.repository.ResourceRepository;
import com.capstone.backend.repository.UserResourcePermissionRepository;
import com.capstone.backend.repository.criteria.ReviewResourceCriteria;
import com.capstone.backend.service.ReviewResourceService;
import com.capstone.backend.utils.EmailHandler;
import com.capstone.backend.utils.EmailHtml;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.capstone.backend.utils.Constants.API_VERSION;
import static com.capstone.backend.utils.Constants.HOST;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewResourceServiceImpl implements ReviewResourceService {
    ResourceRepository resourceRepository;
    ReportResourceRepository reportResourceRepository;
    UserResourcePermissionRepository userResourcePermissionRepository;
    EmailHandler emailHandler;
    MessageException messageException;
    UserHelper userHelper;
    EmailHtml emailHtml;
    ReviewResourceCriteria reviewResourceCriteria;

    @Override
    public ResourceDTOResponse reviewResource(ReviewResourceDTORequest request, boolean isAccepted) {
        //save
        Resource resource = resourceRepository.findById(request.getResourceId()).orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        ;
        User user = resource.getAuthor();
        //link to the resource detail
        String link = HOST + API_VERSION + "resource/detail/" + resource.getId();

        if (isAccepted) {
            resource.setApproveType(ApproveType.ACCEPTED);
            UserResourcePermission userResourcePermission = userResourcePermissionRepository.findUserResourcePermissionByResource(resource).orElseThrow();
            userResourcePermission.setPermission("CDRV");
            userResourcePermissionRepository.save(userResourcePermission);

            //send approval mail
            String body = "Chúc mừng! Tài liệu của bạn đã được thông qua. Ghi chú: " + request.getMessage();
            String subject = "[EMSS] Tài liệu của bạn đã được kiểm duyệt.";
            String content = emailHtml.buildReviewEmail(user.getUsername(), link, subject, body);
            EmailInfo emailInfo = EmailInfo.builder()
                    .to(user.getEmail())
                    .content(content)
                    .subject(subject)
                    .build();
            emailHandler.send(emailInfo);
        } else {
            resource.setApproveType(ApproveType.REJECT);

            //send reject mail
            String body = "Rất tiếc! Tài liệu của bạn đã bị từ chối. Lí do: " + request.getMessage();
            String subject = "[EMSS] Tài liệu của bạn đã bị từ chối.";
            String content = emailHtml.buildReviewEmail(user.getUsername(), link, subject, body);
            EmailInfo emailInfo = EmailInfo.builder()
                    .to(user.getEmail())
                    .content(content)
                    .subject(subject)
                    .build();
            emailHandler.send(emailInfo);
        }
        resource.setModerator(userHelper.getUserLogin());
        resourceRepository.save(resource);
        return ResourceMapper.toResourceDTOResponse(resource);
    }

    @Override
    public PagingResourceDTOResponse searchMaterial(MaterialReviewDTOFilter materialReviewDTOFilter, boolean isReport) {
        return reviewResourceCriteria.searchReviewMaterial(materialReviewDTOFilter, isReport);
    }

    @Override
    public Object searchMedia(MediaReviewDTOFilter mediaReviewDTOFilter, boolean isReport) {
        return reviewResourceCriteria.searchReviewMedia(mediaReviewDTOFilter, isReport);
    }

    @Override
    public ResourceReportDTOResponse getResourceReportById(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        return ReportResourceMapper.toResourceReportDTOResponse(resource);
    }

    @Override
    public ResourceReportDTOResponse reportResource(ReviewResourceDTORequest request) {
        List<ReportResource> reportResourceList = reportResourceRepository.findByResourceId(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));
        //link to the resource detail
        String link = HOST + API_VERSION + "resource/detail/" + resource.getId();
        User user = resource.getAuthor();
        for (ReportResource reportResource : reportResourceList) {
            reportResource.setActive(false);
        }
        reportResourceRepository.saveAll(reportResourceList);

        resource.setApproveType(ApproveType.UNACCEPTED);
        resourceRepository.save(resource);

        UserResourcePermission userResourcePermission = userResourcePermissionRepository.findUserResourcePermissionByResource(resource).orElseThrow();
        userResourcePermission.setPermission("CDRUV");
        userResourcePermissionRepository.save(userResourcePermission);

        //send report mail
        String body = "Rất tiếc! Tài liệu của bạn đã bị báo cáo. Lí do: " + request.getMessage();
        String subject = "[EMSS] Tài liệu của bạn đã bị báo cáo.";
        String content = emailHtml.buildReviewEmail(user.getUsername(), link, subject, body);
        EmailInfo emailInfo = EmailInfo.builder()
                .to(user.getEmail())
                .content(content)
                .subject(subject)
                .build();
        emailHandler.send(emailInfo);
        return ReportResourceMapper.toResourceReportDTOResponse(resource);
    }

}
