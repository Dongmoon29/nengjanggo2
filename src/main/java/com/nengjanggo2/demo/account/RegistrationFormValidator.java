package com.nengjanggo2.demo.account;

import com.nengjanggo2.demo.account.dto.RegistrationForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
//사용자 회원가입 시 사용되는 Form validator
@Component
@RequiredArgsConstructor
public class RegistrationFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RegistrationForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        RegistrationForm registrationForm = (RegistrationForm)object;
        if(accountRepository.existsByEmail(registrationForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[] {registrationForm.getEmail()}, "이미 사용중인 이메일입니다");

        }
        if(accountRepository.existsByNickname(registrationForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{registrationForm.getNickname()}, "이미 사용중인 닉네임 입니다");
        }
    }
}
