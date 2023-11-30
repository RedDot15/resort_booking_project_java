package t3h.bigproject.controller.frontend;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.dto.ResortDto;
import t3h.bigproject.dto.ResortImageDto;
import t3h.bigproject.dto.RoomDto;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.RoomEntity;
import t3h.bigproject.entities.VerificationTokenEntity;
import t3h.bigproject.event.OnRegistrationSuccessEvent;
import t3h.bigproject.repository.BillRepository;
import t3h.bigproject.repository.RoomRepository;
import t3h.bigproject.service.BillService;
import t3h.bigproject.service.ResortImageService;
import t3h.bigproject.service.ResortService;
import t3h.bigproject.service.RoomService;

import javax.validation.Valid;
import java.util.Calendar;

@Controller
public class BillController {

    @Autowired
    RoomService roomService;

    @Autowired
    ResortService resortService;

    @Autowired
    ResortImageService resortImageService;

    @Autowired
    BillService billService;

    @Autowired
    BillRepository billRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @RequestMapping(method = RequestMethod.GET, value = "/backend/bill")
    String list(Model model){
        Object danhsach = billService.getAll();
        model.addAttribute("list", danhsach);
        return"/backend/bill/listBill.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/backend/updateBill/{id}")
    String updateBill(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        billService.updateBill(id);
        return "redirect:/backend/bill";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/booking/{roomid}")
    public String showFormBooking(@PathVariable Long roomid , Model model){
        BillDto billDto = new BillDto();
        model.addAttribute("roomid",roomid);
        model.addAttribute("billDto",billDto);
        RoomDto roomDto = roomService.getDetailById(roomid);
        model.addAttribute("roomDto",roomDto);
        ResortDto resortDto = resortService.getDetailById(roomDto.getResortId());
        model.addAttribute("resortDto",resortDto);
        ResortImageDto resortImageDto = resortImageService.getFirstByResortId(roomDto.getResortId());
        model.addAttribute("resortImageDto",resortImageDto);
        return "frontend/booking.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/booking/{roomid}")
    public String booking(@Valid @ModelAttribute("billDto") BillDto billDto, BindingResult result,
                          WebRequest request, Model model, @PathVariable Long roomid){
//        RoomDto roomDto = roomService.getDetailById(id);
//        model.addAttribute("roomDto",roomDto);
//        ResortDto resortDto = resortService.getDetailById(roomDto.getResortId());
//        model.addAttribute("resortDto",resortDto);
//        ResortImageDto resortImageDto = resortImageService.getFirstByResortId(roomDto.getResortId());
//        model.addAttribute("resortImageDto",resortImageDto);
        if (result.hasErrors()){
            return "frontend/booking.html";
        }
        BillEntity billEntity = billService.add(billDto);
        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationSuccessEvent(billEntity, appUrl));
        }catch(Exception re) {
            re.printStackTrace();
//			throw new Exception("Error while sending confirmation email");
        }
        return "frontend/thankyou.html";
    }

    @GetMapping("/confirmRegistration")
    public String confirmRegistration(WebRequest request, Model model,@RequestParam("token") String token) {
        VerificationTokenEntity verificationTokenEntity = billService.getVerificationToken(token);
        if(verificationTokenEntity == null) {
            String message = "Truy cập bị hạn chế";
            model.addAttribute("message", message);
            return "errors/404.html";
        }
        BillEntity billEntity = verificationTokenEntity.getBillEntity();
        Calendar calendar = Calendar.getInstance();
        if((verificationTokenEntity.getExpiryDate().getTime()-calendar.getTime().getTime())<=0) {
            String message = "Link xác nhận hết hạn";
            model.addAttribute("message", message);
            return "errors/404.html";
        }
        billEntity.setStatusId((long)2);
        RoomDto roomDto = roomService.getDetailById(billEntity.getRoomId());
        RoomEntity roomEntity = new RoomEntity();
        BeanUtils.copyProperties(roomDto,roomEntity);
        roomEntity.setStatus("busy");
        roomRepository.save(roomEntity);
        billRepository.save(billEntity);
        model.addAttribute("message", "Bạn đã đăng ký giữ phòng thành công");
        return "errors/404.html";
    }
}
