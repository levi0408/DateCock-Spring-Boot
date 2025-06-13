package com.example.DateCock.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = "/")
    public String servemain() {

        return "servemain";
    }

    @GetMapping("/main")
    public String main(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("isMain", uri.endsWith("/main"));
        model.addAttribute("isLogin", uri.endsWith("/DateCocklog"));
        // 필요한 uri 조건마다 addAttribute 하세요
        return "main";
    }

    @GetMapping("/DateCocklog")
    public String logpagego(){

        return "DateCocklog";
    }
    @GetMapping("/game")
    public String heartgame(){

        return "game";
    }

}


