package t3h.bigproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    @GetMapping(value = {"home","/"})
    public String home(Model model){
        return "frontend/index.html";
    }

    @RequestMapping("/city")
    public String city(Model model){
        return "frontend/city.html";
    }

    @RequestMapping("/resort")
    public String resort(Model model){
        return "frontend/resort.html";
    }

    @RequestMapping("/booking")
    public String booking(Model model){
        return "frontend/booking.html";
    }

    @RequestMapping("/thankyou")
    public String thankyou(Model model){
        return "frontend/thankyou.html";
    }

    @GetMapping(value = {"login"})
    public String login(Model model) {
        return "frontend/login.html";
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "errors/403.html";
    }
}
