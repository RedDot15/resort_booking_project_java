package t3h.bigproject.controller.backend.resort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.ExtensionDto;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortDto;
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
    String list(@RequestParam(required = false) String name,
                Model model){
        Object danhsach = resortService.getAll(name);
        model.addAttribute("list", danhsach);
        return"/backend/resort/listResort.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    String delete(@PathVariable Long id,
                  Model model, RedirectAttributes redirectAttributes) {
        resortService.delete(id);
        return "redirect:/backend/resort/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detail(@PathVariable Long id, Model model) {
        Object p = resortService.getDetailById(id);
        model.addAttribute("resortDto", p);
        List<ExtensionDto> extensionDtoList = extensionService.getAll(null);
        model.addAttribute("extensionList", extensionDtoList);
        Object danhsach = cityService.getAll(null);
        model.addAttribute("listCity", danhsach);
        return "/backend/resort/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String add(Model model) {
        ResortDto b = new ResortDto();
        model.addAttribute("resortDto", b);
        List<ExtensionDto> extensionDtoList = extensionService.getAll(null);
        model.addAttribute("extensionList", extensionDtoList);
        Object danhsach = cityService.getAll(null);
        model.addAttribute("listCity", danhsach);
        return "/backend/resort/create.html";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String save(@Valid @ModelAttribute ResortDto resortDto, BindingResult bindingResult,
                Model model,
                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return "/backend/resort/create.html";
        Long id = resortDto.getId();

        if (resortDto.getId() == null) {
//            ProductsDto produceDto = productsService.getDetailById(productsDto.getId());
//            if (produceDto != null) {
//                model.addAttribute("message", "đã tồn tại");
//                return "/products/create.html";
//            }
            resortService.add(resortDto);
            id = resortDto.getId();
            msg = "Tạo mới";
        } else {
            result = resortService.update(resortDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "/backend/resort/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " resort " + id + " thành công");
        return "redirect:/backend/resort/" + id;
    }
}
