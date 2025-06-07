// 예시: 기존의 ErrorController 구현이 여러 군데 있을 수 있으니, 하나로만 유지합니다.
package kr.ac.hansung.cse.hellospringdatajpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    // /error 경로에 대한 처리
    @RequestMapping("/error")
    public String handleError() {
        return "error"; // error.html 페이지를 반환
    }

    // getErrorPath() 메서드는 Spring Boot 2.5 이후로 필요하지 않음
}
