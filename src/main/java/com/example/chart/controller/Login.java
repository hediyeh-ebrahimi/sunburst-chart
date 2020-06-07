package com.example.chart.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Login {
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginForm(@Nullable @RequestParam("error") String error, Model model){
        if(error != null){
            model.addAttribute("error","نام کاربری یا رمز عبور صحیح نمی باشد");
        }
        return "login";
    }

    @RequestMapping("/403")
    public String loginError(){
        return "login";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String registerForm(){
        return "register";
    }
}
