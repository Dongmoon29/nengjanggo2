package com.nengjanggo2.demo.account;

import com.nengjanggo2.demo.account.dto.RegistrationForm;
import com.nengjanggo2.demo.config.AppProperties;
import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.email.EmailMessage;
import com.nengjanggo2.demo.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    // 회원가입
    public Account saveNewAccount(@ModelAttribute @Valid RegistrationForm form) {
        Account account = Account.builder()
                .email(form.getEmail())
                .nickname(form.getNickname())
                .password(passwordEncoder.encode(form.getPassword()))
                .build();

        account.generateEmailCheckToken();
    // JPA를 통해 DB에 회원가입된 회원정보 저장
        return accountRepository.save(account);
    }
    // authentication 이메일 전송 메소드
    public void sendConfirmEmail(Account savedAccount) {
        //이메일 구현 부
        //Context는 Model과 비슷한 기능을 하는 클래스라고 보면된다.
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token="+ savedAccount.getEmailCheckToken() + "&email=" + savedAccount.getEmail());
        context.setVariable("nickname", savedAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "링크를 클릭하세요");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);


        EmailMessage emailMessage = EmailMessage.builder()
                .to(savedAccount.getEmail())
                .subject("냉장고 회원가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
    // 회원가입된 사용자에게 authentication 이메일보내기
    public Account processNewAccount(RegistrationForm form) {
        Account savedAccount = saveNewAccount(form);

        sendConfirmEmail(savedAccount);

        return savedAccount;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }
        if(account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }
        return new UserAccount(account);
    }
    // Spring Security의 로그인 처리방식 UsernamePasswordAuthenticationToken 객체를 SecurityContextHolder의 authentication객체를 넘긴다.
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);


    }
    // 회원가입 완료된 사용자 자동 로그인처리
    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }


}
