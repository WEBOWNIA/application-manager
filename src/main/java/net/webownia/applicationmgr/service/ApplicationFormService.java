package net.webownia.applicationmgr.service;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.model.ApplicationFormAudit;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusRuntimeException;
import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;

/**
 * Created by abarczewski on 2014-12-04.
 */
public interface ApplicationFormService {

    void create(String name, String content) throws ApplicationFormChangingStatusRuntimeException;

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(long id, String cause) throws Exception;

    @Transactional(Transactional.TxType.REQUIRED)
    void verified(long id) throws Exception;

    @Transactional(Transactional.TxType.REQUIRED)
    void reject(long id, String cause) throws Exception;

    @Transactional(Transactional.TxType.REQUIRED)
    void accept(long id) throws Exception;

    @Transactional(Transactional.TxType.REQUIRED)
    void publish(long id) throws Exception;

    Page<ApplicationForm> findByNameOrStage(String name, ApplicationStatus status, Integer pageNumber);

    Page<ApplicationForm> findByName(String name, Integer pageNumber);

    Page<ApplicationForm> findAll(Integer pageNumber);

    Page<ApplicationFormAudit> findByApplicationFormId(long applicationFormId, Integer pageNumber);
}
