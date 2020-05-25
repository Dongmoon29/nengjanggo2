package com.nengjanggo2.demo.food;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nengjanggo2.demo.account.AccountRepository;
import com.nengjanggo2.demo.account.CurrentUser;
import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final FoodRepository foodRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/food/new")
    public String getFoodForm(@CurrentUser Account account, Model model) {
        model.addAttribute("foodForm", new FoodForm());
        model.addAttribute("account", account);

        return "food/food";
    }

    @PostMapping("/food/new")
    public String createFood(@CurrentUser Account account, Model model, @Valid @ModelAttribute FoodForm form, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "food/food";
        }
        Food food = foodService.saveNewFood(account, form);
        return "redirect:/nengjanggo";
    }

    @GetMapping("/nengjanggo")
    public String getFoodList(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);

        List<Food> foodList = foodService.findFoodByAccount(account);
        model.addAttribute("foodList", foodList);

        return "food/foodList";
    }
    @GetMapping("/expiredFoodList")
    public String getExpiredFoodList(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);

        List<Food> expiredFood =  foodService.findExpiredFoodByAccount(account);
        model.addAttribute("expiredFoodList" , expiredFood);
        return "food/expiredFoodList";
    }

    @GetMapping("/favoriteFoodBrandProduct/{brandName}")
    public String getFavoriteFoodBrandProduct(@CurrentUser Account account, Model model, @PathVariable("brandName") String brandName) {
        model.addAttribute("account", account);

        List<Food> favoriteBrandFoods = foodService.findFavoriteBrandFoods(account, brandName);
        model.addAttribute("favoriteBrandFoods", favoriteBrandFoods);

        return "food/favoriteBrandFoodList";
    }


    @PostMapping("/food/{foodId}/delete")
    public String deleteFood(@CurrentUser Account account, Model model, @PathVariable("foodId") Long foodId, RedirectAttributes attributes) {
        Food food = foodRepository.findFoodByAccountAndFoodId(account, foodId);
        foodRepository.delete(food);

        return "redirect:/nengjanggo";
    }


    @GetMapping("/food/{foodId}/detail")
    public String foodDetail(@CurrentUser Account account, Model model, @PathVariable("foodId") Long foodId) {
        Food food = foodService.getFoodDetail(account, foodId);
        model.addAttribute("food", food);
        model.addAttribute("account", account);

        return "food/foodDetail";

    }

    @GetMapping("/overview")
    public String foodOverview(@CurrentUser Account account, Model model) throws JsonProcessingException {
        List<FoodChartData> foodChartData = foodRepository.getFoodCategoryCnt(account);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(foodChartData);
        model.addAttribute("foodCategories", jsonString);

        List<FoodBrandChartData> foodBrandChartDate = foodRepository.getFoodBrandCnt(account);
        String jsonString2 = objectMapper.writeValueAsString(foodBrandChartDate);
        model.addAttribute("foodBrands", jsonString2);

        List<FavoriteBrand> mostFavoriteBrand = foodService.getMostFavoriteBrand(account);
        if(mostFavoriteBrand.size() == 0) {
            mostFavoriteBrand = null;
        }
        model.addAttribute("favoriteBrand", mostFavoriteBrand);

        Integer numberOfExpiredFood = foodService.getNumberOfExpiredFood(account);
        if(numberOfExpiredFood == null) {
            numberOfExpiredFood = 0;
        }
        model.addAttribute("numberOfExpiredFood", numberOfExpiredFood);

        Integer numberOfAllFoodList = foodService.getNumberOfAllFoodList(account);
        if(numberOfAllFoodList == null) {
            numberOfAllFoodList = 0;
        }
        model.addAttribute("numberOfAllFoodList", numberOfAllFoodList);

        model.addAttribute("account", account);
        return "food/overview";
    }


}
