package t3h.bigproject.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortImageDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.service.*;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    CityService cityService;

    @Autowired
    ExtensionService extensionService;

    @Autowired
    ResortService resortService;

    @Autowired
    ResortImageService resortImageService;

    @Autowired
    RoomService roomService;

    @Autowired
    ResortExtensionService resortExtensionService;

    @Autowired
    BillService billService;


    @GetMapping(value = {"home","/"})
    public String home(Model model){
        Object danhsachCityVietNam = cityService.getAllByCountryId((long) 1);
        model.addAttribute("listCityVietNam", danhsachCityVietNam);
        Object danhsachCityQuocTe = cityService.getAllByCountryIdIsNot((long) 1);
        model.addAttribute("listCityQuocTe", danhsachCityQuocTe);
        return "frontend/index.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/city/{id}")
    public String city(@PathVariable Long id, Model model){
        Object city = cityService.getDetailById(id);
        model.addAttribute("cityDto", city);
        Object extensionList = extensionService.getAll(null);
        model.addAttribute("extensionList", extensionList);
        Object resortList = resortService.getAllByCityId(id);
        model.addAttribute("resortList", resortList);

        return "frontend/city.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/resort/{id}")
    public String resort(@PathVariable Long id, Model model){
        Object resortDto = resortService.getDetailById(id);
        model.addAttribute("resortDto", resortDto);
        Object roomDtoList = roomService.getAllByResortId(id);
        model.addAttribute("roomDtoList", roomDtoList);
        Object resortExtensionList = resortExtensionService.getAllByResortId(id);
        model.addAttribute("extensionList", resortExtensionList);

        return "frontend/resort.html";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/thankyou")
    public String thankyou(Model model){
        return "frontend/thankyou.html";
    }

    @GetMapping(value = {"login"})
    public String login(Model model) {
        return "frontend/login.html";
    }

    @RequestMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("message","Bạn không có quyền truy cập");
        return "errors/403.html";
    }
}
