package com.nengjanggo2.demo.food;

import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("select f from Food as f where f.account = :account")
    Slice<Food> findMost5RecentFoodByAccount(@Param("account") Account account, Pageable paging);

    @Query("select f from Food as f where f.account = :account")
    Slice<Food> find5RecentFoodByAccount(@Param("account") Account account, Pageable paging);

    @Query("select f from Food as f where f.account = :account")
    List<Food> findFoodListByAccount(@Param("account") Account account);

    @Query("select f from Food as f where f.account = :account AND f.id = :id")
    Food findFoodByAccountAndFoodId(@Param("account") Account account, @Param("id") Long foodId);

    @Query("select f.category as category, COUNT(f) as count from Food as f WHERE f.account = :account group by f.category")
    List<FoodChartData> getFoodCategoryCnt(@Param("account") Account account);

    @Query("select f.brand as brandName, SUM(f.quantity) as brandCount from Food as f WHERE f.account = :account group by f.brand")
    List<FoodBrandChartData> getFoodBrandCnt(@Param("account") Account account);

    @Query("select f.brand as brandName, SUM(f.quantity) as foodQuantity from Food as f WHERE f.account = :account group by f.brand")
    Slice<FavoriteBrand> getMostFavoriteBrandFood(@Param("account") Account account, Pageable paging);

    @Query("select SUM(f.quantity) as expiredFoodQuantity from Food as f WHERE f.account = :account AND f.isExpired=true")
    Integer getExpiredFoods(@Param("account") Account account);

    @Query("select f from Food as f where f.account = :account AND f.isExpired = true")
    List<Food> findExpiredFoodList(@Param("account") Account account);

    @Query("select f from Food as f where f.account = :account AND f.brand = :brandName")
    List<Food> findFavoriteBrandFood(@Param("account")Account account, @Param("brandName") String brandName);

    @Query("select SUM(f.quantity) from Food as f where f.account = :account")
    Integer getNumberOfAllFoodList(@Param("account") Account account);
}
