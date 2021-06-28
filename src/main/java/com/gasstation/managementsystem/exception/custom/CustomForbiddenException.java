package com.gasstation.managementsystem.exception.custom;

import com.gasstation.managementsystem.model.CustomError;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CustomForbiddenException extends Exception {
    Map<String, CustomError> errorHashMap;

    public CustomForbiddenException(CustomError customError) {
        errorHashMap = new HashMap<>();
        errorHashMap.put("error", customError);
    }

}
