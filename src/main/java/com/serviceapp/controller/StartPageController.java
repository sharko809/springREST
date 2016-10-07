package com.serviceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class StartPageController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView start() {
        return new ModelAndView("index");
    }

}
