package net.webownia.applicationmgr.data.repository;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by abarczewski on 2014-12-03.
 */
public interface ApplicationFormRepository extends PagingAndSortingRepository<ApplicationForm, Long> {

    List<ApplicationForm> findByNameOrStage(String name, ApplicationStage stage);

    ApplicationForm findById(Long id);
}
