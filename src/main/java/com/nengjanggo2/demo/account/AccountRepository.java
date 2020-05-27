package com.nengjanggo2.demo.account;

import com.nengjanggo2.demo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // 회원가입 이메일 중복 확인(email은 unique한 값이여야함.)
    boolean existsByEmail(String email);
    // 회원가입 닉네임 중복확인(nickname은 unique한 값이여야함.)
    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nickname);
}

