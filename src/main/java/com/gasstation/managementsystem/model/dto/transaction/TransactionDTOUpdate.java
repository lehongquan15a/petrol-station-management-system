package com.gasstation.managementsystem.model.dto.transaction;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDTOUpdate {
    private Date time;
    private Double volume;
    private Double unitPrice;
    private Integer cardId;

}
