package com.gasstation.managementsystem.exception.custom;

import com.gasstation.managementsystem.model.CustomError;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CustomInternalServerException extends Exception {
    Map<String, CustomError> errorHashMap;

    public CustomInternalServerException(CustomError customError) {
        errorHashMap = new HashMap<>();
        errorHashMap.put("error", customError);
    }

}
