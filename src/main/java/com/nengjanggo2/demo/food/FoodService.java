package com.nengjanggo2.demo.food;

import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    public List<Food> get5FoodClosedToBeExpired(Account account) {
        Integer pageNo = 0;
        Integer pageSize = 5;
        String sortBy = "expiredAt";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Slice<Food> pagedResult = foodRepository.findMost5RecentFoodByAccount(account, paging);

        return pagedResult.getContent();

    }

    public List<Food> get5RecentFood(Account account) {
        Integer pageNo = 0;
        Integer pageSize = 5;
        String sortBy = "id";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Slice<Food> pagedResult = foodRepository.findMost5RecentFoodByAccount(account, paging);

        return pagedResult.getContent();
    }

    public List<FavoriteBrand> getMostFavoriteBrand(Account account) {
        Integer pageNo = 0;
        Integer pageSize = 1;
        String sortBy = "foodQuantity";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Slice<FavoriteBrand> pagedResult = foodRepository.getMostFavoriteBrandFood(account, paging);

        return pagedResult.getContent();
    }
    public Integer getNumberOfExpiredFood(Account account) {
        return foodRepository.getExpiredFoods(account);
    }

    public List<Food> findFoodByAccount(Account account) {
        return foodRepository.findFoodListByAccount(account);
    }

    public Food saveNewFood(Account account, FoodForm form) {
        Food food = Food.builder()
                .account(account)
                .name(form.getName())
                .category(form.getCategory())
                .brand(form.getBrand())
                .quantity(form.getQuantity())
                .boughtAt(form.getBoughtAt())
                .openedAt(form.getOpenedAt())
                .expiredAt(form.getExpiredAt())
                .build();

        food.expiredFood();
        food.openedFood();


        return foodRepository.save(food);
    }


    public Food getFoodDetail(Account account, Long foodId) {
        return foodRepository.findFoodByAccountAndFoodId(account, foodId);
    }

    public List<Food> findExpiredFoodByAccount(Account account) {
        return foodRepository.findExpiredFoodList(account);
    }

    public List<Food> findFavoriteBrandFoods(Account account, String brandName) {
        return foodRepository.findFavoriteBrandFood(account, brandName);
    }

    public Integer getNumberOfAllFoodList(Account account) {
        return foodRepository.getNumberOfAllFoodList(account);
    }
}
