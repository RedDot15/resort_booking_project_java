package t3h.bigproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import t3h.bigproject.service.CityService;

@Controller
public class HomeController {

    @Autowired
    CityService cityService;

    @GetMapping(value = {"home","/"})
    public String home(Model model){
        Object danhsachCityVietNam = cityService.getAllByCountryId((long) 1);
        model.addAttribute("listCityVietNam", danhsachCityVietNam);
        Object danhsachCityQuocTe = cityService.getAllByCountryIdIsNot((long) 1);
        model.addAttribute("listCityQuocTe", danhsachCityQuocTe);
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
