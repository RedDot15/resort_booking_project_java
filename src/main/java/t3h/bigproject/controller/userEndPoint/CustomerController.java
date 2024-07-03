package t3h.bigproject.controller.userEndPoint;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.CityDto;
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.service.UserService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;

@Controller
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @RequestMapping(method = RequestMethod.GET, value = "/profile/{id}")
//    public String profile(@PathVariable Long id, Model model) {
//        UserDto s = userService.getDetail(id);
//        model.addAttribute("userDto", s);
//        return "frontend/profile.html";
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/change-password/{id}")
//    public String changePassword(@PathVariable Long id, Model model) {
//        UserDto s = userService.getDetail(id);
//        model.addAttribute("userDto", s);
//        return "frontend/changePassword.html";
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/infoChange", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> registerHandle(/*@Valid @ModelAttribute*/ @RequestBody UserDto userDto,
                                                                BindingResult bindingResult,
                                                                Model model,
                                                                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = userDto.getId();

        // LƯU TÊN ẢNH
        if (userDto.getFileImage() != null && !userDto.getFileImage().isEmpty()) {
            userDto.setAvatarImg(fileUtils.saveFile(userDto.getFileImage(), "user\\"));
        }

        if (userDto.getId() == null) {
            userService.addUser(userDto);
            msg = "Create";
        } else {
            result = userService.updateUserWithoutEncode(userDto);
            msg = "Update";
        }

        if (result == null) {
            return new ResponseEntity<>(msg + " User Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/passwordChange", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> changingPasswordHandle(/*@Valid @ModelAttribute*/ UserDto userDto, BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        if (!Objects.equals(userDto.getNewPassword(), userDto.getRePassword())) {
           return new ResponseEntity<>("Password Mismatch!", HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors())
            if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = userDto.getId();

        if (passwordEncoder.matches(userDto.getOldPassword(),userDto.getPassword())){
            userDto.setPassword(userDto.getNewPassword());
            result = userService.updateUser(userDto);
        } else{
            return new ResponseEntity<>("Account/Password Not Available!", HttpStatus.BAD_REQUEST);
        }

        if (result == null){
            return new ResponseEntity<>("Update Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Password Change Success!", HttpStatus.OK);
    }
}
