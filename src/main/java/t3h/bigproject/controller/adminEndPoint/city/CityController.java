package t3h.bigproject.controller.adminEndPoint.city;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.CityDto;
import t3h.bigproject.dto.CountryDto;
import t3h.bigproject.repository.CityRepository;
import t3h.bigproject.service.CityService;
import t3h.bigproject.service.CountryService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/backend/city")
public class CityController {
    @Autowired
    CityService cityService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    CountryService countryService;

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<List<CityDto>> allCityList(@RequestParam(required = false) String name, Model model) {
        List<CityDto> danhsach = cityService.getAll(name);
        return new ResponseEntity<>(danhsach, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vn")
    ResponseEntity<List<CityDto>> allVNCitylist( Model model) {
        List<CityDto> vnCityList = cityService.getAllByCountryId((long) 1);
        return new ResponseEntity<>(vnCityList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/qt")
    ResponseEntity<List<CityDto>> allQTCitylist( Model model) {
        List<CityDto> qtCityList = cityService.getAllByCountryIdIsNot((long) 1);
        return new ResponseEntity<>(qtCityList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detail(@PathVariable Long id, Model model) {
        CityDto cityDto = cityService.getDetailById(id);
        if (cityDto == null){
            String apiErr = "Not Found City ID: " + id;
            return new ResponseEntity<String>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(cityDto, HttpStatus.OK);
        }
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String add(Model model) {
//        CityDto b = new CityDto();
//        model.addAttribute("cityDto", b);
//        Object danhsach = countryService.getAll(null);
//        model.addAttribute("listCountry", danhsach);
//        return "/backend/city/create.html";
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> save(/* @Valid @ModelAttribute */ @RequestBody CityDto cityDto,
                                                        BindingResult bindingResult,
                                                        Model model,
                                                        RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<String>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = cityDto.getId();

        if (id == null) {
            result = cityService.add(cityDto);
            msg = "Create";
        } else {
            result = cityService.update(cityDto);
            msg = "Update";
        }

        if (result == null) {
            return new ResponseEntity<String>(msg + " City Fail!", HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<CityDto>(cityDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        CityDto cityDto = cityService.getDetailById(id);
        if (cityDto == null){
            return new ResponseEntity<>("Not Found City ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            cityService.delete(id);
            return new ResponseEntity<>("Delete City ID: " + id + " Success!", HttpStatus.OK);
        }
    }
}
