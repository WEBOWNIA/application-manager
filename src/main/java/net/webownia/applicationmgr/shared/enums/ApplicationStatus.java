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
package net.webownia.applicationmgr.shared.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
public enum ApplicationStatus {
    CREATED,
    DELETED,
    VERIFIED,
    REJECTED,
    ACCEPTED,
    PUBLISHED;

    public static final EnumSet<ApplicationStatus> stagesForRejecting = EnumSet.of(VERIFIED, ACCEPTED);
    public static final EnumSet<ApplicationStatus> allStatusCollection = EnumSet.of(CREATED, DELETED, VERIFIED, REJECTED, ACCEPTED, PUBLISHED);

    public static Collection<String> statusCollectionForEnumSet(EnumSet<ApplicationStatus> enumSet) {
        Collection<String> statusCollection = new ArrayList<>(0);
        for (ApplicationStatus status : enumSet) {
            statusCollection.add(status.name());
        }
        return statusCollection;
    }

    public static EnumSet<ApplicationStatus> enumSetForStatusCollection(Collection<String> statusCollection) {
        List<ApplicationStatus> enumList = new ArrayList<>(statusCollection.size());
        for (String status : statusCollection) {
            enumList.add(ApplicationStatus.valueOf(status));
        }
        return EnumSet.copyOf(enumList);
    }
}
