package com.example.DateCock.Controller;

import com.example.DateCock.Config.MemberSecurityConfig;
import com.example.DateCock.DTO.MemberDTO;
import com.example.DateCock.Entity.MemberEntity;
import com.example.DateCock.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    @Qualifier("memberPasswordEncoder")
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Long> verificationCodeTimestamps = new HashMap<>();
    private static final long VERIFICATION_CODE_EXPIRATION_MS = 3 * 60 * 1000;

    @GetMapping("/signup")
    public String input(Model model, MemberDTO memberDTO) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "/member/signup";
    }

    @GetMapping("/membersearch")
    public String search() {
        return "member/msearch";
    }

    @PostMapping("/membersave")
    public String save(@ModelAttribute("memberDTO") @Valid MemberDTO memberDTO, Model mo) throws UnsupportedEncodingException {
        memberDTO.setPassword(MemberSecurityConfig.memberPasswordEncoder().encode(memberDTO.getPassword()));
        MemberEntity member = memberDTO.toEntity();
        memberService.insert(member);
        String encodedMessage = URLEncoder.encode("회원가입이 완료되었습니다!", StandardCharsets.UTF_8.toString());
        return "redirect:/signup?successMessage=" + encodedMessage;
    }

    @GetMapping("/checkUserId")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam("id") String id) {
        boolean isAvailable = !memberService.isUserIdExists(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/searchIdAjax", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchIdAjax(@RequestParam("name") String name, @RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "이름과 이메일을 모두 입력해주세요.");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            String foundId = memberService.findUserIdByNameAndEmail(name, email);
            if (foundId != null && !foundId.isEmpty()) {
                response.put("success", true);
                response.put("foundId", foundId);
            } else {
                response.put("success", false);
                response.put("message", "입력하신 정보와 일치하는 아이디를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "아이디 찾기 중 서버 오류가 발생했습니다. 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/checkMemberExists", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkMemberExits(@RequestParam("findid") String id, @RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || id.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            response.put("exists", false);
            response.put("message", "아이디와 이메일을 모두 입력해주세요.");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            boolean memberExists = memberService.checkMemberExists(id, email);
            if (memberExists) {
                response.put("exists", true);
                response.put("message", "회원 정보가 확인되었습니다.");
            } else {
                response.put("exists", false);
                response.put("message", "입력하신 정보와 일치하는 회원이 없습니다.");
            }
        } catch (Exception e) {
            response.put("exists", false);
            response.put("message", "회원 확인 중 서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestParam("findid") String id, @RequestParam("email") String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String generatedCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            boolean memberExists = memberService.checkMemberExists(id, email);
            if (!memberExists) {
                response.put("success", false);
                response.put("message", "해당 아이디와 이메일로 등록된 회원이 없습니다.");
                return ResponseEntity.ok(response);
            }
            memberService.sendVerificationEmail(email, generatedCode);
            verificationCodes.put(email, generatedCode);
            verificationCodeTimestamps.put(email, System.currentTimeMillis() + VERIFICATION_CODE_EXPIRATION_MS);
            response.put("success", true);
            response.put("message", "인증번호가 발송되었습니다. 3분 이내로 입력해주세요.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "인증번호 발송에 실패했습니다. 다시 시도해주세요.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkVerificationCode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkVerificationCode(@RequestParam("code") String userCode, @RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        String storedCode = verificationCodes.get(email);
        Long storedTimestamp = verificationCodeTimestamps.get(email);
        if (storedCode == null || storedTimestamp == null) {
            response.put("verified", false);
            response.put("message", "인증번호를 발송해주세요.");
            return ResponseEntity.ok(response);
        }
        if (System.currentTimeMillis() > storedTimestamp) {
            verificationCodes.remove(email);
            verificationCodeTimestamps.remove(email);
            response.put("verified", false);
            response.put("message", "인증 시간이 만료되었습니다. 인증번호를 다시 발송해주세요.");
            return ResponseEntity.ok(response);
        }
        if (storedCode.equals(userCode)) {
            verificationCodes.remove(email);
            verificationCodeTimestamps.remove(email);
            response.put("verified", true);
            response.put("message", "인증되었습니다! 비밀번호를 재설정할 수 있습니다.");
        } else {
            response.put("verified", false);
            response.put("message", "인증번호가 올바르지 않습니다.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/searchpwd")
    public String goToPasswordModifyPage(@RequestParam("id") String id, @RequestParam("email") String email, Model model) {
        model.addAttribute("userId", id);
        model.addAttribute("email", email);
        return "member/pwdmodify";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("id") String id, @RequestParam("email") String email, @RequestParam("newPassword") String newPassword, Model model) {
        try {
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
            memberService.updatePassword(id, encodedPassword);
            model.addAttribute("successMessage", "비밀번호가 성공적으로 재설정되었습니다. 로그인 해주세요.");
            return "redirect:/DateCocklog";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "비밀번호 재설정에 실패했습니다. 다시 시도해주세요.");
            model.addAttribute("userId", id);
            model.addAttribute("email", email);
            return "member/pwdmodify";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "예상치 못한 오류가 발생했습니다.");
            model.addAttribute("userId", id);
            model.addAttribute("email", email);
            return "member/pwdmodify";
        }
    }
}