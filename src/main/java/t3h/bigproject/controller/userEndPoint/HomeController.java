package t3h.bigproject.controller.userEndPoint;

import io.swagger.v3.oas.annotations.Operation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.*;
import t3h.bigproject.entities.UserEntity;
import t3h.bigproject.entities.VerificationTokenEntity;
import t3h.bigproject.event.OnRegistrationSuccessEvent;
import t3h.bigproject.repository.UserRepository;
import t3h.bigproject.service.*;
import t3h.bigproject.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@RestController
public class HomeController {

    @Autowired
    CityService cityService;

    @Autowired
    ExtensionService extensionService;

    @Autowired
    ResortService resortService;

    @Autowired
    ResortImageService resortImageService;

    @Autowired
    RoomService roomService;

    @Autowired
    ResortExtensionService resortExtensionService;

    @Autowired
    BillService billService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

//    @GetMapping(value = { "home", "/" })
//    ResponseEntity<String> home(Model model) {
//        List<CityDto> vnCityList = cityService.getAllByCountryId((long) 1);;
//        JSONArray vnCityJSONList = new JSONArray();
//        for (CityDto cityDto : vnCityList){
//            JSONObject vnCityJSON = new JSONObject();
//            vnCityJSON.put("id", cityDto.getId());
//            vnCityJSON.put("countryId", cityDto.getCountryId());
//            vnCityJSON.put("name", cityDto.getName());
//            vnCityJSON.put("imageName", cityDto.getImageName());
//            vnCityJSONList.add(vnCityJSON);
//        }
//
//        List<CityDto> qtCityList = cityService.getAllByCountryIdIsNot((long) 1);
//        JSONArray qtCityJSONList = new JSONArray();
//        for (CityDto cityDto : qtCityList){
//            JSONObject qtCityJSON = new JSONObject();
//            qtCityJSON.put("id", cityDto.getId());
//            qtCityJSON.put("countryId", cityDto.getCountryId());
//            qtCityJSON.put("name", cityDto.getName());
//            qtCityJSON.put("imageName", cityDto.getImageName());
//            qtCityJSONList.add(qtCityJSON);
//        }
//
//        return new ResponseEntity<String>(JSONValue.toJSONString(result), HttpStatus.OK);
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/city/{id}")
//    public String city(@PathVariable Long id,
//                       Model model,
//                       @Param("keyword") String keyword) {
//
//        Object city = cityService.getDetailById(id);
//        model.addAttribute("cityDto", city);
//        Object extensionList = extensionService.getAll(null);
//        model.addAttribute("extensionList", extensionList);
//        Object resortList = resortService.getAllByCityIdAndNameContaining(id, keyword);
//        model.addAttribute("resortList", resortList);
//        model.addAttribute("keyword", keyword);
//
//        return "frontend/city.html";
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/resort/{id}")
//    public String resort(@PathVariable Long id, Model model) {
//        Object resortDto = resortService.getDetailById(id);
//        model.addAttribute("resortDto", resortDto);
//        Object roomDtoList = roomService.getAllByResortId(id);
//        model.addAttribute("roomDtoList", roomDtoList);
//        Object resortExtensionList = resortExtensionService.getAllByResortId(id);
//        model.addAttribute("extensionList", resortExtensionList);
//        List<ResortDto> resortList = resortService.getAllByIdIsNot(id, ((ResortDto) resortDto).getCityId());
//        while (resortList.size() > 15) {
//            resortList.remove(0);
//        }
//        model.addAttribute("resortList", resortList);
//
//        return "frontend/resort.html";
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/thankyou")
//    public String thankyou(Model model) {
//        return "frontend/thankyou.html";
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/sorry")
//    public String sory(Model model) {
//        return "frontend/sorry.html";
//    }

//    @GetMapping(value = { "login" })
//    public String login(Model model) {
//        return "backend/login.html";
//    }
//
//    @GetMapping(value = { "signup" })
//    public String signup(Model model) {
//        UserDto userDto = new UserDto();
//        model.addAttribute("userDto", userDto);
//        return "backend/signup.html";
//    }

    @Operation(summary = "Register new user")
    @RequestMapping(method = RequestMethod.POST, value = "/registerHandle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> registerHandle(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult,
                                     WebRequest request,
                                     Model model,
                                     RedirectAttributes redirectAttributes) throws IOException {
        String msg = "Create";

        if (!Objects.equals(userDto.getPassword(), userDto.getRePassword())) {
            return new ResponseEntity<>("Password Mismatch!", HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>("Binding Error!", HttpStatus.BAD_REQUEST);
        }
        Long id = userDto.getId();

        if (!Objects.equals(userService.getDetailByEmail(userDto.getEmail()),null)) {
            return new ResponseEntity<>("Account Available!", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = userService.addUser(userDto);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationSuccessEvent(userEntity, appUrl));
        } catch (Exception re) {
            re.printStackTrace();
            // throw new Exception("Error while sending confirmation email");
        }

        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

//    @RequestMapping("/403")
//    public String accessDenied(Model model) {
//        model.addAttribute("message", "Bạn không có quyền truy cập");
//        return "errors/pages-error-403.html";
//    }

    @Operation(summary = "Comfirming registration token, validate account")
    @GetMapping("/confirmRegistration")
    public ResponseEntity<?> confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        VerificationTokenEntity verificationTokenEntity = userService.getVerificationToken(token);
        if (verificationTokenEntity == null) {
            return new ResponseEntity<>("Truy cập bị hạn chế", HttpStatus.FORBIDDEN);
        }
        UserEntity userEntity = verificationTokenEntity.getUserEntity();
        Calendar calendar = Calendar.getInstance();
        if ((verificationTokenEntity.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return new ResponseEntity<>("Link xác nhận hết hạn", HttpStatus.FORBIDDEN);
        }

        userEntity.setStatus((long) 1);
        userRepository.save(userEntity);

        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }
}
