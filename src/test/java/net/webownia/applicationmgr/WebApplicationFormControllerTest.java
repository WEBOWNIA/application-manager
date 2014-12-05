package net.webownia.applicationmgr;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class WebApplicationFormControllerTest {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
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
}
