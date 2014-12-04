package net.webownia.applicationmgr.service;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by abarczewski on 2014-12-04.
 */
@Service
class ApplicationFormServiceImpl implements ApplicationFormService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ApplicationFormRepository repository;


    @Override
    public void create(String name, String content) {
        ApplicationForm applicationForm = new ApplicationForm(name, content, ApplicationStage.CREATED, LocalDateTime.now());
        repository.save(applicationForm);
    }

    @Override
    public void delete(long id, String cause) throws Exception {

    }

    @Override
    public void verified(long id) throws Exception {

    }

    @Override
    public void reject(long id) throws Exception {

    }

    @Override
    public void accept(long id) throws Exception {
        ApplicationForm applicationForm = repository.findById(id);
        applicationForm.setStage(ApplicationStage.ACCEPTED);
        applicationForm.setFromDate(LocalDateTime.now());
        // version ++
        repository.save(applicationForm);
    }

    @Override
    public void publish(long id) throws Exception {

    }

    @Override
    public Page<ApplicationForm> findByNameOrStage(String name, ApplicationStage stage, Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
        return repository.findByNameOrStage(name, stage, request);
    }

    @Override
    public Page<ApplicationForm> findByName(String name, Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
        return repository.findByName(name, request);
    }

    @Override
    public Page<ApplicationForm> findAll(Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
        return repository.findAll(request);
    }
}
