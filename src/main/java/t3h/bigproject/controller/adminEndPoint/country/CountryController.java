package t3h.bigproject.controller.adminEndPoint.country;

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
import t3h.bigproject.service.CountryService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/backend/country")
public class CountryController {
    // hello
    @Autowired
    CountryService countryService;

    @Autowired
    FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<?> list(@RequestParam(required = false) String name,
                        Model model){
        List<CountryDto> countryDtoList = countryService.getAll(name);
        return new ResponseEntity<>(countryDtoList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detail(@PathVariable Long id, Model model) {
        CountryDto countryDto = countryService.getDetailById(id);
        if (countryDto == null){
            String apiErr = "Not Found Country ID: " + id;
            return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(countryDto, HttpStatus.OK);
        }
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String add(Model model) {
//        CountryDto b = new CountryDto();
//        model.addAttribute("countryDto", b);
//        return "/backend/country/create.html";
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> save(/* @Valid @ModelAttribute */ @RequestBody CountryDto countryDto,
                                                             BindingResult bindingResult,
                                                             Model model,
                                                             RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = countryDto.getId();

        if (countryDto.getId() == null) {
            result = countryService.add(countryDto);
            id = countryDto.getId();
            msg = "Create";
        } else {
            result = countryService.update(countryDto);
            msg = "Update";
        }

        if (result == null) {
            return new ResponseEntity<>(msg + "Country Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        CountryDto countryDto = countryService.getDetailById(id);
        if (countryDto == null){
            return new ResponseEntity<>("Not Found Country ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            countryService.delete(id);
            return new ResponseEntity<>("Delete Country ID: " + id + " Success!", HttpStatus.OK);
        }
    }

}
