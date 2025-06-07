package kr.ac.hansung.cse.hellospringdatajpa.service;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Product;
import kr.ac.hansung.cse.hellospringdatajpa.repo.ProductRepository;
import lombok.RequiredArgsConstructor; // Lombok의 @RequiredArgsConstructor를 사용하여 자동 생성자 주입
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; // Optional을 사용하여 null 값 처리를 간편하게 할 수 있음

@Service // 이 클래스는 서비스 계층을 나타내며, 비즈니스 로직을 처리합니다.
@Transactional // 메서드 실행 시 트랜잭션 처리를 보장하는 어노테이션
@RequiredArgsConstructor // final 필드가 있는 클래스에 자동으로 생성자를 생성하는 Lombok 어노테이션
public class ProductService {

    // ProductRepository를 final로 선언하여, 생성자 주입 방식으로 의존성 주입
    private final ProductRepository repo;

    // 상품 ID로 조회하는 메서드 (Optional 사용)
    // Optional을 사용하여 null 값을 안전하게 처리할 수 있습니다.
    // findById 메서드는 값이 없을 경우 빈 Optional을 반환하므로, 컨트롤러에서 적절히 처리할 수 있습니다.
    public Optional<Product> findById(Long id) {
        return repo.findById(id); // 상품을 ID로 찾는 쿼리 실행
    }

    // 모든 상품 목록을 조회하는 메서드
    // 리스트 형태로 모든 상품을 반환합니다.
    // 이 메서드는 서비스 계층에서 단순히 Repository의 findAll() 메서드를 호출하는 역할을 합니다.
    public List<Product> findAll() {
        return repo.findAll(); // 모든 상품을 리스트 형태로 반환
    }

    // 상품을 저장하는 메서드 (추가 또는 수정)
    // 저장할 상품 객체를 매개변수로 받아서, Repository를 통해 DB에 저장합니다.
    // DB에 이미 존재하는 상품일 경우, 해당 상품을 수정하고, 없으면 새로 추가됩니다.
    public void save(Product product) {
        repo.save(product); // 상품 저장
    }

    // 상품 ID로 삭제하는 메서드
    // 삭제할 상품의 ID를 받아 해당 상품을 DB에서 삭제합니다.
    public void deleteById(Long id) {
        repo.deleteById(id); // 주어진 ID에 해당하는 상품을 삭제
    }
}
