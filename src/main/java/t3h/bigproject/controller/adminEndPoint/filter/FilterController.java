package t3h.bigproject.controller.adminEndPoint.filter;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "Get all resort by City ID, extension ID, starCount ID (Filter function)")
    @PostMapping("/resort/filter")
    public ResponseEntity<?> getAllResortByEntensionIdAndCityId(
            @RequestBody ExtensionCityParam extensionCity) {
        Object res = resortExtensionService.getAllResortByEntensionIdAndCityId(extensionCity);
        return ResponseEntity.ok(res);
    }
}
