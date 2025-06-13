package com.example.DateCock.Controller;

import com.example.DateCock.DTO.BusinessmemberDTO;
import com.example.DateCock.Entity.BusinessmemberEntity;
import com.example.DateCock.Service.BusinessmemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BusinessMypageController {

    @Autowired
    BusinessmemberService businessmemberService ;
    @GetMapping("/businessmypage")
    public String businessmypage() {


        return "businessmypage/businessmypage";
    }

    @GetMapping("/manual")
    public String manual(){

        return "businessmypage/manual";
    }

    @GetMapping(value="/notice")
    public String notice() {

        return "businessmypage/notice";
    }
    @GetMapping(value="/businessmodify")
    public String myinfomodify1(HttpSession session, Model model, BusinessmemberDTO businessmemberDTO) {
        String businessnumber=(String) session.getAttribute("businessnumberA");
        BusinessmemberEntity dto =businessmemberService.modifyview(businessnumber);
        model.addAttribute("dto",dto);
        return "businessmypage/businessmodify";
    }
    @PostMapping(value="/businessmodify1")
    public String myinfomodify2(BusinessmemberDTO dto, Model model, RedirectAttributes ra) {
        String businessnumber=dto.getBusinessnumber();
        String businessname=dto.getBusinessname();
        String email=dto.getEmail();
        String phone=dto.getPhone();

        BusinessmemberEntity businessmemberEntity = dto.entity();
        businessmemberService.modifygo(businessmemberEntity);

        ra.addFlashAttribute("successMessage", "회원 정보가 성공적으로 변경되었습니다.");

        return "redirect:/businessmypage";
    }

    @GetMapping(value="/businesspwmodify") //비밀번호 변경
    public String myinfopwmodify(HttpSession session,Model model) {
        String businessnumber = (String) session.getAttribute("businessnumberA");

        if (businessnumber == null || businessnumber.isBlank()) {
            return "redirect:/login"; // 세션 없으면 로그인으로 튕김
        }
        BusinessmemberEntity dto =businessmemberService.modifyview(businessnumber);
        model.addAttribute("dto",dto);
        return "businessmypage/businesspwmodify";
    }
    @PostMapping("/businesspwmodify")
    public String modifyBusinessPassword(
            @ModelAttribute BusinessmemberDTO dto,
            HttpSession session,
            RedirectAttributes ra) {

        // 1. 세션에서 사업자 번호 가져오기
        String businessnumber = (String) session.getAttribute("businessnumberA");
        if (businessnumber == null) {
            ra.addFlashAttribute("errorMessage", "세션이 만료되었습니다. 다시 로그인해주세요.");
            return "redirect:/businesspwmodify";
        }

        dto.setBusinessnumber(businessnumber);

        // 2. 현재 비밀번호 조회
        BusinessmemberEntity encodedPassword = businessmemberService.getbusinesspw(businessnumber);

        if (encodedPassword == null) {
            ra.addFlashAttribute("errorMessage", "비밀번호 조회에 실패했습니다.");
            return "redirect:/businesspwmodify";
        }

        // 3. 비밀번호 비교
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String storedPw = encodedPassword.getPassword(); // 필드명에 맞게 수정
        if (dto.getPassword() == null || !encoder.matches(dto.getPassword(), storedPw)) {
            ra.addFlashAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/businesspwmodify";
        }

        // 4. 새 비밀번호 확인
        if (dto.getBusinessnewpw() == null ||
                dto.getBusinessnewpw_confirm() == null ||
                !dto.getBusinessnewpw().equals(dto.getBusinessnewpw_confirm())) {
            ra.addFlashAttribute("errorMessage", "새 비밀번호와 확인이 일치하지 않습니다.");
            return "redirect:/businesspwmodify";
        }

        // 5. 특수문자 포함 여부 확인
        if (!dto.getBusinessnewpw().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            ra.addFlashAttribute("errorMessage", "비밀번호는 특수문자를 1개 이상 포함해야 합니다.");
            return "redirect:/businesspwmodify";
        }

        // 6. 새 비밀번호 암호화 및 저장
        String newEncodedPw = encoder.encode(dto.getBusinessnewpw());
        dto.setPassword(newEncodedPw);

        BusinessmemberEntity businessmemberEntity = dto.entity();
        businessmemberService.modifygo(businessmemberEntity);

        // 7. 세션 종료 및 리다이렉트
        session.invalidate();
        ra.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요.");
        return "redirect:/businesssignup";
    }


    @GetMapping(value="/businessdelete") //탈퇴할 사업자의 이메일 보내주기
    public String myinfodelete1() {

        return "businessmypage/businessdelete";
    }

    @PostMapping(value="/businessdelete1")
    public String myinfodelete2(BusinessmemberDTO dto,HttpSession session) {
        String businessnumber = (String) session.getAttribute("businessnumberA");

        businessmemberService.businessinfodelete(businessnumber);

        // 세션 전체 종료 (로그아웃 처리)
        session.invalidate();

        return "redirect:/main";
    }
}
