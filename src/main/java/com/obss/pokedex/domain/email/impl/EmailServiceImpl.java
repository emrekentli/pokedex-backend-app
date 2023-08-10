import EmailDto;
import EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Component
@Log4j2
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(dto.getText());
        message.setFrom(from);
        try{
            mailSender.send(message);
        }
        catch (Exception e) {
            log.error("Error while sending email: " + e.getMessage());
        }
    }
}
