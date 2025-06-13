package com.example.DateCock.Controller;

import com.example.DateCock.DTO.MemberDTO;
import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.DateCourseReservationEntity;
import com.example.DateCock.Entity.MemberEntity;
import com.example.DateCock.Service.DateCourseReservationService;
import com.example.DateCock.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserMypageController {
    @Autowired
    DateCourseReservationService dateCourseReservationService;
    @Autowired
    MemberService memberService;
    @GetMapping(value = "/mypage")
    public String usermypage(Model model, HttpServletRequest request, RedirectAttributes ra) {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");

        if (id == null || id.isBlank()) {
            ra.addFlashAttribute("errorMessage", "세션이 만료되었습니다. 다시 로그인해주세요.");
            return "redirect:/main"; // 로그인 페이지로 튕기기
        }


        List<DateCourseReservationEntity> allReservations = dateCourseReservationService.allReservations(id);

        // 오늘 이후 예약 필터
        ArrayList<DateCourseReservationEntity> upcomingReservations = new ArrayList<>();
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        for (DateCourseReservationEntity r : allReservations) {
            if (r.getDay().compareTo(today) >= 0) {
                upcomingReservations.add(r);
            }
        }
        model.addAttribute("list", upcomingReservations);
        model.addAttribute("upcomingReservations", !upcomingReservations.isEmpty());
        return "usermypage/usermypage";
    }
    @GetMapping(value = "/datelist")
    public String details(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        List<DateCourseReservationEntity> allReservations = dateCourseReservationService.allReservations(id);
        // 오늘 이전의 예약만 추출
        ArrayList<DateCourseReservationEntity> pastReservations = new ArrayList<>();
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        for (DateCourseReservationEntity r : allReservations) {
            if (r.getDay().compareTo(today) < 0) { // 오늘 이전 날짜만
                pastReservations.add(r);
            }

        }

        model.addAttribute("list", pastReservations);
        return "usermypage/datelist";
    }


    @RequestMapping("/details")
    public String details(@RequestParam("name") String name, @RequestParam("id")String id , @RequestParam("day")Date day,
                          @RequestParam("businessname")String businessname, Model model) {


        BusinessEntity dto = dateCourseReservationService.businessinputselect(businessname);
        DateCourseReservationEntity result = dateCourseReservationService.datereservationselect(id,businessname,day);
        model.addAttribute("dto",dto); // 가게목록
        model.addAttribute("result", result); //예약목록
        return "usermypage/datecoursedetails";
    }

    @PostMapping(value = "reservationdelete")
    @ResponseBody
    public String reservationdelete(@RequestParam("name")String name ,@RequestParam("day")Date day ,@RequestParam("businessname") String businessname,HttpServletRequest request) {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        int deleted = dateCourseReservationService.datereservationdelete(id, name,businessname, day); // 예약자 id 기준 삭제
        if (deleted > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
    @GetMapping(value = "userdelete")
    public String delete1(){

        return "usermypage/userdelete";
    }

    @PostMapping(value = "userdelete1")
    public String delete2(MemberDTO dto, HttpSession session){
        String id = (String) session.getAttribute("id");
        memberService.userinfodelete(id);

        session.invalidate();

        return "redirect:/main";
    }

    @GetMapping(value = "usermodify")
    public String modify1(HttpSession session, Model model, MemberDTO memberDTO){
        String id = (String) session.getAttribute("id");
        MemberEntity dto = memberService.usermodifyview(id);
        model.addAttribute("dto",dto);

        return "usermypage/usermodify";
    }

    @PostMapping(value = "usermodify1")
    public String modify2(MemberDTO dto, Model model,RedirectAttributes ra){
        String id = dto.getId();
        String name = dto.getName();
        String email = dto.getEmail();
        String phone = dto.getPhone();

        MemberEntity memberentity = dto.toEntity();
        memberService.userinfomodify(memberentity);

        ra.addFlashAttribute("successMessage", "회원 정보가 성공적으로 변경되었습니다.");

        return "redirect:/mypage";
    }

    @GetMapping(value = "userpwmodify")
    public String pwmodify1(HttpSession session, Model model){
        String id = (String) session.getAttribute("id");

        if(id==null || id.isBlank()){
            return "redirect:/login";
        }
        MemberEntity dto = memberService.userpwmodifyview(id);
        model.addAttribute("dto",dto);

        return "usermypage/userpwmodify";
    }

    @PostMapping(value = "userpwmodify1")
    public String pwmodify2(@ModelAttribute MemberDTO dto,
                            HttpSession session,
                            RedirectAttributes ra,
                            HttpServletRequest request) {

        // 1. 세션에서 User ID 가져오기
        String id = (String) session.getAttribute("id");
        if (id == null || id.isBlank()) {
            return "redirect:/login";
        }
        dto.setId(id);

        // 2. 현재 비밀번호 조회
        MemberEntity encodedPassword = memberService.getuserpw(id);
        if (encodedPassword == null) {
            ra.addFlashAttribute("errorMessage", "비밀번호 조회에 실패했습니다.");
            return "redirect:/userpwmodify";
        }

        // 3. 현재 비밀번호 확인 (pwd 필드 값이 dto.password로 들어오게 강제 세팅)
        String currentPw = request.getParameter("pwd"); // HTML name="pwd"
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (currentPw == null || !encoder.matches(currentPw, encodedPassword.getPassword())) {
            ra.addFlashAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/userpwmodify";
        }

        // 4. 새 비밀번호 확인
        String newPw = request.getParameter("newpwd");
        String confirmPw = request.getParameter("newpwd_confirm");
        dto.setPassword(newPw);
        if (newPw == null || confirmPw == null || !newPw.equals(confirmPw)) {
            ra.addFlashAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
            return "redirect:/userpwmodify";
        }

        // 5. 특수문자 포함 여부 확인
        if (!dto.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            ra.addFlashAttribute("errorMessage", "비밀번호는 특수문자를 1개 이상 포함해야 합니다.");
            return "redirect:/businesspwmodify";
        }

        // 6. 새 비밀번호 암호화 및 저장
        String encodedNewPw = encoder.encode(newPw);
        dto.setPassword(encodedNewPw); // 여기 새 비번을 넣어줘야 DB 반영됨

        MemberEntity memberEntity = dto.toEntity();
        memberService.userinfomodify(memberEntity);

        // 7. 세션 종료 및 리다이렉트
        session.invalidate();
        ra.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요.");
        return "redirect:/businesssignup";
    }

}
