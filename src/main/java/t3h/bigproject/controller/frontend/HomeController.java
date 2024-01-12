package t3h.bigproject.controller.frontend;

import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.*;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.RoomEntity;
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

@Controller
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

    @GetMapping(value = { "home", "/" })
    public String home(Model model) {
        Object danhsachCityVietNam = cityService.getAllByCountryId((long) 1);
        model.addAttribute("listCityVietNam", danhsachCityVietNam);
        Object danhsachCityQuocTe = cityService.getAllByCountryIdIsNot((long) 1);
        model.addAttribute("listCityQuocTe", danhsachCityQuocTe);
        return "frontend/index.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/city/{id}")
    public String city(@PathVariable Long id, Model model, @Param("keyword") String keyword) {
        Object city = cityService.getDetailById(id);
        model.addAttribute("cityDto", city);
        Object extensionList = extensionService.getAll(null);
        model.addAttribute("extensionList", extensionList);
        Object resortList = resortService.getAllByCityIdAndNameContaining(id, keyword);
        model.addAttribute("resortList", resortList);
        model.addAttribute("keyword", keyword);

        return "frontend/city.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/resort/{id}")
    public String resort(@PathVariable Long id, Model model) {
        Object resortDto = resortService.getDetailById(id);
        model.addAttribute("resortDto", resortDto);
        Object roomDtoList = roomService.getAllByResortId(id);
        model.addAttribute("roomDtoList", roomDtoList);
        Object resortExtensionList = resortExtensionService.getAllByResortId(id);
        model.addAttribute("extensionList", resortExtensionList);
        List<ResortDto> resortList = resortService.getAllByIdIsNot(id, ((ResortDto) resortDto).getCityId());
        while (resortList.size() > 15) {
            resortList.remove(0);
        }
        model.addAttribute("resortList", resortList);

        return "frontend/resort.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/thankyou")
    public String thankyou(Model model) {
        return "frontend/thankyou.html";
    }

    @GetMapping(value = { "login" })
    public String login(Model model) {
        return "backend/login.html";
    }

    @GetMapping(value = { "signup" })
    public String signup(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "backend/signup.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registerHandle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String registerHandle(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult,
            WebRequest request,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {
        String msg = "";
        if (!Objects.equals(userDto.getPassword(), userDto.getRePassword())) {
            bindingResult.rejectValue("rePassword", "error.userDto", "Mật khẩu không trùng khớp");
        }
        if (bindingResult.hasErrors())
            return "/backend/signup.html";
        Long id = userDto.getId();

        if (!Objects.equals(userService.getDetailByEmail(userDto.getEmail()),null)) {
            redirectAttributes.addFlashAttribute("message", "Tài khoản đã tồn tại");
            return "redirect:/signup";
        }

        UserEntity userEntity = new UserEntity();

        if (userDto.getId() == null) {
            userEntity = userService.addUser(userDto);
            id = userDto.getId();
            msg = "Tạo mới";
        }

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationSuccessEvent(userEntity, appUrl));
        } catch (Exception re) {
            re.printStackTrace();
            // throw new Exception("Error while sending confirmation email");
        }

        redirectAttributes.addFlashAttribute("message", "Vui lòng xác nhận email để có thể đăng nhập");
        return "redirect:/login";
    }

    @RequestMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("message", "Bạn không có quyền truy cập");
        return "errors/pages-error-403.html";
    }

    @GetMapping("/confirmRegistration")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        VerificationTokenEntity verificationTokenEntity = userService.getVerificationToken(token);
        if (verificationTokenEntity == null) {
            String message = "Truy cập bị hạn chế";
            model.addAttribute("message", message);
            return "errors/404.html";
        }
        UserEntity userEntity = verificationTokenEntity.getUserEntity();
        Calendar calendar = Calendar.getInstance();
        if ((verificationTokenEntity.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            String message = "Link xác nhận hết hạn";
            model.addAttribute("message", message);
            return "errors/404.html";
        }
        userEntity.setStatus((long) 1);

        userRepository.save(userEntity);
        model.addAttribute("message", "Tài khoản đã được kích hoạt");
        return "success/successful.html";
    }
}
