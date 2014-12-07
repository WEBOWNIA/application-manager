/*
* Copyright 2014 Adam Barczewski webownia.net
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.webownia.applicationmgr.service;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.model.ApplicationFormAudit;
import net.webownia.applicationmgr.data.repository.ApplicationFormAuditRepository;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusException;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusRuntimeException;
import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@Service
public class ApplicationFormServiceImpl implements ApplicationFormService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ApplicationFormRepository repository;

    @Autowired
    private ApplicationFormAuditRepository auditRepository;

    @Override
    public void create(String name, String content) throws ApplicationFormChangingStatusRuntimeException {
        if ((content == null || content.isEmpty()) && (name == null || name.isEmpty())) {
            throw new ApplicationFormChangingStatusRuntimeException("Name and content are required.");
        } else if (name == null || name.isEmpty()) {
            throw new ApplicationFormChangingStatusRuntimeException("Name is required.");
        } else if (content == null || content.isEmpty()) {
            throw new ApplicationFormChangingStatusRuntimeException("Content is required.");
        }

        ApplicationForm applicationForm = new ApplicationForm(name, content, ApplicationStatus.CREATED, LocalDateTime.now());
        repository.save(applicationForm);
    }

    @Override
    public void delete(long id, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        changeStatusAndAudit(id, ApplicationStatus.DELETED, cause);
    }

    @Override
    public void verified(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        changeStatusAndAudit(id, ApplicationStatus.VERIFIED, null);
    }

    @Override
    public void reject(long id, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        changeStatusAndAudit(id, ApplicationStatus.REJECTED, cause);
    }

    @Override
    public void accept(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        changeStatusAndAudit(id, ApplicationStatus.ACCEPTED, null);
    }

    @Override
    public void publish(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        changeStatusAndAudit(id, ApplicationStatus.PUBLISHED, null);
    }

    @Override
    public Page<ApplicationForm> findByNameOrStatusIn(String name, List<String> collectionStatus, Integer pageNumber) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        if ((name == null || name.isEmpty()) && (collectionStatus == null || collectionStatus.isEmpty())) {
            return null;
        }

        try {
            EnumSet applicationStatuses = ApplicationStatus.enumSetForStatusCollection(collectionStatus);
            boolean forStatus = false;
            if (applicationStatuses != null && ApplicationStatus.allStatusCollection.containsAll(applicationStatuses)) {
                forStatus = true;
            }
            PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "lastModifiedDate", "createdDate");
            if (name != null && !name.isEmpty() && forStatus) {
                return repository.findByNameContainingAndStatusIn(name, applicationStatuses, request);
            } else if (name != null && !name.isEmpty()) {
                return repository.findByNameContaining(name, request);
            } else if (forStatus) {
                return repository.findByStatusIn(applicationStatuses, request);
            } return null;
        } catch (IllegalArgumentException e) {
            throw new ApplicationFormChangingStatusRuntimeException("Wrong status collections.", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationFormChangingStatusException("Unknown exception", e.getCause());
        }
    }

    @Override
    public Page<ApplicationForm> findAll(Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "lastModifiedDate", "createdDate");
        return repository.findAll(request);
    }

    @Override
    public Page<ApplicationFormAudit> findByApplicationFormId(long applicationFormId, Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "lastModifiedDate", "createdDate");
        return auditRepository.findByApplicationFormId(applicationFormId, request);
    }

    private void changeStatusAndAudit(long id, ApplicationStatus status, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException {
        ApplicationForm applicationForm = repository.findById(id);

        ApplicationFormAudit applicationFormAudit = new ApplicationFormAudit();
        applicationFormAudit.setApplicationFormId(id);
        applicationFormAudit.setName(applicationForm.getName());
        applicationFormAudit.setContent(applicationForm.getContent());
        applicationFormAudit.setCreatedDate(applicationForm.getCreatedDate());
        applicationFormAudit.setLastModifiedDate(applicationForm.getLastModifiedDate());
        applicationFormAudit.setVersion(applicationForm.getVersion());
        applicationFormAudit.setStatus(applicationForm.getStatus());
        applicationFormAudit.setCause(applicationForm.getCause());

        if (status.equals(ApplicationStatus.DELETED) && applicationForm.getStatus().equals(ApplicationStatus.CREATED)) {
            applicationForm.setStatus(status);
            doThrowWithOutCause(cause, applicationForm);
        } else if (status.equals(ApplicationStatus.VERIFIED) && applicationForm.getStatus().equals(ApplicationStatus.CREATED)) {
            applicationForm.setStatus(status);
        } else if (status.equals(ApplicationStatus.REJECTED) && ApplicationStatus.stagesForRejecting.contains(applicationForm.getStatus())) {
            applicationForm.setStatus(status);
            doThrowWithOutCause(cause, applicationForm);
        } else if (status.equals(ApplicationStatus.ACCEPTED) && applicationForm.getStatus().equals(ApplicationStatus.VERIFIED)) {
            applicationForm.setStatus(status);
        } else if (status.equals(ApplicationStatus.PUBLISHED) && applicationForm.getStatus().equals(ApplicationStatus.ACCEPTED)) {
            applicationForm.setStatus(status);
        } else {
            throw new ApplicationFormChangingStatusException("Can not changed status.");
        }

        // put on history
        auditRepository.save(applicationFormAudit);

        applicationForm.setLastModifiedDate(LocalDateTime.now());
        // version incremented
        repository.save(applicationForm);
    }

    private void doThrowWithOutCause(String cause, ApplicationForm applicationForm) throws ApplicationFormChangingStatusRuntimeException {
        if (cause != null && !cause.isEmpty()) {
            applicationForm.setCause(cause);
        } else {
            throw new ApplicationFormChangingStatusRuntimeException("Cause is required.");
        }
    }
}
