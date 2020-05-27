package com.nengjanggo2.demo.food;

import com.nengjanggo2.demo.domain.Account;
import com.nengjanggo2.demo.domain.Food;
import com.nengjanggo2.demo.food.dto.FavoriteBrand;
import com.nengjanggo2.demo.food.dto.FoodBrandChartData;
import com.nengjanggo2.demo.food.dto.FoodChartData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    // @CurrentUser(현재 로그인한 사용자)의 최근 등록한 5개의 식품 쿼리
    @Query("select f from Food as f where f.account = :account")
    Slice<Food> findMost5RecentFoodByAccount(@Param("account") Account account, Pageable paging);

    // @CurrentUser(현재 로그인한 사용자)의 전체 식품 목록 쿼리
    @Query("select f from Food as f where f.account = :account")
    List<Food> findFoodListByAccount(@Param("account") Account account);

    // @CurrentUser(현재 로그인한 사용자)의 특정 식품 쿼리
    @Query("select f from Food as f where f.account = :account AND f.id = :id")
    Food findFoodByAccountAndFoodId(@Param("account") Account account, @Param("id") Long foodId);

    // Overview 페이지의 차트에 식품 종류별 차트에 쓰이는 DTO에 보내는 데이터 쿼리
    // String category, int count
    @Query("select f.category as category, COUNT(f) as count from Food as f WHERE f.account = :account group by f.category")
    List<FoodChartData> getFoodCategoryCnt(@Param("account") Account account);

    // Overview 페이지의 차트에 식품 종류별 차트에 쓰이는 DTO에 보내는 데이터 쿼리
    // String brandName, int brandCount
    @Query("select f.brand as brandName, SUM(f.quantity) as brandCount from Food as f WHERE f.account = :account group by f.brand")
    List<FoodBrandChartData> getFoodBrandCnt(@Param("account") Account account);

    // 가장많은 브랜드 식품을 구한 값을 FavoriteBrand DTO클래스로 받은 후 FavoriteBrand "리스트"를 반환
    @Query("select f.brand as brandName, SUM(f.quantity) as foodQuantity from Food as f WHERE f.account = :account group by f.brand")
    Slice<FavoriteBrand> getMostFavoriteBrandFood(@Param("account") Account account, Pageable paging);

    // 사용자의 유통기한 만료된 음식 갯수 쿼리
    @Query("select SUM(f.quantity) as expiredFoodQuantity from Food as f WHERE f.account = :account AND f.isExpired=true")
    Integer getExpiredFoods(@Param("account") Account account);

    // @CurrentUser(현재 로그인한 사용자)의 유통기한 만료 식품 갯수 쿼리
    @Query("select f from Food as f where f.account = :account AND f.isExpired = true")
    List<Food> findExpiredFoodList(@Param("account") Account account);

    // @CurrentUser(현재 로그인한 사용자)가 가장 많은 수의 브랜드식품 쿼리 Food "List"를 반환
    @Query("select f from Food as f where f.account = :account AND f.brand = :brandName")
    List<Food> findFavoriteBrandFood(@Param("account")Account account, @Param("brandName") String brandName);

    // @CurrentUser(현재 로그인한 사용자)의 전체 식품 수 쿼리
    @Query("select SUM(f.quantity) from Food as f where f.account = :account")
    Integer getNumberOfAllFoodList(@Param("account") Account account);
}
