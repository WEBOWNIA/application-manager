package net.webownia.applicationmgr.data.dao;

import net.webownia.applicationmgr.shared.enums.ApplicationStage;

import javax.transaction.Transactional;

/**
 * Created by abarczewski on 2014-12-03.
 */
public interface ApplicationFormDAO {

    void createNew(String name, String content);

    void changeContent(long id, String newContent) throws Exception;

    void deleteOrReject(long id, String cause, ApplicationStage actualStage) throws Exception;

    void changeStage(long id, ApplicationStage actualStage) throws Exception;
}
