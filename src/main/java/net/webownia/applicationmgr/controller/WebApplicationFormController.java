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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@Controller
public class WebApplicationFormController extends WebMvcConfigurerAdapter {

    private List<String> statusesFilter;
    private String nameFilter;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/new").setViewName("new");
        registry.addViewController("/preview").setViewName("preview");
        registry.addViewController("/edit").setViewName("edit");
        registry.addViewController("/deleteOrReject").setViewName("deleteOrReject");
        registry.addViewController("/history").setViewName("history");
    }

    @Autowired
    private ApplicationFormService applicationFormService;

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json; charset=utf-8")
    public String applications(Model model) throws IOException, ApplicationFormChangingStatusException {

        if (statusesFilter == null) {
            statusesFilter = ApplicationStatus.statusCollectionForEnumSet(ApplicationStatus.allStatusCollection);
        }
        Page<ApplicationForm> page = applicationFormService.findByNameOrStatusIn(nameFilter, statusesFilter, 1);

        setModelForIndexPage(model, page);

        setAppNameAndVersionOnModel(model);

        return "index";
    }

    @RequestMapping(value = "/applications", method = RequestMethod.POST, headers = "Accept=application/json; charset=utf-8")
    public String applications(@RequestParam(value = "page", required = false) Integer pageNumber,
                               Filter filter, Model model) throws IOException, ApplicationFormChangingStatusException {

        if (pageNumber == null) {
            pageNumber = 1;
        }

        nameFilter = filter.getNameFilter();

        Page<ApplicationForm> page = applicationFormService.findByNameOrStatusIn(nameFilter, statusesFilter, pageNumber);

        setModelForIndexPage(model, page);

        setAppNameAndVersionOnModel(model);

        return "index";
    }

    @RequestMapping(value = "/applications", method = RequestMethod.GET, headers = "Accept=application/json; charset=utf-8")
    public String paging(@RequestParam(value = "page", required = false) Integer pageNumber, Model model) throws IOException, ApplicationFormChangingStatusException {

        if (pageNumber == null) {
            pageNumber = 1;
        }

        Page<ApplicationForm> page = applicationFormService.findByNameOrStatusIn(nameFilter, statusesFilter, pageNumber);

        setModelForIndexPage(model, page);

        setAppNameAndVersionOnModel(model);

        return "index";
    }

    @RequestMapping(value = "/applications/{status}/{method}", method = RequestMethod.GET, headers = "Accept=application/json; charset=utf-8")
    public String applications(
            @PathVariable String status,
            @PathVariable String method,
            @RequestParam(value = "page", required = false) Integer pageNumber,
            Model model) throws Exception {

        if (statusesFilter == null) {
            statusesFilter = new ArrayList<>(0);
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }

        if (!StringUtils.isEmpty(method)) {
            if ("show".equals(method)) {
                statusesFilter.add(status);
            } else if ("hide".equals(method)) {
                statusesFilter.remove(status);
            }
        }

        Page<ApplicationForm> page = applicationFormService.findByNameOrStatusIn(nameFilter, statusesFilter, pageNumber);

        setModelForIndexPage(model, page);

        setAppNameAndVersionOnModel(model);

        return "index";
    }

    @RequestMapping(value = "/application/new", method = RequestMethod.GET, headers = "Accept=application/json; charset=utf-8")
    public String addNew(Model model) throws IOException {
        setAppNameAndVersionOnModel(model);
        return "new";
    }

    @RequestMapping(value = "/application/save", method = RequestMethod.POST, headers = "Accept=application/json; charset=utf-8")
    public String save(ApplicationForm applicationForm, Model model) throws IOException {
        applicationFormService.create(applicationForm.getName(), applicationForm.getContent());
        setAppNameAndVersionOnModel(model);
        return "redirect:/";
    }

    @RequestMapping(value = "/application/{id}/update", method = RequestMethod.POST, headers = "Accept=application/json; charset=utf-8")
    public String update(@PathVariable Long id, ApplicationForm applicationForm) throws IOException, ApplicationFormChangingStatusException {
        applicationFormService.update(id, applicationForm.getName(), applicationForm.getContent());
        return "redirect:/";
    }

    @RequestMapping(value = "/application/{id}/{method}", method = RequestMethod.GET, headers = "Accept=application/json; charset=utf-8")
    public String changeStatus(@PathVariable Long id, @PathVariable String method, Model model) throws ApplicationFormChangingStatusException, IOException {
        if ("verify".equals(method) && id != null) {
            applicationFormService.verify(id);
        } else if ("accept".equals(method) && id != null) {
            applicationFormService.accept(id);
        } else if ("publish".equals(method) && id != null) {
            applicationFormService.publish(id);
        } else if ("preview".equals(method) && id != null) {
            return toView(id, model, "preview");
        }else if ("edit".equals(method) && id != null) {
            return toView(id, model, "edit");
        }else if ("history".equals(method) && id != null) {
            return toView(id, model, "history");
        } else if (("delete".equals(method) || "reject".equals(method)) && id != null) {
            return toView(id, model, "deleteOrReject");
        }
        return "redirect:/";
    }

    private String toView(Long id, Model model, String viewName) throws IOException {
        model.addAttribute("appForm", applicationFormService.findById(id));
        setAppNameAndVersionOnModel(model);
        return viewName;
    }

    private void setModelForIndexPage(Model model, Page<ApplicationForm> page) {
        if (page != null) {
            int current = page.getNumber() + 1;
            int begin = Math.max(1, current - 5);
            int end = Math.min(begin + 10, page.getTotalPages());

            model.addAttribute("page", page);
            model.addAttribute("beginIndex", begin);
            model.addAttribute("endIndex", end);
            model.addAttribute("currentIndex", current);
            model.addAttribute("total", page.getTotalElements());
            model.addAttribute("pageWrapper", new PageWrapper<ApplicationForm>(page, "/applications"));
        } else {
            model.addAttribute("total", 0);
            model.addAttribute("currentIndex", 1);
        }
        model.addAttribute("statuses", statusesFilter);
    }

    private void setAppNameAndVersionOnModel(Model model) throws IOException {
        Properties prop = new Properties();
        prop.load(WebApplicationFormController.class.getResourceAsStream("/application.properties"));
        model.addAttribute("versionApp", prop.getProperty("application.version"));
    }
}
