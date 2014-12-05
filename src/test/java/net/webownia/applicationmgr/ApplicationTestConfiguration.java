package net.webownia.applicationmgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by abarczewski on 2014-12-04.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@Profile("tests")
public class ApplicationTestConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTestConfiguration.class, args);
    }
}
