package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // 이 클래스가 웹 요청을 처리하는 Controller임을 선언
@RequestMapping // 각 메서드에 경로를 매핑할 때 사용할 기본 경로 설정
public class UserController {

    @Autowired
    private UserService userService; // UserService 객체를 주입하여 비즈니스 로직을 처리

    // 회원가입 폼 페이지를 보여주는 메서드
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // 회원가입 폼에 사용할 User 객체를 생성해서 전달
        return "register"; // 'register.html' 뷰를 반환
    }

    // 회원가입 폼을 제출한 후 처리를 담당하는 메서드
    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") User user,
                                      RedirectAttributes redirectAttributes) {

        // 사용자가 입력한 이메일이 이미 존재하는지 확인
        if (userService.isEmailExist(user.getEmail())) {
            // 이메일이 중복될 경우, 에러 메시지를 리다이렉트 속성에 담아 전달
            redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 이메일입니다.");
            return "redirect:/register"; // 중복 시 다시 회원가입 페이지로 리다이렉트
        }

        // 중복되지 않으면, 사용자 정보를 등록
        userService.registerNewUser(user);
        // 회원가입 성공 메시지를 리다이렉트 속성에 담아 로그인 페이지로 리다이렉트
        redirectAttributes.addFlashAttribute("successMessage", "회원가입이 성공적으로 완료되었습니다!");
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }

    // 전체 사용자 목록 페이지를 처리하는 메서드
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 관리자만 접근 가능하도록 설정
    public String listUsers(Model model) {
        // 모든 사용자를 가져오기 위해 UserService의 findAllUsers() 메서드 호출
        Iterable<User> users = userService.findAllUsers();
        model.addAttribute("users", users); // 사용자 목록을 뷰에 전달
        return "admin/users"; // 'admin/users.html' 뷰를 반환 (관리자 페이지에서 사용자 목록 표시)
    }
}
