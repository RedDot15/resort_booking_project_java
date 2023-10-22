package t3h.bigproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"home","/"})
    public String home(Model model){
        return "index.html";
    }

    @GetMapping(value = {"login"})
    public String login(Model model) {
        return "login.html";
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "errors/403.html";
    }
}
