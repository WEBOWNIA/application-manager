package net.webownia.applicationmgr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by abarczewski on 2014-12-03.
 */
@Controller
public class ApplicationFormController {

    @RequestMapping("/applicationForms")
    public String applications(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "applicationForms";
    }
}
