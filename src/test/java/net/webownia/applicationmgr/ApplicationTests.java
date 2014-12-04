package net.webownia.applicationmgr;

import net.webownia.applicationmgr.data.dao.ApplicationFormDAO;
import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.data.repository.ApplicationFormRepository;
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
@IntegrationTest("server.port=0")
@DirtiesContext
public class ApplicationTests {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ApplicationFormDAO applicationFormDAO;

    @Autowired
    private ApplicationFormRepository applicationFormRepository;

    @Test
    public void contextLoads() throws Exception {
        applicationFormDAO.createNew("note1", "wersja 0");
        applicationFormDAO.createNew("note2", "wersja 0");
        applicationFormDAO.createNew("note3", "wersja 0");
        applicationFormDAO.createNew("note4", "wersja 0");

        ApplicationForm applicationForm = applicationFormRepository.findById(2l);
        applicationFormDAO.changeStage(applicationForm.getId(), ApplicationStage.ACCEPTED);

        for (ApplicationForm form : applicationFormRepository.findAll()) {
            System.out.println(form);
        }
    }

}
