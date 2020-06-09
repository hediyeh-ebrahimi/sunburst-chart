package com.example.chart.controller;
import com.example.chart.model.Department;
import com.example.chart.model.Item;
import com.example.chart.model.User;
import com.example.chart.repository.UserRepository;
import com.sun.deploy.net.HttpResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class Login {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginForm(@Nullable @RequestParam("error") String error, Model model){
        if(error != null){
            model.addAttribute("error","نام کاربری یا رمز عبور صحیح نمی باشد");
        }
        return "login";
    }

    @RequestMapping(value = "/submitlogin",method = RequestMethod.POST)
    public String validateLogin(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes, Model model,  HttpServletResponse response) {

        final String uri = "http://localhost:8080/authenticate";

        RestTemplate restTemplate = new RestTemplate();
        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        String result = null;
        result = restTemplate.postForObject(uri, entity,String.class);
        String token = null;

        if(result == null){
            token = "";
        }else{
            JSONObject jsonObject = new JSONObject(result);
            token = (String) jsonObject.get("token");
        }
        if(!token.isEmpty() && token != null){
            model.addAttribute("department",new Department());
            model.addAttribute("item",new Item());
            Cookie jwtCookie = new Cookie("jwtToken",token);
            response.addCookie(jwtCookie);
            return "redirect:/";
        }else{
            redirectAttributes.addFlashAttribute("error","نام کاربری یا رمز عبور صحیح نمی باشد");
            return "redirect:/login";
        }

    }

    @RequestMapping("/403")
    public String loginError(){
        return "login";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,@RequestParam String password,RedirectAttributes redirectAttributes) {
        String encodedPassword  = passwordEncoder.encode(password);
        User user = new User();
        user.setEnabled(Boolean.TRUE);
        user.setPassword(encodedPassword);
        user.setUsername(username);
        user.setRole("ADMIN");

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success_res","کاربر جدید با موفقیت افزوده شد.");
        return "redirect:/login";
    }
}
