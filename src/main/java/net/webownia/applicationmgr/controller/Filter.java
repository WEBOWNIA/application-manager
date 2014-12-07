package net.webownia.applicationmgr.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Adam Barczewski on 2014-12-07.
 */
@Component
@Scope("request")
public class Filter {
    private String nameFilter;

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }
}
