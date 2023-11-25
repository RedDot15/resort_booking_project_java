package t3h.bigproject.controller.frontend;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import t3h.bigproject.dto.ResortDto;

@Controller
public class CustomerController {
    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    public String profile(Model model) {
        return "frontend/profile.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/trips")
    public String trips(Model model) {
        return "frontend/trips.html";
    }
}
