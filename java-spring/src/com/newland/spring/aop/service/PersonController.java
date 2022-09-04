package com.newland.spring.aop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Author: leell
 * Date: 2022/9/4 00:19:23
 */
@Controller
public class PersonController {
    @Autowired
    private PersonService personService;
    public void save(String str){
        personService.save("");
    }
}
