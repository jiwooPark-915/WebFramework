package kr.ac.hansung.cse.hellospringdatajpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(String error, String logout, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "로그인에 실패하셨습니다. 다시 입력해주세요.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃 되었습니다.");
        }
        return "login"; // 로그인 페이지로 리턴
    }

    // 로그인 성공 후 알림을 표시하려면 RedirectAttributes를 사용
    @GetMapping("/loginSuccess")
    public String loginSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginSuccess", "로그인에 성공하셨습니다.");
        return "redirect:/products"; // 상품 목록 페이지로 리다이렉트
    }
}
