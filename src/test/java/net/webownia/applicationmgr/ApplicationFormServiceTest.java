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
import java.util.EnumSet;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationFormServiceTest {

    private static final int PAGE_NUMBER = 3;
    private static final PageRequest REQUEST = new PageRequest(PAGE_NUMBER - 1, 10, Sort.Direction.ASC, "lastModifiedDate", "createdDate");
    private static final List<String> STATUS_COLLECTIONS = ApplicationStatus.statusCollectionForEnumSet(ApplicationStatus.allStatusCollection);
    private boolean updateTest = false;

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
    }

    @Test
    public void shouldThrowWhenCreateTestCases1() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent("", "");
    }

    @Test
    public void shouldThrowWhenCreateTestCases2() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent(null, "");
    }

    @Test
    public void shouldThrowWhenCreateTestCases3() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent("", null);
    }

    @Test
    public void shouldThrowWhenCreateTestCases4() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent("xx", null);
    }

    @Test
    public void shouldThrowWhenCreateTestCases5() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent(null, "xx");
    }

    @Test
    public void shouldThrowWhenCreateTestCases6() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent("", "xx");
    }

    @Test
    public void shouldThrowWhenCreateTestCases7() throws Exception {
        shouldThrowRuntimeWithOutNameOrContent("xx", "");
    }

    @Test
    public void shouldVerify() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.CREATED, Action.VERIFY);
    }

    @Test
    public void shouldThrowWhenVerifyTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.VERIFIED, Action.VERIFY);
    }

    @Test
    public void shouldThrowWhenVerifyTestCases1() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.VERIFY);
    }

    @Test
    public void shouldThrowWhenVerifyTestCases2() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.VERIFY);
    }

    @Test
    public void shouldThrowWhenVerifyTestCases3() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.ACCEPTED, Action.VERIFY);
    }

    @Test
    public void shouldThrowWhenVerifyTestCases4() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.VERIFY);
    }

    @Test
    public void shouldDelete() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.CREATED, Action.DELETE);
    }

    @Test
    public void shouldThrowWhenDeleteTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.VERIFIED, Action.DELETE);
    }

    @Test
    public void shouldThrowWhenDeleteTestCases1() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.DELETE);
    }

    @Test
    public void shouldThrowWhenDeleteTestCases2() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.ACCEPTED, Action.DELETE);
    }

    @Test
    public void shouldThrowWhenDeleteTestCases3() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.DELETE);
    }

    @Test
    public void shouldThrowWhenDeleteTestCases4() throws Exception {
        shouldThrowRuntimeWithOutCause(ApplicationStatus.VERIFIED, Action.DELETE);
    }

    @Test
    public void shouldReject() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.VERIFIED, Action.REJECT);
    }

    @Test
    public void shouldThrowWhenRejectTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.CREATED, Action.REJECT);
    }

    @Test
    public void shouldThrowWhenRejectTestCases1() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.REJECT);
    }

    @Test
    public void shouldThrowWhenRejectTestCases2() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.REJECT);
    }

    @Test
    public void shouldThrowWhenRejectTestCases3() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.REJECT);
    }

    @Test
    public void shouldThrowWhenRejectTestCases4() throws Exception {
        shouldThrowRuntimeWithOutCause(ApplicationStatus.CREATED, Action.REJECT);
    }

    @Test
    public void shouldAccept() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.VERIFIED, Action.ACCEPT);
    }

    @Test
    public void shouldThrowWhenAcceptTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.ACCEPTED, Action.ACCEPT);
    }

    @Test
    public void shouldThrowWhenAcceptTestCases1() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.CREATED, Action.ACCEPT);
    }

    @Test
    public void shouldThrowWhenAcceptTestCases2() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.ACCEPT);
    }

    @Test
    public void shouldThrowWhenAcceptTestCases3() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.ACCEPT);
    }

    @Test
    public void shouldThrowWhenAcceptTestCases4() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.ACCEPT);
    }

    @Test
    public void shouldPublish() throws Exception {
        shouldChangedForStatusAndAction(ApplicationStatus.ACCEPTED, Action.PUBLISH);
    }

    @Test
    public void shouldThrowWhenPublishTestCases() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.PUBLISHED, Action.PUBLISH);
    }

    @Test
    public void shouldThrowWhenPublishTestCases1() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.VERIFIED, Action.PUBLISH);
    }

    @Test
    public void shouldThrowWhenPublishTestCases2() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.REJECTED, Action.PUBLISH);
    }

    @Test
    public void shouldThrowWhenPublishTestCases3() throws Exception {
        shouldThrowForStatusAndAction(ApplicationStatus.DELETED, Action.PUBLISH);
    }

    @Test
    public void shouldThrowWhenPublishTestCases4() throws Exception {
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
    }

    @Test
    public void shouldFindApplicationTestCases1() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter("", null);
    }

    @Test
    public void shouldFindApplicationTestCases2() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter("xxx", null);
    }

    @Test
    public void shouldFindApplicationTestCases3() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter(null, new ArrayList<>(0));
    }

    @Test
    public void shouldFindApplicationTestCases4() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter("", new ArrayList<>(0));
    }

    @Test
    public void shouldFindApplicationTestCases5() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter("xxx", new ArrayList<>(0));
    }

    @Test
    public void shouldFindApplicationTestCases6() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter(null, STATUS_COLLECTIONS);
    }

    @Test
    public void shouldFindApplicationTestCases7() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter("", STATUS_COLLECTIONS);
    }

    @Test
    public void shouldFindApplicationTestCases8() throws ApplicationFormChangingStatusException {
        shouldFindApplicationForFilter("xxx", STATUS_COLLECTIONS);
    }

    @Test
    public void shouldThrowRuntimeWhenFilteringPageWithWrongStatusCollections() throws ApplicationFormChangingStatusException {
        //GIVEN
        List<String> wrongStatusCollection = new ArrayList<>(1);
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

    @Test
    public void shouldUpdate() {
        //GIVEN
        when(applicationFormRepository.findById(1)).thenReturn(getApplicationForm(ApplicationStatus.CREATED));

        //WHEN
        testee.update(1l, "new name", "new content");

        //THEN
        verify(applicationFormRepository, times(1)).findById(1);
        verify(applicationFormRepository, times(1)).save(Matchers.any(ApplicationForm.class));
    }

    @Test
    public void shouldThrowRuntimeWhenUpdateUnValidFieldTestCase() {
        updateTest = true;
        shouldThrowRuntimeWithOutNameOrContent(null, null);
        updateTest = false;
    }


    @Test
    public void shouldThrowRuntimeWhenUpdateUnValidFieldTestCase1() {
        updateTest = true;
        shouldThrowRuntimeWithOutNameOrContent("", "");
        updateTest = false;
    }

    @Test
    public void shouldThrowRuntimeWhenUpdateUnValidFieldTestCase2() {
        updateTest = true;
        shouldThrowRuntimeWithOutNameOrContent(null, "");
        updateTest = false;
    }

    @Test
    public void shouldThrowRuntimeWhenUpdateUnValidFieldTestCase3() {
        updateTest = true;
        shouldThrowRuntimeWithOutNameOrContent("", null);
        updateTest = false;
    }

    /**
     * dynamic method for assert find applications for filter
     *
     * @param name             - name application form
     * @param statusCollection - Collection<String>
     */
    private void shouldFindApplicationForFilter(String name, List<String> statusCollection) throws ApplicationFormChangingStatusException {
        //GIVEN
        EnumSet applicationStatuses = ApplicationStatus.enumSetForStatusCollection(statusCollection);
        boolean forStatus = false;
        if (applicationStatuses != null && ApplicationStatus.allStatusCollection.containsAll(applicationStatuses)) {
            forStatus = true;
        }
        if (name != null && !name.isEmpty() && forStatus) {
            //  for  shouldFindApplicationForFilter("xxx", STATUS_COLLECTIONS);
            when(applicationFormRepository.findByNameContainingAndStatusIn(name, applicationStatuses, REQUEST)).thenReturn(pageApplicationForms);
            check(name, statusCollection, 10);
        } else if ((name == null || name.isEmpty()) && (statusCollection == null || statusCollection.isEmpty())) {
            //  for  shouldFindApplicationForFilter(null, null);
            //  for  shouldFindApplicationForFilter("", new ArrayList<>(0));
            //  for  shouldFindApplicationForFilter(null, new ArrayList<>(0));
            //  for  shouldFindApplicationForFilter("", null);
            //WHEN
            Page<ApplicationForm> page = testee.findByNameOrStatusIn(name, statusCollection, PAGE_NUMBER);
            //THEN
            Assert.assertTrue(page == null);
        } else if (name != null && !name.isEmpty()) {
            //  for  shouldFindApplicationForFilter("xxx", new ArrayList<>(0));
            //  for  shouldFindApplicationForFilter("xxx", null);
            when(applicationFormRepository.findByNameContaining(name, REQUEST)).thenReturn(pageApplicationForms);
            check(name, statusCollection, 10);
        } else if (forStatus) {
            //  for  shouldFindApplicationForFilter("", STATUS_COLLECTIONS);
            //  for  shouldFindApplicationForFilter(null, STATUS_COLLECTIONS);
            when(applicationFormRepository.findByStatusIn(applicationStatuses, REQUEST)).thenReturn(pageApplicationForms);
            check(name, statusCollection, 10);
        }
    }

    private void check(String name, List<String> statusCollection, long total) throws ApplicationFormChangingStatusException {
        when(pageApplicationForms.getTotalElements()).thenReturn(total);
        //WHEN
        long totalElements = testee.findByNameOrStatusIn(name, statusCollection, PAGE_NUMBER).getTotalElements();
        //THEN
        Assert.assertTrue(totalElements == total);
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
                testee.verify(1);
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
        thrown.expectMessage("Can not changing status.");

        //WHEN
        switch (action) {
            case REJECT:
                testee.reject(1, "Cause message...");
                break;
            case DELETE:
                testee.delete(1, "Cause message...");
                break;
            case VERIFY:
                testee.verify(1);
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

        if (updateTest) {
            testee.update(1l, name, content);
        } else {
            testee.create(name, content);
        }
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

        if (ApplicationStatus.CREATED.equals(oldStatus) && Action.REJECT.equals(action)) {
            thrown.expect(ApplicationFormChangingStatusException.class);
            thrown.expectMessage("Can not changing status.");
        } else if (ApplicationStatus.VERIFIED.equals(oldStatus) && Action.DELETE.equals(action)) {
            thrown.expect(ApplicationFormChangingStatusException.class);
            thrown.expectMessage("Can not changing status.");
        } else {
            thrown.expect(ApplicationFormChangingStatusRuntimeException.class);
            thrown.expectMessage("Cause is required.");
        }

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
