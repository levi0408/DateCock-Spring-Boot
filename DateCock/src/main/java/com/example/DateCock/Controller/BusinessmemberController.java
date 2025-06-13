package com.example.DateCock.Controller;

import com.example.DateCock.DTO.BusinessmemberDTO;
import com.example.DateCock.Entity.BusinessmemberEntity;
import com.example.DateCock.Repository.BusinessmemberRepository;
import com.example.DateCock.Service.BusinessmemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BusinessmemberController {

    @Autowired
    BusinessmemberService businessmemberService;

    @Autowired
    BusinessmemberRepository businessmemberRepository;
    @GetMapping(value = "/businesssignup")
    public String businesssmemberinput1() {

        return "businessmember/businesssignup";
    }
    @PostMapping("/businessnumbercheck2")
    @ResponseBody
    public String checkBusinessNumber(@RequestParam("businessnumber") String businessnumber) {
        System.out.println("DEBUG >>> 받은 사업자번호: " + businessnumber);
        int count = businessmemberRepository.checkbusinessnumber(businessnumber);
        return count >0 ? "no" : "yes";
    }
    @PostMapping(value = "/businessmembersave")
    public String businesssmemberinput2(BusinessmemberDTO businessmemberDTO){
        String password = businessmemberDTO.getPassword();
        PasswordEncoder pe = new BCryptPasswordEncoder();

        password = pe.encode(password);
        businessmemberDTO.setPassword(password);

        BusinessmemberEntity businessmemberEntity = businessmemberDTO.entity();
        businessmemberService.businessmembersave(businessmemberEntity);


        return "redirect:/businesssignup";
    }

    @GetMapping(value = "/businessresult")
    public String find1() {

        return "businessmember/businessresult";
    }

    @GetMapping(value = "businessfindA")
    public String findbusinessnumber1(@RequestParam("businessname") String businessname,
                                      @RequestParam("email") String email, Model mo) {
        BusinessmemberDTO result = businessmemberService.findbusinessnumber(businessname, email);
        System.out.println("찾아온 사업자번호: " + result.getBusinessnumber());

        if (result != null) {
            mo.addAttribute("findbusinessnumber", result);
        } else {
            mo.addAttribute("idSearchError", "해당 정보로 등록된 사업자가 없습니다.");
        }

        return "businessmember/businessresult";
    }

    @PostMapping("/businessfindpwA")
    @ResponseBody
    public String findpassword1(@RequestParam String businessnumber, HttpSession session) {
        boolean match = businessmemberService.MatchBusinessNumber(businessnumber);

        if (match) {
            session.setAttribute("businessnumber", businessnumber);
            return "success";
        } else {
            return "fail";
        }
    }

    @PostMapping("/businessfindpwB")
    @ResponseBody
    public String findpassword2(@RequestParam String email, HttpSession session) {
        String businessnumber = (String) session.getAttribute("businessnumber");

        if (businessnumber == null) {
            return "fail";
        }
        boolean match = businessmemberService.MatchBusinessnumberEmail(businessnumber, email);

        if (match) {
            String code = businessmemberService.SendCode(email);
            session.setAttribute("authCode",code); //authCode = 인증코드
            return "success";
        }
        else {
            return "nomatch";
        }
    }

    @PostMapping("/businessfindpwC")
    @ResponseBody
    public String findpassword3(@RequestParam String inputCode, HttpSession session){
        String authCode = (String) session.getAttribute("authCode");
        String businessnumber = (String) session.getAttribute("businessnumber");

        System.out.println("DEBUG: authCode = [" + authCode + "]");
        System.out.println("DEBUG: inputCode = [" + inputCode + "]");

        if (authCode != null && inputCode != null && authCode.trim().equals(inputCode.trim()) && businessnumber != null) {
            String tempPassword = businessmemberService.NewPassword(businessnumber);
            return "tempPw:" + tempPassword;
        } else {
            return "fail";
        }

    }

}
