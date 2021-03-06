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

import com.jayway.restassured.RestAssured;
import net.webownia.applicationmgr.service.ApplicationFormService;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.when;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("tests")
@IntegrationTest
@Ignore
public class WebApplicationFormControllerTest {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${local.server.port}")
    int port;

    @Autowired
    private ApplicationFormService applicationFormService;


    @Before
    public void setUp() {
        for (int i = 1; i <= 25; i++) {
            applicationFormService.create("note" + i, "content message");
        }

        RestAssured.port = port;
    }

    @Test
    public void shouldLoadProfile() throws Exception {
        Assert.assertTrue(profile.equals("tests"));
    }

    @Test
    public void shouldGetAllApplications() {
        when().get("/").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldGetApplicationsForStatusCollection() {
        when().get("/applications/1?statuses=CREATED,DELETED").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldGetApplicationsForName() {
        when().get("/applications/1?name=note23").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void should500ForGetApplicationsByWrongStatus() {
        when().get("/applications/1?statuses=CREATED,DELETE").then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
