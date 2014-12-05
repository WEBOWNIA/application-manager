package net.webownia.applicationmgr.data.repository;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ApplicationForm entity
 * Created by abarczewski on 2014-12-03.
 */
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {

    Page<ApplicationForm> findByNameOrStatus(String name, ApplicationStatus status, Pageable pageable);

    ApplicationForm findById(long id);

    Page<ApplicationForm> findByName(String name, Pageable pageable);

    Page<ApplicationForm> findAll(Pageable pageable);
}
