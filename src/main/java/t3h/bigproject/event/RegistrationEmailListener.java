package t3h.bigproject.event;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.mail.MailSender;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.UserEntity;
import t3h.bigproject.service.BillService;
import t3h.bigproject.service.UserService;

import java.util.UUID;


@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {
    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationSuccessEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationSuccessEvent event) {
        UserEntity userEntity = event.getUserEntity();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(userEntity,token);

        String recipient = userEntity.getEmail();
        String subject = "Registration Confirmation";
        String url
                = event.getAppUrl() + "/confirmRegistration?token=" + token;
        String message = "Thank you for registering. Please click on the below link to activate. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(message + "http://localhost:8888" + url);
        System.out.println(url);
        mailSender.send(email);

    }
}
