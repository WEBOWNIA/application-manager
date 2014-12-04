package net.webownia.applicationmgr.data.dao;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStageException;
import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by abarczewski on 2014-12-03.
 */
@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ApplicationFormDAOImpl implements ApplicationFormDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationFormRepository repository;

    @Override
    public void createNew(String name, String content) {
        ApplicationForm applicationForm = new ApplicationForm(name, content, ApplicationStage.CREATED, LocalDateTime.now());
        repository.save(applicationForm);
    }

    @Override
    public void changeContent(Long id, String newContent) throws Exception {
        ApplicationForm applicationForm = repository.findById(id);
        if (ApplicationStage.stagesForChangeContent.contains(applicationForm.getStage())) {
            applicationForm.setContent(newContent);
            applicationForm.setFromDate(LocalDateTime.now());
            // version ++
            entityManager.merge(applicationForm);
        } else {
            throw new ApplicationFormChangingStageException("Treść wniosku można zmienić tylko przy stanach " + ApplicationStage.stagesForChangeContent);
        }
    }

    @Override
    public void deleteOrReject(Long id, String cause, ApplicationStage actualStage) throws Exception {
        ApplicationForm applicationForm = repository.findById(id);
        applicationForm.setCause(cause);
        applicationForm.setFromDate(LocalDateTime.now());
        // version ++
        entityManager.merge(applicationForm);
    }

    @Override
    public void changeStage(Long id, ApplicationStage actualStage) throws Exception {
        ApplicationForm applicationForm = repository.findById(id);
        applicationForm.setStage(actualStage);
        applicationForm.setFromDate(LocalDateTime.now());
        // version ++
        entityManager.merge(applicationForm);
    }
}
