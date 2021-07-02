package softuniBlog.controller;

import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.ResetPasswordBindingModel;
import softuniBlog.entity.User;
import softuniBlog.service.UserService;
import softuniBlog.utility.Utility;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    private JavaMailSender javaMailSender;
    private UserService userService;

    public ForgotPasswordController(JavaMailSender javaMailSender,
                                    UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        model.addAttribute("view", "user/forgotPassword");

        return "base-layout";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordProcess(Model model, HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            // String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            // String resetPasswordLink = baseUrl + "/resetPassword?token=" + token;
            String resetPasswordLink = Utility.getSiteURL(request) + "/resetPassword?token=" + token;
            this.sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent you a reset password link. Please check you email.");

        }
        catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "redirect:/forgotPassword";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("contact@softuniblog.com", "SoftUni Support");
            helper.setTo(recipientEmail);

            String subject = "Here's the link to reset your password";

            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";

            helper.setSubject(subject);

            helper.setText(content, true);

            javaMailSender.send(message);
    }


    @GetMapping("/resetPassword")
    public String resetPassword(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        model.addAttribute("view", "user/resetPassword");

        return "base-layout";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordProcess (HttpServletRequest request, Model model,
                                        ResetPasswordBindingModel resetPasswordBindingModel) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);

        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        else if (!StringUtils.isEmpty(resetPasswordBindingModel.getNewPassword())
                && !StringUtils.isEmpty(resetPasswordBindingModel.getConfirmNewPassword())) {
            if (resetPasswordBindingModel.getNewPassword().equals(resetPasswordBindingModel.getConfirmNewPassword())) {
                userService.updatePassword(user, resetPasswordBindingModel.getNewPassword());

                model.addAttribute("message", "You have successfully changed your password.");
            }
            else {
                model.addAttribute("message", "Passwords do not match.");
            }
        }

        model.addAttribute("view", "user/resetPassword");

        return "base-layout";
    }
}
