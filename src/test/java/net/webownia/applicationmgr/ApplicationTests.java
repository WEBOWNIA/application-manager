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
package net.webownia.applicationmgr;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
import net.webownia.applicationmgr.service.ApplicationFormService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest(value = "spring.profiles.active=tests")
@DirtiesContext
@Ignore
public class ApplicationTests {

    @Autowired
    private ApplicationFormService applicationFormService;

    @Autowired
    private ApplicationFormRepository applicationFormRepository;

    @Test
    public void contextLoads() throws Exception {
        applicationFormService.create("note1", "wersja 0");
        applicationFormService.create("note2", "wersja 0");
        applicationFormService.create("note3", "wersja 0");
        applicationFormService.create("note4", "wersja 0");
        applicationFormService.create("note5", "wersja 0");
        applicationFormService.create("note6", "wersja 0");
        applicationFormService.create("note7", "wersja 0");
        applicationFormService.create("note8", "wersja 0");
        applicationFormService.create("note9", "wersja 0");
        applicationFormService.create("note10", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");
        applicationFormService.create("note11", "wersja 0");


        long i = 0;

        for (ApplicationForm form : applicationFormRepository.findAll()) {
            if ((i / 5) > 0) {
                applicationFormService.delete(i, "bo tak");
            }
            i = form.getId();
            System.out.println(form);
        }
    }

}
