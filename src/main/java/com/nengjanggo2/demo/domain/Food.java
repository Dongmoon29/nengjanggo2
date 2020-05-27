package com.nengjanggo2.demo.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue
    @Column(name = "food_id")
    private Long id;

    private String name;

    private String category;

    private String brand;

    //다대일 관계 매핑 (Food : n = Account : 1)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate boughtAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openedAt;

    private int quantity;

    // 사용자로부터 openedAt 값을 폼으로 부터 받았을 시 (openedAt 값이 null이 아닐 시) false 그 외의 경우 true반환
    private boolean isOpened = false;

    // 사용자로부터 받은 expiredAt 값이 현재 시간보다 지났을 경우 true 반환
    private boolean isExpired = false;

    // CommandLineRunner로 초기 데이터 생성시 편의 생성자
    public Food(String name, String category, String brand, Account account, LocalDate boughtAt, LocalDate expiredAt, LocalDate openedAt, int quantity, boolean isOpened, boolean isExpired) {
        this.name = name;
        this.account = account;
        this.category = category;
        this.brand = brand;
        this.boughtAt = boughtAt;
        this.expiredAt = expiredAt;
        this.openedAt = openedAt;
        this.quantity = quantity;
        this.isOpened = isOpened;
        this.isExpired = isExpired;
    }

    // 사용자로부터 받은 openedAt 값이 null이 아닐 시 true 반환
    public void openedFood() {
        if (this.openedAt != null) {
            this.isOpened = true;
        }
    }

    // expiredAt 값이 현재보다 지났을 경우 true 반환
    public void expiredFood() {
        if (this.expiredAt.isBefore(LocalDate.now())) {
            this.isExpired = true;
        }
    }
}
