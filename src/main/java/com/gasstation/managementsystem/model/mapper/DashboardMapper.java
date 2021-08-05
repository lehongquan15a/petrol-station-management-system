package com.gasstation.managementsystem.model.mapper;

import com.gasstation.managementsystem.model.Dashboard;
import com.gasstation.managementsystem.model.dto.dashboard.DashboardDTO;
import com.gasstation.managementsystem.model.dto.fuel.FuelDTO;
import com.gasstation.managementsystem.model.dto.station.StationDTO;

public class DashboardMapper {

    public static DashboardDTO toDashboardDTO(Dashboard dashboard) {
        if (dashboard == null) return null;

        FuelDTO fuelDTO = dashboard.getFuelId() != null ? FuelDTO.builder().id(dashboard.getFuelId())
                .name(dashboard.getFuelName()).build() : null;
        StationDTO stationDTO = dashboard.getStationId() != null ? StationDTO.builder()
                .id(dashboard.getStationId())
                .name(dashboard.getStationName())
                .address(dashboard.getStationAddress()).build() : null;
        return DashboardDTO.builder()
                .fuel(fuelDTO)
                .station(stationDTO)
                .totalRevenue(dashboard.getTotalRevenue())
                .totalDebt(dashboard.getTotalDebt())
                .totalVolume(dashboard.getTotalVolume())
                .totalPaid(dashboard.getTotalPaid()).build();
    }
}
