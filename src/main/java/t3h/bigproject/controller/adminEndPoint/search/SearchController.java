package t3h.bigproject.controller.adminEndPoint.search;

import java.security.Principal;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import t3h.bigproject.service.CityService;
import t3h.bigproject.utils.FileUtils;

@RestController
public class SearchController {
    @Autowired
    CityService cityService;

    @Autowired
    FileUtils fileUtils;

    @Operation(summary = "Get all Viet Nam City")
    @GetMapping("/city/search")
    @ResponseBody
    public ResponseEntity<?> getAllCityInVietnam(Principal principal) {
        Object p = cityService.getAllCityByKeyword("");
        return ResponseEntity.ok(p);
    }

    @Operation(summary = "Get all City By keyword")
    @GetMapping("/city/search/{keyword}")
    @ResponseBody
    public ResponseEntity<?> getAllCityByKeyword(@PathVariable("keyword") String keyword, Principal principal) {
        Object p = cityService.getAllCityByKeyword(keyword);
        return ResponseEntity.ok(p);
    }
}
