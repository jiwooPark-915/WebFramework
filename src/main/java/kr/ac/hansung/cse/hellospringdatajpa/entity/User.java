package kr.ac.hansung.cse.hellospringdatajpa.entity;

import jakarta.persistence.*;
import lombok.Data; // 또는 @Getter, @Setter
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data // UserDetails 구현 및 기타 편의성을 위해 @Data 사용 가정
@Table(name = "users") // 실제 사용자 테이블 이름에 맞게 수정 (예: user_accounts)
public class User implements UserDetails { // UserAccount 대신 User로 가정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true; // DB에 enabled 컬럼이 필요합니다 (이전 답변 참고)

    // ***** 다대다 관계 매핑 *****
    // 'user_roles'는 User와 Role 엔티티를 연결하는 조인 테이블입니다.
    // user_roles 테이블은 user_id와 role_id 컬럼을 가질 것입니다.
    @ManyToMany(fetch = FetchType.EAGER) // 역할 정보는 자주 사용되므로 EAGER 로딩
    @JoinTable(
            name = "user_roles", // 조인 테이블의 이름
            joinColumns = @JoinColumn(name = "user_id"), // User 테이블의 PK와 연결
            inverseJoinColumns = @JoinColumn(name = "role_id") // Role 테이블의 PK와 연결
    )
    private Set<Role> roles = new HashSet<>(); // Set을 사용하여 중복 역할을 방지

    // 편의 메소드 (UserService에서 addRole을 사용하므로 필요)
    public void addRole(Role role) {
        this.roles.add(role);
    }

    // UserDetails 인터페이스 구현 메소드들
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role 엔티티의 getName() 메소드를 통해 실제 역할 문자열을 가져옵니다.
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // ... (나머지 UserDetails 메소드들은 이전 답변과 동일)
    @Override
    public String getPassword() { return this.password; }
    @Override
    public String getUsername() { return this.email; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return this.enabled; }
}