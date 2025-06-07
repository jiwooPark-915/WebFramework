package kr.ac.hansung.cse.hellospringdatajpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // Lombok을 사용하여 getter와 setter 메서드를 자동 생성

@Entity
@Table(name = "roles") // 'roles' 테이블과 매핑되는 엔티티 클래스
@Getter
@Setter // Lombok을 사용하여 getter와 setter 메서드를 자동으로 생성
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 필드는 자동 생성되며, 데이터베이스에서 관리
    private Long id;

    // DB 컬럼 이름 'rolename'을 'name' 필드에 매핑
    // 'rolename' 컬럼은 유니크하며 null을 허용하지 않음
    @Column(name = "rolename", unique = true, nullable = false)
    private String name; // 역할 이름 (예: "ROLE_USER", "ROLE_ADMIN")

    // 기본 생성자 (빈 객체 생성용)
    public Role() {
    }

    // 역할 이름을 매개변수로 받는 생성자
    public Role(String name) {
        this.name = name;
    }

    // 객체의 문자열 표현을 반환하는 toString 메서드 (디버깅 등에서 사용)
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
