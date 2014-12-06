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
package net.webownia.applicationmgr.controller;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.exception.ApplicationFormChangingStatusException;
import net.webownia.applicationmgr.service.ApplicationFormService;
import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@Controller
public class WebApplicationFormController extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @Autowired
    private ApplicationFormService applicationFormService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) throws IOException {

        Page<ApplicationForm> page = applicationFormService.findAll(1);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("total", page.getTotalElements());

        setAppNameAndVersionOnModel(model);

        return "index";
    }

    @RequestMapping(value = "/applications/{pageNumber}", method = RequestMethod.GET)
    public String applications(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "statusCollection", required = false) List<String> collectionStatus,
            @PathVariable Integer pageNumber, Model model) throws Exception {

        Page<ApplicationForm> page = null;

        EnumSet<ApplicationStatus> applicationStatuses = ApplicationStatus.enumSetForStatusCollection(collectionStatus);

        if (collectionStatus != null && !collectionStatus.isEmpty() && ApplicationStatus.allStatusCollection.containsAll(applicationStatuses)) {
            page = applicationFormService.findByNameOrStatusIn(name, collectionStatus, pageNumber);
        } else {
            page = applicationFormService.findAll(pageNumber);
        }

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("total", page.getTotalElements());

        setAppNameAndVersionOnModel(model);

        return "index";
    }

    private void setAppNameAndVersionOnModel(Model model) throws IOException {
        Properties prop = new Properties();
        prop.load(WebApplicationFormController.class.getResourceAsStream("/application.properties"));
        model.addAttribute("versionApp", prop.getProperty("application.version"));
    }
}
