/*
* Copyright 2014 Adam Barczewski webownia.net
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.webownia.applicationmgr.service;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.model.ApplicationFormAudit;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusException;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusRuntimeException;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
public interface ApplicationFormService {

    /**
     * Service for create new application
     *
     * @param name
     * @param content
     * @throws ApplicationFormChangingStatusRuntimeException
     */
    void create(String name, String content) throws ApplicationFormChangingStatusRuntimeException;

    /**
     * Service for delete
     *
     * @param id
     * @param cause
     * @throws ApplicationFormChangingStatusRuntimeException
     * @throws ApplicationFormChangingStatusException
     */
    @Transactional(Transactional.TxType.REQUIRED)
    void delete(long id, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    /**
     * Service for verify
     *
     * @param id
     * @throws ApplicationFormChangingStatusRuntimeException
     * @throws ApplicationFormChangingStatusException
     */
    @Transactional(Transactional.TxType.REQUIRED)
    void verify(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    /**
     * Service for reject
     *
     * @param id
     * @param cause
     * @throws ApplicationFormChangingStatusRuntimeException
     * @throws ApplicationFormChangingStatusException
     */
    @Transactional(Transactional.TxType.REQUIRED)
    void reject(long id, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    /**
     * Service for accept
     *
     * @param id
     * @throws ApplicationFormChangingStatusRuntimeException
     * @throws ApplicationFormChangingStatusException
     */
    @Transactional(Transactional.TxType.REQUIRED)
    void accept(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    /**
     * Service for publish
     *
     * @param id
     * @throws ApplicationFormChangingStatusRuntimeException
     * @throws ApplicationFormChangingStatusException
     */
    @Transactional(Transactional.TxType.REQUIRED)
    void publish(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    /**
     * Service for find applications by name or status collection
     *
     * @param name
     * @param statusCollection - List<String>
     * @param pageNumber
     * @return
     * @throws ApplicationFormChangingStatusRuntimeException
     * @throws ApplicationFormChangingStatusException
     */
    Page<ApplicationForm> findByNameOrStatusIn(String name, List<String> statusCollection, Integer pageNumber) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    /**
     * Service for find all applications
     *
     * @param pageNumber
     * @return
     */
    Page<ApplicationForm> findAll(Integer pageNumber);

    /**
     * Service for find applications by audit id
     *
     * @param applicationFormId
     * @param pageNumber
     * @return
     */
    Page<ApplicationFormAudit> findByApplicationFormId(long applicationFormId, Integer pageNumber);

    /**
     * Service for find Appication by id
     *
     * @param id
     * @return
     */
    ApplicationForm findById(Long id);

    /**
     * Service for update application
     *
     * @param id
     * @param name
     * @param content
     * @throws ApplicationFormChangingStatusRuntimeException
     */
    @Transactional(Transactional.TxType.REQUIRED)
    void update(Long id, String name, String content) throws ApplicationFormChangingStatusRuntimeException;
}
