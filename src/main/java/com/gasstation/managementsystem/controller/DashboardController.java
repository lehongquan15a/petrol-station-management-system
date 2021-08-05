package com.gasstation.managementsystem.controller;

import com.gasstation.managementsystem.entity.Api;
import com.gasstation.managementsystem.model.dto.dashboard.FuelStatisticDTOFilter;
import com.gasstation.managementsystem.model.dto.dashboard.TankStatisticDTOFilter;
import com.gasstation.managementsystem.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = Api.PREFIX)
@CrossOrigin
@Tag(name = "Dashboard", description = "API for dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "Fuel statistics")
    @GetMapping("fuel-statistics")
    public HashMap<String, Object> fuelStatistic(@RequestParam(name = "startTime") Long startTime,
                                                 @RequestParam(name = "endTime") Long endTime,
                                                 @RequestParam(name = "stationId", required = false) Integer stationId) {
        FuelStatisticDTOFilter filter = FuelStatisticDTOFilter.builder()
                .startTime(startTime)
                .endTime(endTime).stationId(stationId).build();
        return dashboardService.fuelStatistic(filter);
    }

    @Operation(summary = "Tank statistics")
    @GetMapping("tank-statistics")
    public HashMap<String, Object> tankStatistic(@RequestParam(name = "startTime") Long startTime,
                                                 @RequestParam(name = "endTime") Long endTime) {
        TankStatisticDTOFilter filter = TankStatisticDTOFilter.builder()
                .startTime(startTime)
                .endTime(endTime).build();
        return dashboardService.tankStatistic(filter);
    }
}
