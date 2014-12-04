package net.webownia.applicationmgr.data.dao;

import net.webownia.applicationmgr.shared.enums.ApplicationStage;

import javax.transaction.Transactional;

/**
 * Created by abarczewski on 2014-12-03.
 */
public interface ApplicationFormDAO {

    void createNew(String name, String content);

    @Transactional
    void changeContent(Long id, String newContent) throws Exception;

    @Transactional
    void deleteOrReject(Long id, String cause, ApplicationStage actualStage) throws Exception;

    @Transactional
    void changeStage(Long id, ApplicationStage actualStage) throws Exception;
}
