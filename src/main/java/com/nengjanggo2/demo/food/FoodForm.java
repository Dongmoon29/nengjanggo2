package com.nengjanggo2.demo.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodForm {

    private Long id;

    @NotBlank(message = "식품이름은 공백이면 안됩니다.")
    @Length(min = 1, max = 30, message = "최소 1글자에서 30글자로 작성해주세요")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{1,30}$", message = "한글과 영문소문자 특수문자는 _ , -  만 가능합니다")
    private String name;

    private boolean isOpened = false;

    @NotNull(message = "식품종류는 공백이면 안됩니다.")
    @Length(min = 1, max = 30, message = "최소 1글자에서 30글자로 작성해주세요")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{1,30}$", message = "한글과 영문 특수문자는 _ , -  만 가능합니다")
    private String category;

    @Length(min = 1, max = 30, message = "최소 1글자에서 30글자로 작성해주세요")
    private String brand;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openedAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate boughtAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "유통기한은 공백일 수 없습니다.")
//    @Future(message = "유통기한 날짜를 다시 확인해주세요 (현재 날짜보다 지난 날짜이여야함.)")
    private LocalDate expiredAt;

    @Min(value = 1, message = "1이상의 숫자를 입력해주세요")
    private int quantity;

}
