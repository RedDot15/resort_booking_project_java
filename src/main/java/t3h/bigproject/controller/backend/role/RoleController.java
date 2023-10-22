package t3h.bigproject.controller.backend.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.RoleDto;
import t3h.bigproject.dto.RoleDto;
import t3h.bigproject.service.RoleService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/backend/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "")
    String list(@RequestParam(required = false) String name,
                Model model){
        Object danhsach = roleService.getAll(name);
        model.addAttribute("list", danhsach);
        return"/backend/role/listRole.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detail(@PathVariable Long id, Model model) {
        Object p = roleService.getDetailById(id);
        model.addAttribute("roleDto", p);
        return "/backend/role/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String add(Model model) {
        RoleDto b = new RoleDto();
        model.addAttribute("roleDto", b);
        return "/backend/role/create.html";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String save(@Valid @ModelAttribute RoleDto roleDto, BindingResult bindingResult,
                Model model,
                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return "/backend/role/create.html";
        Long id = roleDto.getId();

        if (roleDto.getId() == null) {
//            ProductsDto produceDto = productsService.getDetailById(productsDto.getId());
//            if (produceDto != null) {
//                model.addAttribute("message", "đã tồn tại");
//                return "/products/create.html";
//            }
            roleService.add(roleDto);
            id = roleDto.getId();
            msg = " tao moi";
        } else {
            result = roleService.update(roleDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "/backend/role/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " role " + id + " thành công");
        return "redirect:/backend/role/" + id;
    }
}
