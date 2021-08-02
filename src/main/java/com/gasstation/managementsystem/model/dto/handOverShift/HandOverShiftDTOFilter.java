package com.gasstation.managementsystem.model.dto.handOverShift;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HandOverShiftDTOFilter {
    public static final String STATUS_CLOSED = "CLOSED";
    public static final String STATUS_UNCLOSE = "UNCLOSE";
    private Integer pageIndex;
    private Integer pageSize;
    private Long createdDate;
    private Long closedTime;
    private Integer[] shiftIds;
    private Integer[] pumpIds;
    private Integer[] stationIds;
    private String executorName;
    private String[] statuses;

}