package t3h.bigproject.controller.adminEndPoint.room;

import io.swagger.v3.oas.annotations.Operation;
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
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.service.ResortService;
import t3h.bigproject.service.RoomService;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/backend/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ResortService resortService;

    @Operation(summary = "List all room available")
    @RequestMapping(method = RequestMethod.GET, value = "")
    ResponseEntity<?> list(@RequestParam(required = false) String name,
                           Model model){
        List<RoomDto> roomDtoList = roomService.getAll(name);
        return new ResponseEntity<>(roomDtoList, HttpStatus.OK);
    }

    @Operation(summary = "Find by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<?> detail(@PathVariable Long id, Model model) {
        RoomDto roomDto = roomService.getDetailById(id);
        if (roomDto == null){
            String apiErr = "Not Found Room ID: " + id;
            return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        }
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/new")
//    String add(Model model) {
//        RoomDto b = new RoomDto();
//        model.addAttribute("roomDto", b);
//        Object danhsach = resortService.getAll(null);
//        model.addAttribute("listResort", danhsach);
//        return "/backend/room/create.html";
//    }

    @Operation(summary = "Create/Update")
    @RequestMapping(method = RequestMethod.POST, value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> save(/*@Valid @ModelAttribute*/ @ModelAttribute RoomDto roomDto,
                                                           BindingResult bindingResult,
                                                           Model model,
                                                           RedirectAttributes redirectAttributes) throws IOException {
        Object result = null;
        String msg = "";

        if (bindingResult.hasErrors()) return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        Long id = roomDto.getId();

        if (id == null) {
            roomService.add(roomDto);
            msg = "Create";
        } else {
            result = roomService.update(roomDto);
            msg = "Update";
        }

        if (result == null) {
            return new ResponseEntity<>(msg + " Room Fail!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    ResponseEntity<?> delete(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes){
        RoomDto roomDto = roomService.getDetailById(id);
        if (roomDto == null){
            return new ResponseEntity<>("Not Found Room ID: " + id, HttpStatus.NOT_FOUND);
        } else{
            roomService.delete(id);
            return new ResponseEntity<>("Delete Room ID: " + id + " Success!", HttpStatus.OK);
        }
    }
}
