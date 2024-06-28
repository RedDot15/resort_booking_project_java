package t3h.bigproject.controller.adminEndPoint.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.ExtensionDto;
import t3h.bigproject.service.ExtensionService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/backend/extension")
public class ExtensionController {

    @Autowired
    ExtensionService extensionService;

    @Autowired
    FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "")
    String list(@RequestParam(required = false) String name,
                Model model){
        Object danhsach = extensionService.getAll(name);
        model.addAttribute("list", danhsach);
        return"/backend/extension/listExtension.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    String delete(@PathVariable Long id,
                  Model model, RedirectAttributes redirectAttributes) {
        extensionService.delete(id);
        return "redirect:/backend/extension/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detail(@PathVariable Long id, Model model) {
        Object p = extensionService.getDetailById(id);
        model.addAttribute("extensionDto", p);
        return "/backend/extension/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String add(Model model) {
        ExtensionDto b = new ExtensionDto();
        model.addAttribute("extensionDto", b);
        return "/backend/extension/create.html";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String save(@Valid @ModelAttribute ExtensionDto extensionDto, BindingResult bindingResult,
                Model model,
                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return "/backend/extension/create.html";
        Long id = extensionDto.getId();

        if (extensionDto.getId() == null) {
//            ProductsDto produceDto = productsService.getDetailById(productsDto.getId());
//            if (produceDto != null) {
//                model.addAttribute("message", "đã tồn tại");
//                return "/products/create.html";
//            }
            extensionService.add(extensionDto);
            id = extensionDto.getId();
            msg = "Tạo mới";
        } else {
            result = extensionService.update(extensionDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "/backend/extension/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " extension " + id + " thành công");
        return "redirect:/backend/extension/" + id;
    }
}
