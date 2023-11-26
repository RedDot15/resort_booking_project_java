package t3h.bigproject.controller.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.UserDto;
import t3h.bigproject.service.UserService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/backend/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "")
    String listUser(@RequestParam(required = false) String email,
            Model model) {
        // Object user = userService.get
        Object danhsach = userService.getAll(email);
        model.addAttribute("list", danhsach);
        return "/backend/user/listUser.html";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String newPage(Model model) {
        UserDto s = new UserDto();
        model.addAttribute("userDto", s);
        return "/backend/user/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detailUser(@PathVariable Long id, Model model) {
        Object u = userService.getDetail(id);
        model.addAttribute("userDto", u);
        return "/backend/user/create.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String addUser(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";
        if (!Objects.equals(userDto.getPassword(), userDto.getRePassword())) {
            bindingResult.rejectValue("rePassword", "error.userDto", "Mật khẩu không trùng khớp");
        }
        if (bindingResult.hasErrors())
            return "/backend/user/create.html";
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
            result = userService.updateUser(userDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " thất bại");
            return "/backend/user/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " tài khoản " + id + " thành công");
        return "redirect:/backend/user/" + id;
    }
}
