package kr.ac.hansung.cse.hellospringdatajpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Configuration
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 등을 활성화하기 위한 설정
public class SecurityConfig {

    // 비밀번호 암호화를 위한 PasswordEncoder 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용하여 비밀번호를 안전하게 해시 처리
        return new BCryptPasswordEncoder();
    }

    // HTTP 보안 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 기본적인 페이지 접근 권한 설정
                        .requestMatchers(new AntPathRequestMatcher("/register/**"), new AntPathRequestMatcher("/login/**"), new AntPathRequestMatcher("/"))
                        .permitAll() // 로그인 및 회원가입 페이지는 모든 사용자에게 허용

                        // 정적 리소스 (CSS, JS, 이미지 등)에 대한 접근 허용
                        .requestMatchers(new AntPathRequestMatcher("/css/**"), new AntPathRequestMatcher("/js/**"), new AntPathRequestMatcher("/images/**"))
                        .permitAll()

                        // 상품 목록 페이지는 "USER", "ADMIN" 역할을 가진 사용자만 접근 가능
                        .requestMatchers(new AntPathRequestMatcher("/products"), new AntPathRequestMatcher("/products/")).hasAnyRole("USER", "ADMIN")

                        // 상품 등록, 수정, 삭제는 "ADMIN" 역할을 가진 사용자만 접근 가능
                        .requestMatchers(new AntPathRequestMatcher("/products/new"), new AntPathRequestMatcher("/products/save"),
                                new AntPathRequestMatcher("/products/edit/**"), new AntPathRequestMatcher("/products/delete/**"))
                        .hasRole("ADMIN")

                        // 관리자 페이지 관련 URL 접근을 "ADMIN" 역할을 가진 사용자만 허용
                        .requestMatchers(new AntPathRequestMatcher("/templates/admin/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")

                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
                // 로그인 설정
                .formLogin(form -> form
                        .loginPage("/login") // 커스텀 로그인 페이지를 사용
                        .loginProcessingUrl("/login") // Spring Security가 로그인 폼을 처리하도록 지정
                        .defaultSuccessUrl("/loginSuccess", true) // 로그인 성공 후 "/loginSuccess"로 리다이렉트
                        .failureUrl("/login?error=true") // 로그인 실패 시 "/login?error=true"로 리다이렉트
                        .permitAll() // 로그인 관련 페이지는 모든 사용자에게 허용
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 URL 설정
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공 시 "/login?logout"으로 리다이렉트
                        .invalidateHttpSession(true) // 로그아웃 후 세션을 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                        .permitAll() // 로그아웃 관련 페이지는 모든 사용자에게 허용
                );
        return http.build(); // 설정을 마친 후 SecurityFilterChain을 빌드하여 반환
    }

    // 로그인 성공 시 처리할 핸들러
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 성공 후에 사용할 RedirectAttributes 객체를 가져옴
            RedirectAttributes redirectAttributes = (RedirectAttributes) request.getAttribute("redirectAttributes");
            if (redirectAttributes != null) {
                // 로그인 성공 메시지를 플래시 속성으로 전달
                redirectAttributes.addFlashAttribute("loginSuccess", "로그인에 성공하셨습니다.");
            }
            // 로그인 성공 후 "/products" 페이지로 리다이렉트
            response.sendRedirect("/products");
        };
    }

    // 로그인 실패 시 처리할 핸들러
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            // 로그인 실패 후 "/login?error=true"로 리다이렉트
            response.sendRedirect("/login?error=true");
        };
    }
}
