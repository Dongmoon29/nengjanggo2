package com.nengjanggo2.demo.account;

import com.nengjanggo2.demo.domain.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Getter
public class UserAccount extends User {

    private Account account;


    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("USER_ROLE")));{

            this.account = account;
        }
    }
}
