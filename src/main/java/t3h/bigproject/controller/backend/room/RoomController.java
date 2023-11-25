package t3h.bigproject.controller.backend.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.service.ResortService;
import t3h.bigproject.service.RoomService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/backend/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ResortService resortService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    String list(@RequestParam(required = false) String name,
                Model model){
        Object danhsach = roomService.getAll(name);
        model.addAttribute("list", danhsach);
        return"/backend/room/listRoom.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    String detail(@PathVariable Long id, Model model) {
        Object p = roomService.getDetailById(id);
        model.addAttribute("roomDto", p);
        Object danhsach = resortService.getAll(null);
        model.addAttribute("listResort", danhsach);
        return "/backend/room/create.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    String add(Model model) {
        RoomDto b = new RoomDto();
        model.addAttribute("roomDto", b);
        Object danhsach = resortService.getAll(null);
        model.addAttribute("listResort", danhsach);
        return "/backend/room/create.html";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String save(@Valid @ModelAttribute RoomDto roomDto, BindingResult bindingResult,
                Model model,
                RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return "/backend/room/create.html";
        Long id = roomDto.getId();

        
        if (roomDto.getId() == null) {
//            ProductsDto produceDto = productsService.getDetailById(productsDto.getId());
//            if (produceDto != null) {
//                model.addAttribute("message", "đã tồn tại");
//                return "/products/create.html";
//            }
            roomService.add(roomDto);
            id = roomDto.getId();
            msg = "Tạo mới";
        } else {
            result = roomService.update(roomDto);
            msg = "Cập nhật";
        }

        if (Objects.equals(result, 0)) {
            model.addAttribute("message", msg + " fail");
            return "/backend/room/create.html";
        }
        redirectAttributes.addFlashAttribute("message", msg + " room " + id + " thành công");
        return "redirect:/backend/room/" + id;
    }
}
