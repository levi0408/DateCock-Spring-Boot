// SessionControllerAdvice.java
package com.example.DateCock.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SessionControllerAdvice {
    //로그인 세션 보내는 곳
    @ModelAttribute
    public void addSessionAttributes(Model model, HttpSession session, HttpServletRequest request) {
        // 세션에서 값을 꺼내 모델에 넣어줌
        Object businessState = session.getAttribute("buisnessloginstate");
        Object businessName = session.getAttribute("businessname");
        Object businessnumberA = session.getAttribute("businessnumberA");
        Object personalState = session.getAttribute("personalloginstate");
        Object personalName = session.getAttribute("name");
        Object id = session.getAttribute("id");


        if (businessState != null) {
            model.addAttribute("buisnessloginstate", businessState);
            model.addAttribute("businessname", businessName);
            model.addAttribute("businessnumberA",businessnumberA);
        }

        if (personalState != null) {
            model.addAttribute("personalloginstate", personalState);
            model.addAttribute("name", personalName);
            model.addAttribute("id", id);
        }
        model.addAttribute("_csrf", request.getAttribute("_csrf"));
    }
}
