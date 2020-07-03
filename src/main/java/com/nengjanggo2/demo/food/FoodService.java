package com.nengjanggo2.demo.food;

import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import com.nengjanggo2.demo.food.dto.FavoriteBrand;
import com.nengjanggo2.demo.food.dto.FoodForm;
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

    // 유통기한 제일 임박한 식품 5개 구현 메서드
    public List<Food> get5FoodClosedToBeExpired(Account account) {
        Integer pageNo = 0;
        Integer pageSize = 5;
        String sortBy = "expiredAt";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Slice<Food> pagedResult = foodRepository.findMost5RecentFoodByAccount(account, paging);

        return pagedResult.getContent();

    }
    // 최근 등록한 5개 식품 구현 메서드
    public List<Food> get5RecentFood(Account account) {
        Integer pageNo = 0;
        Integer pageSize = 5;
        String sortBy = "id";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Slice<Food> pagedResult = foodRepository.findMost5RecentFoodByAccount(account, paging);

        return pagedResult.getContent();
    }
    //가장 선호하는 브랜드 구현 메소드
    //FoodRepository에서 가져온 FavoriteBrand 리스트의 인덱스 0의 list를 리턴
    // TODO: FoodRepository에서 JPQL로 FavoriteBrand 최상단 하나의 row만 가져오는 방법 연구해보기
    public List<FavoriteBrand> getMostFavoriteBrand(Account account) {
        Integer pageNo = 0;
        Integer pageSize = 1;
        String sortBy = "foodQuantity";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Slice<FavoriteBrand> pagedResult = foodRepository.getMostFavoriteBrandFood(account, paging);

        return pagedResult.getContent();
    }
    // 유통기한 만료된 식품 갯수 구현 메서드
    public Integer getNumberOfExpiredFood(Account account) {
        return foodRepository.getExpiredFoods(account);
    }

    // 현재 접속한 사용자의 전체 식품 목록 구현 메서드
    public List<Food> findFoodByAccount(Account account) {
        return foodRepository.findFoodListByAccount(account);
    }

    // 새 식품 등록 구현 메서드
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

    public Food updateFood(Account account, FoodForm form) {
        Food food = Food.builder()
                .account(account)
                .id(form.getId())
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

    // 사용자의 음식 상세 구현 메서드
    public Food getFoodDetail(Account account, Long foodId) {
        return foodRepository.findFoodByAccountAndFoodId(account, foodId);
    }

    // 사용자의 유통기한 만료된 음식 목록 구현 메서드
    public List<Food> findExpiredFoodByAccount(Account account) {
        return foodRepository.findExpiredFoodList(account);
    }

    // 사용자 선호 브랜드 식품 목록 구현 메서드
    public List<Food> findFavoriteBrandFoods(Account account, String brandName) {
        return foodRepository.findFavoriteBrandFood(account, brandName);
    }

    // 사용자 전체 음식 갯수 구현 메서드
    public Integer getNumberOfAllFoodList(Account account) {
        return foodRepository.getNumberOfAllFoodList(account);
    }
}
