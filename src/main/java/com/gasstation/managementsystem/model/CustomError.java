package com.gasstation.managementsystem.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CustomError {
    private String code;
    private String message;
    private String field;
    private String table;
}
