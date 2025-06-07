package kr.ac.hansung.cse.hellospringdatajpa.service;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.repo.RoleRepository;
import kr.ac.hansung.cse.hellospringdatajpa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // 비즈니스 로직을 처리하는 서비스 계층으로 이 클래스가 사용됩니다.
@Transactional // 모든 메서드는 트랜잭션 처리가 되어 데이터베이스에 안전하게 저장됩니다.
public class UserService {

    @Autowired
    private UserRepository userRepository; // 사용자 관련 데이터베이스 작업을 담당하는 레포지토리

    @Autowired
    private RoleRepository roleRepository; // 역할 관련 데이터베이스 작업을 담당하는 레포지토리

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder

    // 새로운 사용자 저장 (회원가입)
    public User registerNewUser(User user) {
        // 1. 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(user.getPassword())); // BCryptPasswordEncoder를 사용하여 비밀번호 암호화

        // 2. 기본 역할(ROLE_USER) 할당
        Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER"); // "ROLE_USER" 역할을 DB에서 조회
        Role userRole;

        // ROLE_USER가 이미 있으면 그 역할을 사용, 없으면 새로 생성
        if (userRoleOptional.isPresent()) {
            userRole = userRoleOptional.get(); // ROLE_USER가 이미 존재하면 가져옴
        } else {
            // 없으면 새로운 ROLE_USER 역할을 생성하고 DB에 저장
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole); // ROLE_USER 역할을 저장
        }
        user.addRole(userRole); // 사용자에게 ROLE_USER 역할을 추가

        // (옵션) 특정 이메일(관리자 이메일)에 대해 관리자 역할(Role) 부여
        if ("2271247@hansung.ac.kr".equals(user.getEmail())) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            Role adminRole;

            // 관리자 역할이 존재하지 않으면 새로 생성하여 저장
            if (adminRoleOptional.isPresent()) {
                adminRole = adminRoleOptional.get(); // ROLE_ADMIN이 존재하면 가져옴
            } else {
                adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole); // ROLE_ADMIN 역할을 저장
            }
            user.addRole(adminRole); // 사용자가 관리자 역할을 가지도록 설정
            System.out.println("관리자 계정(" + user.getEmail() + ")이 생성되었습니다.");
        }

        // 3. 사용자 정보를 데이터베이스에 저장하고 반환
        return userRepository.save(user); // 새로 생성된 사용자 저장
    }

    // 이메일 중복 확인
    public boolean isEmailExist(String email) {
        // 이메일로 이미 존재하는 사용자 확인
        return userRepository.existsByEmail(email); // 존재하면 true, 없으면 false 반환
    }

    // 모든 사용자 조회 (관리자 페이지용)
    public Iterable<User> findAllUsers() {
        // 모든 사용자 데이터를 반환
        return userRepository.findAll(); // 전체 사용자 리스트를 반환
    }

    // 현재 로그인한 사용자 조회
    public User getLoggedInUser() {
        // 현재 로그인한 사용자의 정보를 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // SecurityContext에서 로그인 정보 추출

        // 로그인된 사용자일 경우, 해당 사용자 정보를 반환
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername(); // 로그인한 사용자의 이메일을 추출
            return userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found")); // 이메일로 사용자 찾기
        }

        // 로그인하지 않은 경우 예외 처리
        throw new RuntimeException("User not logged in"); // 로그인되지 않았을 경우 예외를 던짐
    }
}
