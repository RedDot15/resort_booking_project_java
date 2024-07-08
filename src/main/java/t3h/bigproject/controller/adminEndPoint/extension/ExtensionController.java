package t3h.bigproject.controller.adminEndPoint.extension;

import io.swagger.v3.oas.annotations.Operation;
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
import t3h.bigproject.service.ExtensionService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/backend/extension")
public class ExtensionController {

    @Autowired
    ExtensionService extensionService;

    @Autowired
    FileUtils fileUtils;

    @Operation(summary = "List all extension available",
            description = "List all extension available.")
    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<?> list(@RequestParam(required = false) String name,
                        Model model){
        List<ExtensionDto> extensionDtoList = extensionService.getAll(name);
        return new ResponseEntity<>(extensionDtoList, HttpStatus.OK);
    }

    @Operation(summary = "Find by ID",
            description = "Find by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detail(@PathVariable Long id, Model model) {
        ExtensionDto extensionDto = extensionService.getDetailById(id);
        if (extensionDto == null){
            String apiErr = "Not Found Extention ID: " + id;
            return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(extensionDto, HttpStatus.OK);
        }
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String add(Model model) {
//        ExtensionDto b = new ExtensionDto();
//        model.addAttribute("extensionDto", b);
//        return "/backend/extension/create.html";
//    }

    @Operation(summary = "Create/Update",
            description = "Create/Update")
    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> save(/* @Valid @ModelAttribute */ @ModelAttribute ExtensionDto extensionDto,
                                                             BindingResult bindingResult,
                                                             Model model,
                                                             RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = extensionDto.getId();

        if (extensionDto.getId() == null) {
            result = extensionService.add(extensionDto);
            msg = "Create";
        } else {
            result = extensionService.update(extensionDto);
            msg = "Update";
        }

        if (result == null) {
            return new ResponseEntity<>(msg + " Extension Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(extensionDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete by ID",
            description = "Delete by ID")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        ExtensionDto extensionDto = extensionService.getDetailById(id);
        if (extensionDto == null){
            return new ResponseEntity<>("Not Found Extension ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            extensionService.delete(id);
            return new ResponseEntity<>("Delete Extension ID: " + id + " Success!", HttpStatus.OK);
        }
    }

}
