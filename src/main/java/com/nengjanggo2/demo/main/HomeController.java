package com.nengjanggo2.demo.main;

import com.nengjanggo2.demo.account.AccountService;
import com.nengjanggo2.demo.account.CurrentUser;
import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import com.nengjanggo2.demo.food.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AccountService accountService;
    private final FoodService foodService;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute("account", account);
        }
        List<Food> most5FoodClosedToBeExpired = foodService.get5FoodClosedToBeExpired(account);
        model.addAttribute("most5FoodClosedToBeExpired", most5FoodClosedToBeExpired);

        List<Food> mostRecentlyRegistered5FoodList = foodService.get5RecentFood(account);
        model.addAttribute("recent5FoodList", mostRecentlyRegistered5FoodList);
        return "home";
    }

    @GetMapping("/settings")
    public String accountSetting(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        return "account/settings";
    }


    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }
}
