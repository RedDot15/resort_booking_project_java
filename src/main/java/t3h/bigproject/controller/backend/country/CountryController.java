package t3h.bigproject.controller.backend.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.CountryDto;
import t3h.bigproject.dto.CountryDto;
import t3h.bigproject.service.CountryService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/backend/country")
public class CountryController {
    // hello
    @Autowired
    CountryService countryService;

    @Autowired
    FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "")
    String list(@RequestParam(required = false) String name,
                Model model){
        Object danhsach = countryService.getAll(name);
        model.addAttribute("list", danhsach);
        return"/backend/country/listCountry.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detail(@PathVariable Long id, Model model) {
        Object p = countryService.getDetailById(id);
        model.addAttribute("countryDto", p);
        return "/backend/country/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String add(Model model) {
        CountryDto b = new CountryDto();
        model.addAttribute("countryDto", b);
        return "/backend/country/create.html";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String save(@Valid @ModelAttribute CountryDto countryDto, BindingResult bindingResult,
                Model model,
                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";
         

        // My name is Quang Minh


        if (bindingResult.hasErrors()) return "/backend/country/create.html";
        Long id = countryDto.getId();

        if (countryDto.getId() == null) {
//            ProductsDto produceDto = productsService.getDetailById(productsDto.getId());
//            if (produceDto != null) {
//                model.addAttribute("message", "đã tồn tại");
//                return "/products/create.html";
//            }
            countryService.add(countryDto);
            id = countryDto.getId();
            msg = " tao moi";
        } else {
            result = countryService.update(countryDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "/backend/country/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " country " + id + " thành công");
        return "redirect:/backend/country/" + id;
    }
}
