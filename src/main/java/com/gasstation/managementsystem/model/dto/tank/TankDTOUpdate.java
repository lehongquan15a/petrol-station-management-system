package com.gasstation.managementsystem.model.dto.tank;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class TankDTOUpdate {
    private String name;
    private Double volume;
    private Double remain;
    private Double currentPrice;
    private Integer stationId;
    private Integer fuelId;
}
