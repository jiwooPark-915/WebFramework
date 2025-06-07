package kr.ac.hansung.cse.hellospringdatajpa.repo;

import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 선택 사항이지만, 가독성을 위해 추가하는 것이 좋습니다.
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Security의 UserDetailsService에서 사용자의 이메일로 User 정보를 조회할 때 사용됩니다.
    Optional<User> findByEmail(String email);

    // (선택 사항) 이메일 존재 여부 확인용
    boolean existsByEmail(String email);
}