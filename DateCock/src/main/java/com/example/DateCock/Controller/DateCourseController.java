package com.example.DateCock.Controller;

import com.example.DateCock.DTO.*;

import com.example.DateCock.Entity.*;
import com.example.DateCock.Service.*;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class DateCourseController {

    @Autowired
    DateCourseService dateCourseService;
    @Autowired
    BusinessService businessService;
    @Autowired
    MemberService memberService;
    @Autowired
    DateCourseReservationService dateCourseReservationService;
    @Autowired
    GuestService guestService;

    @GetMapping(value = "/recommendation")
    public String recommendation() {

        return "datecourse/recommendation";
    }

    @PostMapping("/getDateCourse")
    @ResponseBody
    public List<DateCourseDTO> getCourse(@RequestBody FilterDTO dto) {
        System.out.println("나이: " + dto.getAge());
        System.out.println("지역: " + dto.getRegion());
        System.out.println("종류: " + dto.getType());

        return dateCourseService.getFilteredCourses(dto.getAge(), dto.getRegion(), dto.getType());
    }

    @PostMapping("/recommendresult")
    public String recommendResult(@RequestParam("resultList") String resultList, Model model) {
        // JSON을 파싱해서 리스트로 바꾸고
        List<DateCourseDTO> list = new Gson().fromJson(resultList, new TypeToken<List<DateCourseDTO>>() {
        }.getType());

        model.addAttribute("resultList", list);
        return "datecourse/recommendresult";
    }

    @GetMapping(value = "/datecourseout")
    public String datecourseout(Model model, DateCourseDTO dateCourseDTO) {
        List<BusinessEntity> list = businessService.allselect();
        model.addAttribute("list", list);
        return "datecourse/datecourseout";
    }

    @GetMapping(value = "/datereservation")
    public String datereservation(@RequestParam("businessname") String businessname, Model model, MemberDTO dto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userid = (String) session.getAttribute("id");

        String user = memberService.userName(userid);
        String phone = memberService.userPhone(userid);
        BusinessEntity bentity = businessService.datereservationOut(businessname);
        String businessAddress = businessService.businessAddressOut(businessname);
        List<GuestEntity> guestList = guestService.select();
        model.addAttribute("guestList", guestList);

        model.addAttribute("businessview", bentity); //가게정보출력
        model.addAttribute("username", user); //유저이름
        model.addAttribute("userphone", phone); //유저전화번호
        model.addAttribute("businessAddress", businessAddress);//가게 주소
        model.addAttribute("userid", userid); //유저아이디
        return "datecourse/datereservation";
    }

    @RequestMapping(value = "/reservationsave")
    public String reservationsave(Model model, HttpServletResponse response, DateCourseReservationDTO dcrdto,
                                  @RequestParam("businessname") String businessname, @RequestParam("image") String image) throws IOException {
        dcrdto.setBusinessname(businessname);
        dcrdto.setImage(image);

        DateCourseReservationEntity dateentity = dcrdto.entity();
        DateCourseReservationEntity result = dateCourseReservationService.reservationOut(dcrdto.getBusinessname(), dcrdto.getDay());

        if (result == null) {
            dateCourseReservationService.insert(dateentity);
            return "redirect:/main";
        } else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print("<script>alert('이미 예약되어 있습니다'); location.href='datecourseout';</script>");
            out.close();
            return null;
        }
    }


    @PostMapping("/insertGuest")
    @ResponseBody
    public ResponseEntity<GuestEntity> insertGuest(@RequestParam String name,
                                                   @RequestParam String memo) {
        GuestDTO dto = new GuestDTO();
        dto.setName(name);
        dto.setMemo(memo);

        GuestEntity saved = guestService.insertGuest(dto);

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/deleteGuest")
    @ResponseBody
    public ResponseEntity<Void> deleteGuest(@RequestParam String name,
                                            @RequestParam String todays) {
        try {
            // 2025-06-12 11:25:57.0 → 2025-06-12 11:25:57 로 자르기
            if (todays.contains(".")) {
                todays = todays.substring(0, todays.indexOf("."));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(todays, formatter);
            Timestamp timestamp = Timestamp.valueOf(dateTime);

            guestService.deleteGuest(name, timestamp);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.out.println("⛔ 삭제 실패. 전달받은 todays: " + todays);
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/businesssearch")
    public String businessSearch(@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
                                 @RequestParam(value="zone", required=false, defaultValue="") String zone,
                                 Model m) {
        List<BusinessEntity> list;
        if (zone == null || zone.isEmpty()) {
            // 전체 지역: 가게명만 검색
            list = businessService.searchBusinessesByName(keyword);
        } else {
            // 특정 지역: 지역+가게명 AND 조건
            list = businessService.searchBusinessesByZoneAndName(zone, keyword);
        }
        m.addAttribute("list", list);
        return "datecourse/datecourseout";
    }

}



