package t3h.bigproject.controller.backend.filter;

import java.security.Principal;
import java.util.ArrayList;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import t3h.bigproject.params.ExtensionCityParam;
import t3h.bigproject.service.CityService;
import t3h.bigproject.service.ResortExtensionService;
import t3h.bigproject.utils.FileUtils;

@RestController
public class FilterController {
    @Autowired
    CityService cityService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ResortExtensionService resortExtensionService;

    @PostMapping("/resort/filter")
    public ResponseEntity<?> getAllResortByEntensionIdAndCityId(
            @RequestBody ExtensionCityParam extensionCity) {
        Object res = resortExtensionService.getAllResortByEntensionIdAndCityId(extensionCity);
        return ResponseEntity.ok(res);
    }
}
