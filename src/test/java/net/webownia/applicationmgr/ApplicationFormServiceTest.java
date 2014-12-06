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
package net.webownia.applicationmgr;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.model.ApplicationFormAudit;
import net.webownia.applicationmgr.data.repository.ApplicationFormAuditRepository;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusException;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusRuntimeException;
import net.webownia.applicationmgr.service.ApplicationFormService;
import net.webownia.applicationmgr.service.ApplicationFormServiceImpl;
import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationFormServiceTest {

    private static final int PAGE_NUMBER = 3;
    private static final PageRequest REQUEST = new PageRequest(PAGE_NUMBER - 1, 10, Sort.Direction.ASC, "lastModifiedDate", "createdDate");
    private static final Collection<String> STATUS_COLLECTIONS = ApplicationStatus.statusCollectionForEnumSet(ApplicationStatus.allStatusCollection);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ApplicationFormRepository applicationFormRepository;

    @Mock
    private ApplicationFormAuditRepository auditRepository;

    @Mock
    private Page<ApplicationForm> pageApplicationForms;

    @Mock
    private Page<ApplicationFormAudit> pageApplicationAuditForms;

    @InjectMocks
    private ApplicationFormService testee = new ApplicationFormServiceImpl();

    @Test
    public void shouldCreate() throws Exception {
        //WHEN
        testee.create("name", "content");

        //THEN
        verify(applicationFormRepository, times(1)).save(Matchers.any(ApplicationForm.class));
    }

    @Test
    public void shouldThrowWhenCreateTestCases() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent(null, null);
        shouldThrowRuntimeWithOutNameOrContent("", "");
        shouldThrowRuntimeWithOutNameOrContent(null, "");
        shouldThrowRuntimeWithOutNameOrContent("", null);
        shouldThrowRuntimeWithOutNameOrContent("xx", null);
        shouldThrowRuntimeWithOutNameOrContent(null, "xx");
        shouldThrowRuntimeWithOutNameOrContent("", "xx");
        shouldThrowRuntimeWithOutNameOrContent("xx", "");
    }

    @Test
    public void shouldVerify() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.CREATED, Action.VERIFY);
    }

    @Test
    public void shouldThrowWhenVerifyTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.VERIFIED, Action.VERIFY);
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.VERIFY);
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.VERIFY);
        shouldThrowForStatusAndAction(ApplicationStatus.ACCEPTED, Action.VERIFY);
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.VERIFY);
    }

    @Test
    public void shouldDelete() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.CREATED, Action.DELETE);
    }

    @Test
    public void shouldThrowWhenDeleteTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.VERIFIED, Action.DELETE);
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.DELETE);
        shouldThrowForStatusAndAction(ApplicationStatus.ACCEPTED, Action.DELETE);
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.DELETE);
        shouldThrowRuntimeWithOutCause(ApplicationStatus.VERIFIED, Action.DELETE);
    }

    @Test
    public void shouldReject() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.VERIFIED, Action.REJECT);
    }

    @Test
    public void shouldThrowWhenRejectTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.CREATED, Action.REJECT);
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.REJECT);
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.REJECT);
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.REJECT);
        shouldThrowRuntimeWithOutCause(ApplicationStatus.CREATED, Action.REJECT);
    }

    @Test
    public void shouldAccept() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.VERIFIED, Action.ACCEPT);
    }

    @Test
    public void shouldThrowWhenAcceptTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.ACCEPTED, Action.ACCEPT);
        shouldThrowForStatusAndAction(ApplicationStatus.CREATED, Action.ACCEPT);
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.ACCEPT);
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.ACCEPT);
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.ACCEPT);
    }

    @Test
    public void shouldPublish() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.ACCEPTED, Action.PUBLISH);
    }

    @Test
    public void shouldThrowWhenPublishTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.PUBLISH);
        shouldThrowForStatusAndAction(ApplicationStatus.VERIFIED, Action.PUBLISH);
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.PUBLISH);
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.PUBLISH);
        shouldThrowForStatusAndAction(ApplicationStatus.CREATED, Action.PUBLISH);
    }

    @Test
    public void shouldFindApplicationsPageable() {
        //GIVEN
        when(applicationFormRepository.findAll(REQUEST)).thenReturn(pageApplicationForms);
        when(pageApplicationForms.getTotalElements()).thenReturn(10l);

        //WHEN
        long totalElements = testee.findAll(PAGE_NUMBER).getTotalElements();

        //THEN
        Assert.assertTrue(totalElements == 10L);
    }

    @Test
    public void shouldFindApplicationTestCases() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter(null, null);
        shouldFindApplicationForFilter("", null);
        shouldFindApplicationForFilter("xxx", null);
        shouldFindApplicationForFilter(null, new ArrayList<>(0));
        shouldFindApplicationForFilter("", new ArrayList<>(0));
        shouldFindApplicationForFilter("xxx", new ArrayList<>(0));
        shouldFindApplicationForFilter(null, STATUS_COLLECTIONS);
        shouldFindApplicationForFilter("", STATUS_COLLECTIONS);
        shouldFindApplicationForFilter("xxx", STATUS_COLLECTIONS);
    }

    @Test
    public void shouldThrowRuntimeWhenFilteringPageWithWrongStatusCollections() throws ApplicationFormChangingStatusException {
        //GIVEN
        Collection<String> wrongStatusCollection = new ArrayList<>(1);
        wrongStatusCollection.add("WRONG_STATUS");

        thrown.expect(ApplicationFormChangingStatusRuntimeException.class);
        thrown.expectMessage("Wrong status collections.");

        //WHEN
        testee.findByNameOrStatusIn("xxx", wrongStatusCollection, PAGE_NUMBER);
    }

    @Test
    public void shouldFindAuditApplicationsPageable() {
        //GIVEN
        when(auditRepository.findByApplicationFormId(1l, REQUEST)).thenReturn(pageApplicationAuditForms);
        when(pageApplicationAuditForms.getTotalElements()).thenReturn(10l);

        //WHEN
        long totalElements = testee.findByApplicationFormId(1L, PAGE_NUMBER).getTotalElements();

        //THEN
        Assert.assertTrue(totalElements == 10L);
    }

    /**
     * dynamic method for assert find applications for filter
     *
     * @param name             - name application form
     * @param statusCollection - Collection<String>
     */
    private void shouldFindApplicationForFilter(String name, Collection<String> statusCollection) throws ApplicationFormChangingStatusException {
        //GIVEN
        if (name == null || name.isEmpty() || statusCollection == null || statusCollection.isEmpty()) {
            when(applicationFormRepository.findAll(REQUEST)).thenReturn(pageApplicationForms);
        } else {
            when(applicationFormRepository.findByNameOrStatusIn(name, statusCollection, REQUEST)).thenReturn(pageApplicationForms);
        }
        when(pageApplicationForms.getTotalElements()).thenReturn(10l);

        //WHEN
        long totalElements = testee.findByNameOrStatusIn(name, statusCollection, PAGE_NUMBER).getTotalElements();

        //THEN
        Assert.assertTrue(totalElements == 10L);
    }


    /**
     * Dynamic method for success in run service action for changing status
     *
     * @param oldStatus - status before action
     * @param action    - name for service action
     * @throws Exception
     */
    private void shouldChangedForStatusAndAction(ApplicationStatus oldStatus, Action action) throws Exception {
        //GIVEN
        when(applicationFormRepository.findById(1)).thenReturn(getApplicationForm(oldStatus));

        //WHEN
        switch (action) {
            case REJECT:
                testee.reject(1, "Cause message...");
                break;
            case DELETE:
                testee.delete(1, "Cause message...");
                break;
            case VERIFY:
                testee.verified(1);
                break;
            case ACCEPT:
                testee.accept(1);
                break;
            case PUBLISH:
                testee.publish(1);
                break;
            default:
                break;
        }

        //THEN
        verify(applicationFormRepository, times(1)).findById(1);
        verify(applicationFormRepository, times(1)).save(Matchers.any(ApplicationForm.class));
        verify(auditRepository, times(1)).save(Matchers.any(ApplicationFormAudit.class));
    }

    /**
     * Expected ApplicationFormChangingStatusException
     *
     * @param oldStatus - status before action
     * @param action    - name for service action
     * @throws Exception
     */
    private void shouldThrowForStatusAndAction(ApplicationStatus oldStatus, Action action) throws Exception {
        //GIVEN
        when(applicationFormRepository.findById(1)).thenReturn(getApplicationForm(oldStatus));

        thrown.expect(ApplicationFormChangingStatusException.class);
        thrown.expectMessage("Can not changed status.");

        //WHEN
        switch (action) {
            case REJECT:
                testee.reject(1, "Cause message...");
                break;
            case DELETE:
                testee.delete(1, "Cause message...");
                break;
            case VERIFY:
                testee.verified(1);
                break;
            case ACCEPT:
                testee.accept(1);
                break;
            case PUBLISH:
                testee.publish(1);
                break;
            default:
                break;
        }
    }

    /**
     * Expected ApplicationFormChangingStatusRuntimeException for create service action
     *
     * @param name    - name application form
     * @param content - content application form
     * @throws ApplicationFormChangingStatusRuntimeException - java.lang.RuntimeException
     */
    private void shouldThrowRuntimeWithOutNameOrContent(String name, String content) throws ApplicationFormChangingStatusRuntimeException {
        thrown.expect(ApplicationFormChangingStatusRuntimeException.class);
        if ((content == null || content.isEmpty()) && (name == null || name.isEmpty())) {
            thrown.expectMessage("Name and content are required.");
        } else if (name == null || name.isEmpty()) {
            thrown.expectMessage("Name is required.");
        } else if (content == null || content.isEmpty()) {
            thrown.expectMessage("Content is required.");
        }

        testee.create(name, content);
    }

    /**
     * Expected ApplicationFormChangingStatusRuntimeException for delete or reject service action
     *
     * @param oldStatus - status before action
     * @param action    - name for service action
     * @throws Exception - java.lang.RuntimeException
     */
    private void shouldThrowRuntimeWithOutCause(ApplicationStatus oldStatus, Action action) throws Exception {
        //GIVEN
        when(applicationFormRepository.findById(1)).thenReturn(getApplicationForm(oldStatus));

        thrown.expect(ApplicationFormChangingStatusRuntimeException.class);
        thrown.expectMessage("Cause is required.");

        //WHEN
        switch (action) {
            case REJECT:
                testee.reject(1, null);
                break;
            case DELETE:
                testee.delete(1, null);
                break;
            default:
                break;
        }
    }

    private ApplicationForm getApplicationForm(ApplicationStatus oldStatus) {
        return new ApplicationForm("note", "contentMessage", oldStatus, LocalDateTime.now());
    }

    /**
     * Representation service actions
     */
    private enum Action {
        DELETE,
        VERIFY,
        REJECT,
        ACCEPT,
        PUBLISH
    }
}
