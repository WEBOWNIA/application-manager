/*
* Copyright 2008-2014 the original author or authors.
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
import java.util.Collection;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
public interface ApplicationFormService {

    void create(String name, String content) throws ApplicationFormChangingStatusRuntimeException;

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(long id, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    @Transactional(Transactional.TxType.REQUIRED)
    void verified(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    @Transactional(Transactional.TxType.REQUIRED)
    void reject(long id, String cause) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    @Transactional(Transactional.TxType.REQUIRED)
    void accept(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    @Transactional(Transactional.TxType.REQUIRED)
    void publish(long id) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    Page<ApplicationForm> findByNameOrStatusIn(String name, Collection<String> statusCollection, Integer pageNumber) throws ApplicationFormChangingStatusRuntimeException, ApplicationFormChangingStatusException;

    Page<ApplicationForm> findAll(Integer pageNumber);

    Page<ApplicationFormAudit> findByApplicationFormId(long applicationFormId, Integer pageNumber);
}
