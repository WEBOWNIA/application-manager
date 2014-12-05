package net.webownia.applicationmgr.data.repository;

import net.webownia.applicationmgr.data.model.ApplicationFormAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for audit ApplicationForm entity
 * Created by abarczewski on 2014-12-05.
 */
public interface ApplicationFormAuditRepository extends JpaRepository<ApplicationFormAudit, Long> {

    Page<ApplicationFormAudit> findByApplicationFormId(long applicationFormId, Pageable pageable);

}
