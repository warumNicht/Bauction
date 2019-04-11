package bauction.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController extends BaseController {

    @GetMapping("/unauthorized")
    public String unauthorized(){
        return "unauthorized";
    }

}
