package bauction.web.controllers;

import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.services.contracts.UserService;
import bauction.services.contracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/emails")
public class EmailController extends BaseController {
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public EmailController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/write/{id}")
    public ModelAndView writeEmail(@PathVariable(name = "id") String recipientId,
                                   @Param(value = "auctionName") String auctionName, ModelAndView modelAndView){
        String partnerUsername = this.userService.findUserById(recipientId).getUsername();
        modelAndView.addObject("recipientId" ,recipientId);
        modelAndView.addObject("auctionName" ,auctionName);
        modelAndView.addObject("partnerUsername" ,partnerUsername);
        modelAndView.setViewName("comments/send-email");
        return modelAndView;
    }

    @PostMapping("/write/{id}")
    public ModelAndView writeEmailPost(@PathVariable(name = "id") String  recipientId,
                                       @Param(value = "subject") String subject,
                                       @Param(value = "content") String content,
                                       RedirectAttributes redirectAttributes, ModelAndView modelAndView){
        UserServiceModel recipient = this.userService.findUserById(recipientId);

        if(this.emailService.sendEmail(recipient.getEmail(),subject,content)){
            redirectAttributes.addFlashAttribute("message",
                    String.format("Email to %s sent successfully!", recipient.getUsername()));
        }else {
            redirectAttributes.addFlashAttribute("message",
                    String.format("Email to %s failed!", recipient.getUsername()));
        }
        modelAndView.setViewName("redirect:/users/profile/"+super.getLoggedInUserId());
        return modelAndView;
    }
}
