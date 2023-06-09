package com.fu.fuatsbe.serviceImp;

import com.fu.fuatsbe.DTO.InviteReapplyDTO;
import com.fu.fuatsbe.constant.account.AccountErrorMessage;
import com.fu.fuatsbe.entity.EmailSchedule;
import com.fu.fuatsbe.exceptions.NotFoundException;
import com.fu.fuatsbe.exceptions.PermissionException;
import com.fu.fuatsbe.repository.AccountRepository;
import com.fu.fuatsbe.repository.CvRepository;
import com.fu.fuatsbe.repository.EmailScheduleRepository;
import com.fu.fuatsbe.repository.VerificationRepository;
import com.fu.fuatsbe.service.EmailScheduleService;
import com.fu.fuatsbe.service.EmailService;
import com.fu.fuatsbe.service.NotificationService;
import com.fu.fuatsbe.service.VerificationTokenService;
import com.fu.fuatsbe.constant.common.CommonMessage;
import com.fu.fuatsbe.entity.Account;
import com.fu.fuatsbe.entity.VerificationToken;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {
    private final AccountRepository accountRepository;
    private final CvRepository cvRepository;

    private final VerificationRepository verificationRepository;
    private final VerificationTokenService verificationTokenService;
    private final EmailScheduleRepository emailScheduleRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${ats.forgot-password}")
    private String forgotPasswordLink;

    private Timestamp calculateExpiryTime(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }
    @Override
    public void sendEmailInterview(String email, String title, String name, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText("Thân gửi " + name + ",\n" + content);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailToGetBackPassword(String email) throws MessagingException {
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException(AccountErrorMessage.ACCOUNT_NOT_FOUND));
        String token = UUID.randomUUID().toString();
        Optional<VerificationToken> verificationToken = verificationRepository.findByAccount(account);
        if(verificationToken.isPresent()){
            verificationToken.get().setToken(token);
            verificationToken.get().setExpiryDate(calculateExpiryTime(60));
            verificationRepository.save(verificationToken.get());
        }
        else { verificationTokenService.save(account,token, true);}
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(account.getEmail());
        mimeMessageHelper.setSubject("Reset password from ATS");
        mimeMessageHelper.setText("Lấy lại mật khẩu thành công, vui lòng nhấn đường link bên dưới để đặt lại mật khẩu  \n"+forgotPasswordLink
                +account.getEmail()+"/"
                +token
                +"\nChúc bạn một ngày làm việc tốt lành, \n" +
                "\n" +
                "Trân Trọng, ");
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void confirm(String email, String token) {

    }

    @Override
    public void resetPassword(String email, String token, String password) {
        Account account = accountRepository.findAccountByEmail(email).orElseThrow(() -> new NotFoundException(AccountErrorMessage.ACCOUNT_NOT_FOUND));
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        if(verificationToken.getExpiryDate().before(currentTimeStamp)){
            throw new PermissionException(CommonMessage.TOKEN_EXPIRED_EXCEPTION);
        }
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    @Override
    public void sendEmailToInviteReapply(InviteReapplyDTO invite) throws MessagingException {
        for (Integer cvId: invite.getCvIds()) {
            String email = cvRepository.getEmailByCVId(cvId);
            String content = invite.getContent() +
                    "\n"
                    +
                    "--------------------\n" +
                    "HR Department | CKHR Consulting\n" +
                    "Ground Floor, Rosana Building, 60 Nguyen Dinh Chieu, Da Kao Ward, District 1, HCMC\n" +
                    "\n" +
                    "Phone: (+8428) 7106 8279 Email: info@ckhrconsulting.vn Website: ckhrconsulting.vn";
            if(email != null){
                EmailSchedule emailSchedule = EmailSchedule.builder()
                        .email(email)
                        .title(invite.getTitle())
                        .content(content)
                        .build();
                emailScheduleRepository.save(emailSchedule);
            }
        }

    }
}
