package com.jphilips.springemergencyapi.dto;

import java.lang.reflect.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultResponse<T> {
    private String message;
    private T body;

    public DefaultResponse(String message, String bodyName, T value) {
        try {
            Field field = DefaultResponse.class.getDeclaredField(bodyName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
