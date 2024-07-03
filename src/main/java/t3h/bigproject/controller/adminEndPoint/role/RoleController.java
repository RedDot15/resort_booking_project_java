package t3h.bigproject.controller.adminEndPoint.role;

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
import t3h.bigproject.dto.RoleDto;
import t3h.bigproject.service.RoleService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/backend/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<?> list(@RequestParam(required = false) String name,
                                Model model){
        List<RoleDto> roleDtoList = roleService.getAll(name);
        return new ResponseEntity<>(roleDtoList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detail(@PathVariable Long id, Model model) {
        RoleDto roleDto = roleService.getDetailById(id);
        if (roleDto == null){
            String apiErr = "Not Found Role ID: " + id;
            return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(roleDto, HttpStatus.OK);
        }
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String add(Model model) {
//        RoleDto b = new RoleDto();
//        model.addAttribute("roleDto", b);
//        return "/backend/role/create.html";
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> save(/* @Valid @ModelAttribute */ @RequestBody RoleDto roleDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = roleDto.getId();

        if (id == null) {
            result = roleService.add(roleDto);
            msg = "Create";
        } else {
            result = roleService.update(roleDto);
            msg = "Update";
        }
        if (result == null) {
            return new ResponseEntity<>(msg + " Role Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        RoleDto roleDto = roleService.getDetailById(id);
        if (roleDto == null){
            return new ResponseEntity<>("Not Found Role ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            roleService.delete(id);
            return new ResponseEntity<>("Delete Role ID: " + id + " Success!", HttpStatus.OK);
        }
    }


}
