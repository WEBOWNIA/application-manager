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
package net.webownia.applicationmgr.exception;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
public class ApplicationFormChangingStatusException extends Exception {

    public ApplicationFormChangingStatusException(String message) {
        super(message);
    }

    public ApplicationFormChangingStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
