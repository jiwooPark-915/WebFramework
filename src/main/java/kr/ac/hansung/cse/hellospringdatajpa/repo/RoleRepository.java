package kr.ac.hansung.cse.hellospringdatajpa.repo;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 선택 사항이지만, 가독성을 위해 추가하는 것이 좋습니다.
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 역할 이름(name)으로 Role 정보를 조회할 때 사용됩니다.
    Optional<Role> findByName(String name);
}