package com.nengjanggo2.demo;

import com.nengjanggo2.demo.account.AccountRepository;
import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import com.nengjanggo2.demo.food.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class App {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FoodRepository foodRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            Account testAccount = Account.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .emailVerified(true)
                    .password(passwordEncoder.encode("123123"))
                    .joinedAt(LocalDateTime.now())
                    .build();


            Food testFood1 = new Food("고향만두", "즉석식품", "CJ", testAccount, LocalDate.of(2020, 5, 25)
                    , LocalDate.of(2020, 5, 30), LocalDate.of(2020, 5, 25)
                    , 5, true, false);
            Food testFood2 = new Food("핫도그", "즉석식품", "목우촌", testAccount, LocalDate.of(2020, 5, 26)
                    , LocalDate.of(2022, 4, 30), LocalDate.of(2020, 5, 27)
                    , 1, true, false);
            Food testFood3 = new Food("돼지갈비", "정육", "정육점", testAccount, LocalDate.of(2020, 5, 26)
                    , LocalDate.of(2020, 5, 30), LocalDate.of(2020, 5, 27)
                    , 2, true, false);
            Food testFood4 = new Food("바나나", "과일", "과일가게", testAccount, LocalDate.of(2020, 4, 28)
                    , LocalDate.of(2020, 5, 2), LocalDate.of(2020, 4, 28)
                    , 5, true, true);
            Food testFood5 = new Food("아이스크림", "과자", "베스킨라빈스", testAccount, LocalDate.of(2020, 5, 28)
                    , LocalDate.of(2021, 1, 12), null
                    , 2, false, false);

            accountRepository.save(testAccount);

            foodRepository.save(testFood1);
            foodRepository.save(testFood2);
            foodRepository.save(testFood3);
            foodRepository.save(testFood4);
            foodRepository.save(testFood5);
        };

    }
}
