package com.nengjanggo2.demo.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FoodFormValidator implements Validator {
    private final FoodRepository foodRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FoodForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
//        FoodForm foodForm = (FoodForm)target;
//        if(foodForm.getOpenedAt() != null && foodForm.getExpiredAt() != null && foodForm.getOpenedAt().isBefore(foodForm.getBoughtAt())){
//            errors.rejectValue("openedAt", "wrongValue", "개봉일은 구입일 이후로 입력가능 합니다.");
//        }
//
//        if(foodForm.getOpenedAt() != null && foodForm.getExpiredAt() != null && foodForm.getExpiredAt().isBefore(foodForm.getBoughtAt())) {
//            errors.rejectValue("expiredAt", "wrongValue", "유통기한은 구입일 이후로 입력가능 합니다.");
//        }
//
//        if(foodForm.getExpiredAt() == null) {
//            errors.rejectValue("expiredAt", "wrongValue", "유통기한을 작성해주세요.");
//        }


    }
}
