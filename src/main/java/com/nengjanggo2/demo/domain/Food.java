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

    private boolean isOpened = false;

    private boolean isExpired = false;

    public void openedFood() {
        if (this.openedAt != null) {
            this.isOpened = true;
        }
    }

    public void expiredFood() {
        if (this.expiredAt.isBefore(LocalDate.now())) {
            this.isExpired = true;
        }
    }
}
