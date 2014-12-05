package net.webownia.applicationmgr;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
import net.webownia.applicationmgr.service.ApplicationFormService;
import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfiguration.class)
@WebAppConfiguration
@IntegrationTest(value = "spring.profiles.active=tests")
@DirtiesContext
public class ApplicationTests {

    @Value("${local.server.port}")
    private int port;

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

        for (ApplicationForm form : applicationFormRepository.findAll()) {
            System.out.println(form);
        }
    }

}
