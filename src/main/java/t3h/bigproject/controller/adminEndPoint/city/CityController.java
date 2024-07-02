package t3h.bigproject.controller.adminEndPoint.city;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.CityDto;
import t3h.bigproject.repository.CityRepository;
import t3h.bigproject.service.CityService;
import t3h.bigproject.service.CountryService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/backend/city")
public class CityController {
    @Autowired
    CityService cityService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    CountryService countryService;

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<String> list(@RequestParam(required = false) String name,
                        Model model) {
        List<CityDto> danhsach = cityService.getAll(name);
        JSONArray jsonArray = new JSONArray();
        for (CityDto cityDto : danhsach){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", cityDto.getId());
            jsonObject.put("countryId", cityDto.getCountryId());
            jsonObject.put("name", cityDto.getName());
            jsonObject.put("imageName", cityDto.getImageName());
            jsonArray.add(jsonObject);
        }
        return ResponseEntity.ok()
                .body(JSONValue.toJSONString(jsonArray));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id,
                  Model model, RedirectAttributes redirectAttributes) {
        cityService.delete(id);
        return ResponseEntity.ok("Delete Success!");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detail(@PathVariable Long id, Model model) {
        Object p = cityService.getDetailById(id);
        model.addAttribute("cityDto", p);
        Object danhsach = countryService.getAll(null);
        model.addAttribute("listCountry", danhsach);
        return "/backend/city/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String add(Model model) {
        CityDto b = new CityDto();
        model.addAttribute("cityDto", b);
        Object danhsach = countryService.getAll(null);
        model.addAttribute("listCountry", danhsach);
        return "/backend/city/create.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String save(@Valid @ModelAttribute CityDto cityDto, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors())
            return "/backend/city/create.html";
        Long id = cityDto.getId();

        // LƯU TÊN ẢNH
        // if (cityDto.getFileImage() != null && !cityDto.getFileImage().isEmpty()) {
        // cityDto.setImageName(fileUtils.saveFile(cityDto.getFileImage(), "city\\"));
        // }

        if (cityDto.getId() == null) {
            // ProductsDto produceDto = productsService.getDetailById(productsDto.getId());
            // if (produceDto != null) {
            // model.addAttribute("message", "đã tồn tại");
            // return "/products/create.html";
            // }
            cityService.add(cityDto);
            id = cityDto.getId();
            msg = "Tạo mới";
        } else {
            result = cityService.update(cityDto);
            msg = "Cập nhật";
        }
        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "/backend/city/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " city " + id + " thành công");
        return "redirect:/backend/city/" + id;
    }
}
