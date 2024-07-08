package t3h.bigproject.controller.adminEndPoint.user;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.catalina.User;
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
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.repository.UserRepository;
import t3h.bigproject.service.UserService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/backend/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "List all user available or by Email")
    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<?> listUser(@RequestParam(required = false) String email,
                                    Model model) {
        List<UserDto> userDtoList = userService.getAll(email);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String newPage(Model model) {
//        UserDto s = new UserDto();
//        model.addAttribute("userDto", s);
//        return "/backend/user/create.html";
//    }

    @Operation(summary = "Find by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detailUser(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getDetail(id);
        if (userDto == null){
            String apiErr = "Not Found User ID: " + id;
            return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }

    }

    @Operation(summary = "Create/Update")
    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> addUser(/*@Valid @ModelAttribute*/ @ModelAttribute UserDto userDto,
                                                              BindingResult bindingResult,
                                                              Model model,
                                                              RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (!Objects.equals(userDto.getPassword(), userDto.getRePassword())) {
            return new ResponseEntity<>("Password Mismatch!", HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = userDto.getId();

        // LƯU TÊN ẢNH
        if (userDto.getFileImage() != null && !userDto.getFileImage().isEmpty()) {
            userDto.setAvatarImg(fileUtils.saveFile(userDto.getFileImage(), "user\\"));
        }

        if (id == null) {
            userService.addUser(userDto);
            msg = "Create";
        } else {
            result = userService.updateUser(userDto);
            msg = "Update";
        }

        if (result == null) {
            return new ResponseEntity<>(msg + " User Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete by ID")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        UserDto userDto = userService.getDetail(id);
        if (userDto == null){
            return new ResponseEntity<>("Not Found User ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            userService.delete(id);
            return new ResponseEntity<>("Delete User ID: " + id + " Success!", HttpStatus.OK);
        }
    }

}
