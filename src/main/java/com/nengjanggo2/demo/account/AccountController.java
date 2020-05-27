package com.nengjanggo2.demo.account;

import com.nengjanggo2.demo.account.dto.RegistrationForm;
import com.nengjanggo2.demo.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final RegistrationFormValidator registrationFormValidator;

    /*
        회원가입 폼 validator
    */
    @InitBinder
    public void registrationValidation(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(registrationFormValidator);
    }
    // 회원가입 페이지 콘트롤러
    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "account/registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute RegistrationForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "account/registration";
        }
        Account account = accountService.processNewAccount(form);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/checkConfirmMail")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/checkEmail";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model) {
        if(!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 메일은 1시간에 한번만 전송할 수 있습니다");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }
        accountService.sendConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/checkedEmail";
        if(account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if(!account.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }
        accountService.completeSignUp(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;

    }

}
