package kr.ac.hansung.cse.hellospringdatajpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "상품 이름은 필수 항목입니다.")
    private String name;
    @NotBlank(message = "브랜드는 필수 입력 사항입니다.")
    private String brand;
    @NotBlank(message = "원산지는 필수 입력 사항입니다.")
    private String madeIn;
    @NotNull(message = "가격은 필수 입력 사항입니다.")
    @DecimalMin(value = "0.01", message = "가격은 0보다 커야 합니다.")
    private Double price;

    public Product(String name, String brand, String madeIn, double price) {
        this.name = name;
        this.brand = brand;
        this.madeIn = madeIn;
        this.price = price;
    }
}