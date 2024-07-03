package t3h.bigproject.controller.adminEndPoint.resort;

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
import t3h.bigproject.dto.ExtensionDto;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.entities.ResortimageEntity;
import t3h.bigproject.service.CityService;
import t3h.bigproject.service.ExtensionService;
import t3h.bigproject.service.ResortService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/backend/resort")
public class ResortController {
    @Autowired
    ResortService resortService;

    @Autowired
    ExtensionService extensionService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    CityService cityService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<?> list(@RequestParam(required = false) String name,
                                Model model){
        List<ResortDto> resortDtoList = resortService.getAll(name);
        return new ResponseEntity<>(resortDtoList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filter/except-resort/{resortId}/in/{cityId}")
    ResponseEntity<?> listAllByCityIdExceptResortId(@PathVariable Long resortId,
                                                    @PathVariable Long cityId,
                                                    Model model){
        List<ResortDto> resortDtoList = resortService.getAllByIdIsNot(resortId, cityId);
        while (resortDtoList.size() > 15) {
            resortDtoList.remove(0);
        }
        return new ResponseEntity<>(resortDtoList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filter/city/{cityId}")
    ResponseEntity<?> allResortByCityIdAndNameContaining(@PathVariable Long cityId,
                                                         @RequestParam(required = true) String name,
                                                         Model model){
        List<ResortDto> resortDtoList = resortService.getAllByCityIdAndNameContaining(cityId, name);
        return new ResponseEntity<>(resortDtoList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detail(@PathVariable Long id, Model model) {
        ResortDto resortDto = resortService.getDetailById(id);
        if (resortDto == null){
            String apiErr = "Not Found Resort ID: " + id;
            return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(resortDto, HttpStatus.OK);
        }
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String add(Model model) {
//        ResortDto b = new ResortDto();
//        model.addAttribute("resortDto", b);
//        List<ExtensionDto> extensionDtoList = extensionService.getAll(null);
//        model.addAttribute("extensionList", extensionDtoList);
//        Object danhsach = cityService.getAll(null);
//        model.addAttribute("listCity", danhsach);
//        return "/backend/resort/create.html";
//    }


    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> save(/* @Valid @ModelAttribute */ @RequestBody ResortDto resortDto,
                                                             BindingResult bindingResult,
                                                             Model model,
                                                             RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<String>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = resortDto.getId();

        if (id == null) {
            result = resortService.add(resortDto);
            msg = "Create";
        } else {
            result = resortService.update(resortDto);
            msg = "Update";
        }
        if (result == null){
            return new ResponseEntity<String>(msg + " Resort Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(resortDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        ResortDto resortDto = resortService.getDetailById(id);
        if (resortDto == null){
            return new ResponseEntity<>("Not Found Resort ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            resortService.delete(id);
            return new ResponseEntity<>("Delete Resort ID: " + id + " Success!", HttpStatus.OK);
        }
    }

}
