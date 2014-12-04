package net.webownia.applicationmgr.controller;

import net.webownia.applicationmgr.data.model.ApplicationForm;
import net.webownia.applicationmgr.service.ApplicationFormService;
import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by abarczewski on 2014-12-03.
 */
@Controller
public class ApplicationFormController {

    @Autowired
    private ApplicationFormService applicationFormService;

    @RequestMapping("/")
     public String index(Model model) {

        Page<ApplicationForm>  page = applicationFormService.findAll(1);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("total", page.getTotalElements());
        return "index";
    }

    @RequestMapping(value = "/applications/{pageNumber}", method = RequestMethod.GET)
    public String applications(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "stage", required = false) String stage,
            @PathVariable Integer pageNumber, Model model) {

        Page<ApplicationForm> page = null;

        if (stage != null && !stage.isEmpty() && ApplicationStage.allStages.contains(stage)) {
            ApplicationStage applicationStage = ApplicationStage.getByName(stage);
            page = applicationFormService.findByNameOrStage(name, applicationStage, pageNumber);
        } else if(name != null && !name.isEmpty()) {
            page = applicationFormService.findByName(name, pageNumber);
        }else{
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
        return "index";
    }
}
