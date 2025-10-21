package com.an.DentalClinicSystem.utils;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
public class UpdateUtils {

    /**
     * Cập nhật giá trị nếu giá trị mới khác giá trị hiện tại và không null.
     *
     * @param newValue     giá trị mới từ request
     * @param currentValue giá trị hiện tại trong entity
     * @param setter       phương thức setter tương ứng
     * @param <T>          kiểu dữ liệu chung
     */
    public static <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
        }
    }
}