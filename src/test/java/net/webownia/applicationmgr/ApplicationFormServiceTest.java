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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationFormServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ApplicationFormRepository applicationFormRepository;

    @Mock
    private ApplicationFormAuditRepository auditRepository;

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

    /**
     * Dynamic method for success in run service action for changing status
     *
     * @param oldStatus - status before action
     * @param action    - name for service action
     * @throws Exception
     */
    private void shouldChangedForStatusAndAction(ApplicationStatus oldStatus, Action action) throws Exception {
        //GIVEN
        ApplicationForm applicationForm = new ApplicationForm("note", "contentMessage", oldStatus, LocalDateTime.now());
        when(applicationFormRepository.findById(1)).thenReturn(applicationForm);

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
        ApplicationForm applicationForm = new ApplicationForm("note", "contentMessage", oldStatus, LocalDateTime.now());
        when(applicationFormRepository.findById(1)).thenReturn(applicationForm);

        thrown.expect(ApplicationFormChangingStatusException.class);
        thrown.expectMessage("Nie można zmienić statusu wniosku.");

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
        ApplicationForm applicationForm = new ApplicationForm("note", "contentMessage", oldStatus, LocalDateTime.now());
        when(applicationFormRepository.findById(1)).thenReturn(applicationForm);

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
