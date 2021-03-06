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
package net.webownia.applicationmgr.data.repository;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumSet;

/**
 * Repository for ApplicationForm entity
 * Created by Adam Barczewski on 2014-12-03.
 */
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {

    Page<ApplicationForm> findByNameContainingAndStatusIn(String name, EnumSet<ApplicationStatus> statusCollection, Pageable pageable);

    Page<ApplicationForm> findByNameContaining(String name, Pageable pageable);

    Page<ApplicationForm> findByStatusIn(EnumSet<ApplicationStatus> statusCollection, Pageable pageable);

    ApplicationForm findById(long id);
}
