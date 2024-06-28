package t3h.bigproject.controller.userEndPoint;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @RequestMapping(method = RequestMethod.GET, value = "/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        UserDto s = userService.getDetail(id);
        model.addAttribute("userDto", s);
        return "frontend/profile.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/change-password/{id}")
    public String changePassword(@PathVariable Long id, Model model) {
        UserDto s = userService.getDetail(id);
        model.addAttribute("userDto", s);
        return "frontend/changePassword.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/infoChange", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String registerHandle(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";
        if (bindingResult.hasErrors())
            return "/frontend/profile.html";
        Long id = userDto.getId();

        // LƯU TÊN ẢNH
        if (userDto.getFileImage() != null && !userDto.getFileImage().isEmpty()) {
            userDto.setAvatarImg(fileUtils.saveFile(userDto.getFileImage(), "user\\"));
        }

        if (userDto.getId() == null) {
            userService.addUser(userDto);
            id = userDto.getId();
            msg = "Tạo mới";
        } else {
            result = userService.updateUserWithoutEncode(userDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "redirect:/profile/" + userDto.getId();
        }
        redirectAttributes.addFlashAttribute("message", "cập nhật tài khoản thành công");
        return "redirect:/profile/" + userDto.getId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/passwordChange", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String changingPasswordHandle(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";
        if (!Objects.equals(userDto.getNewPassword(), userDto.getRePassword())) {
            bindingResult.rejectValue("rePassword", "error.userDto", "Mật khẩu không trùng khớp");
        }
        if (bindingResult.hasErrors())
            return "/frontend/changePassword.html";
        Long id = userDto.getId();
        if (passwordEncoder.matches(userDto.getOldPassword(),userDto.getPassword())){
            userDto.setPassword(userDto.getNewPassword());
            result = userService.updateUser(userDto);
        }
        else{
            redirectAttributes.addFlashAttribute("message", "Mật khẩu gốc không đúng");
            return "redirect:/change-password/" + userDto.getId();
        }
        msg = "Cập nhật";
        if (Objects.equals(result, null)){
            redirectAttributes.addFlashAttribute("message", msg + " fail");
            return "redirect:/change-password/" + userDto.getId();

        }
        redirectAttributes.addFlashAttribute("message", "cập nhật tài khoản thành công");
        return "redirect:/change-password/" + userDto.getId();
    }
}
