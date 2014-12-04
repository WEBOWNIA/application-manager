package net.webownia.applicationmgr.data.repository;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by abarczewski on 2014-12-03.
 */
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {

    Page<ApplicationForm> findByNameOrStage(String name, ApplicationStage stage, Pageable pageable);

    ApplicationForm findById(long id);

    Page<ApplicationForm> findByName(String name, Pageable pageable);

    Page<ApplicationForm> findAll(Pageable pageable);
}
