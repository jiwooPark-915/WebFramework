package kr.ac.hansung.cse.hellospringdatajpa.service;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리를 위해 추가

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service // Spring 서비스 빈으로 등록
@Transactional // 트랜잭션 관리
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자의 이메일(Spring Security에서는 "username"으로 간주)을 기반으로
     * 데이터베이스에서 사용자 정보를 로드하고 UserDetails 객체로 반환합니다.
     *
     * @param email 사용자의 이메일 (로그인 시 입력되는 사용자명)
     * @return Spring Security가 사용하는 UserDetails 객체
     * @throws UsernameNotFoundException 해당 이메일의 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. UserRepository를 사용하여 데이터베이스에서 User 엔티티를 이메일로 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. 조회된 User 엔티티의 정보를 기반으로 Spring Security의 UserDetails 객체 생성
        // org.springframework.security.core.userdetails.User 클래스는 UserDetails 인터페이스의 구현체입니다.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                 // 사용자명 (여기서는 이메일)
                user.getPassword(),              // 암호화된 비밀번호
                user.isEnabled(),                // 계정 활성화 여부
                true,                            // 계정 만료되지 않음 (isAccountNonExpired)
                true,                            // 자격 증명(비밀번호) 만료되지 않음 (isCredentialsNonExpired)
                true,                            // 계정 잠금되지 않음 (isAccountNonLocked)
                mapRolesToAuthorities(user.getRoles()) // 사용자의 역할 목록을 GrantedAuthority 목록으로 변환
        );
    }

    /**
     * User 엔티티의 Set<Role>을 Spring Security의 Collection<? extends GrantedAuthority>로 변환합니다.
     * 각 Role의 이름을 "ROLE_" 접두사와 함께 SimpleGrantedAuthority로 변환합니다.
     *
     * @param roles 사용자의 역할 Set
     * @return GrantedAuthority 컬렉션
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Role 객체의 이름을 GrantedAuthority로 매핑
                .collect(Collectors.toList()); // 리스트로 수집
    }
}