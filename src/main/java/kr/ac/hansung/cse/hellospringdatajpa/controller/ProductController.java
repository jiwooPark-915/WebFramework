package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Product;
import kr.ac.hansung.cse.hellospringdatajpa.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products") // "/products" URL에 관련된 요청을 이 컨트롤러가 처리하도록 지정
public class ProductController {

    private final ProductService service; // ProductService를 사용하여 비즈니스 로직을 처리

    // 생성자 주입 방식으로 ProductService를 주입받음 (권장: @Autowired 대신 생성자 주입을 사용)
    public ProductController(ProductService service) {
        this.service = service;
    }

    // 상품 목록을 보여주는 메서드 (홈 페이지)
    @GetMapping({"", "/"}) // "/products" 또는 "/products/"로 접근 시 호출됨
    public String viewHomePage(Model model) {
        List<Product> listProducts = service.findAll(); // 모든 상품을 가져옴
        model.addAttribute("listProducts", listProducts); // 뷰로 상품 목록 전달
        return "products/list"; // "products/list.html"을 반환하여 상품 목록을 화면에 표시
    }

    // 새로운 상품을 등록하는 페이지를 보여주는 메서드
    @GetMapping("/new") // "/products/new"로 접근 시 호출됨
    public String showNewProductPage(Model model) {
        Product product = new Product(); // 새로운 빈 상품 객체 생성
        model.addAttribute("product", product); // 뷰로 전달하여 폼에 바인딩
        return "products/form"; // 상품 등록 폼 페이지로 이동
    }

    // 기존 상품을 수정하는 페이지를 보여주는 메서드
    @GetMapping("/edit/{id}") // "/products/edit/{id}"로 접근 시 호출됨
    public String showEditProductPage(@PathVariable(name = "id") Long id, Model model) {
        // 상품 ID를 기준으로 상품을 찾아오는 메서드
        Optional<Product> productOptional = service.findById(id);

        // 상품이 존재하면 해당 상품 정보를 폼에 채워서 수정 페이지로 이동
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "products/form"; // 상품 수정 폼으로 이동
        } else {
            // 상품이 없을 경우 오류 메시지를 모델에 추가하고 목록으로 리다이렉트
            model.addAttribute("errorMessage", "해당 ID의 상품을 찾을 수 없습니다: " + id);
            return "redirect:/products"; // 상품이 없으면 목록 페이지로 리다이렉트
        }
    }

    // 상품 정보를 저장하는 메서드
    @PostMapping("/save") // 상품 등록 및 수정 요청을 처리하는 POST 메서드
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              Model model) {
        // 유효성 검사를 통과하지 못한 경우 폼 페이지를 다시 반환
        if (result.hasErrors()) {
            System.out.println("상품 저장 유효성 검사 오류: " + result.getAllErrors());
            return "products/form"; // 유효성 오류가 있으면 폼 페이지로 돌아감
        }
        // 상품 저장
        service.save(product);
        return "redirect:/products"; // 저장 후 상품 목록 페이지로 리다이렉트
    }

    // 상품 삭제 메서드
    @GetMapping("/delete/{id}") // "/products/delete/{id}"로 접근 시 호출됨
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        service.deleteById(id); // 상품 ID로 상품 삭제
        return "redirect:/products"; // 삭제 후 상품 목록 페이지로 리다이렉트
    }

    // 상품 목록 페이지 (로그인 성공 메시지 처리)
    @GetMapping("/products") // "/products"로 접근 시 호출됨
    public String viewProducts(Model model) {
        // 로그인 성공 후 메시지 전달
        if (model.containsAttribute("loginSuccess")) {
            model.addAttribute("loginSuccess", "로그인에 성공하셨습니다.");
        }
        return "products/list"; // 상품 목록 페이지로 리턴
    }
}
