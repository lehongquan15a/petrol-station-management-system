package com.gasstation.managementsystem.model.dto.debt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DebtDTOSummaryFilter {
    private Integer pageIndex;
    private Integer pageSize;
    private String cardId;
    private Integer[] stationIds;
    private String customerName;
    private String customerPhone;
    private Double totalAccountsPayable;
}
