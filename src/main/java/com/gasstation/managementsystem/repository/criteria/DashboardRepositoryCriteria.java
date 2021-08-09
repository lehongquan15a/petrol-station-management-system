package com.gasstation.managementsystem.repository.criteria;

import com.gasstation.managementsystem.entity.Fuel;
import com.gasstation.managementsystem.entity.Station;
import com.gasstation.managementsystem.entity.Tank;
import com.gasstation.managementsystem.model.FuelStatistic;
import com.gasstation.managementsystem.model.TankStatistic;
import com.gasstation.managementsystem.model.dto.dashboard.FuelStatisticDTOFilter;
import com.gasstation.managementsystem.model.dto.dashboard.TankStatisticDTOFilter;
import com.gasstation.managementsystem.utils.OptionalValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryCriteria {
    private final EntityManager em;
    private final OptionalValidate optionalValidate;

    public HashMap<String, Object> fuelStatistic(FuelStatisticDTOFilter filter) {
        String str = "select total_revenue_tbl.*,\n" +
                "       coalesce(total_debt_tbl.total_debt, 0) as total_debt,\n" +
                "       coalesce(total_cash_tbl.total_cash, 0) as total_cash\n" +
                "from (select ft.id                                           as fuel_id,\n" +
                "             ft.name                                         as fuel_name,\n" +
                "             st.id                                           as station_id,\n" +
                "             st.name                                         as station_name,\n" +
                "             coalesce(sum(tran.volume), 0)                   as total_volume,\n" +
                "             coalesce(sum(tran.volume * tran.unit_price), 0) as total_revenue\n" +
                "      from transaction_tbl tran\n" +
                "               inner join pump_shift_tbl pst on pst.id = tran.pump_shift_id\n" +
                "               inner join pump_tbl pt on pt.id = pst.pump_id\n" +
                "               inner join tank_tbl tt on tt.id = pt.tank_id\n" +
                "               inner join station_tbl st on st.id = tt.station_id\n" +
                "               inner join fuel_tbl ft on ft.id = tt.fuel_id\n" +
                "      where tran.time between 0 and 9000000000000000\n" +
                "      group by ft.id, st.id) as total_revenue_tbl\n" +
                "         left join\n" +
                "     (select ft.id                                       as fuel_id,\n" +
                "             st.id                                       as station_id,\n" +
                "             coalesce(sum(tt.volume * tt.unit_price), 0) as total_cash\n" +
                "      from transaction_tbl tt\n" +
                "               inner join pump_shift_tbl pst\n" +
                "                          on pst.id = tt.pump_shift_id\n" +
                "               inner join pump_tbl pt on pt.id = pst.pump_id\n" +
                "               inner join tank_tbl t on t.id = pt.tank_id\n" +
                "               inner join station_tbl st on st.id = t.station_id\n" +
                "               inner join fuel_tbl ft on ft.id = t.fuel_id\n" +
                "      where tt.card_id is null\n" +
                "         or (tt.id in (select receipt_tbl.transaction_id from receipt_tbl))\n" +
                "      group by ft.id, st.id) as total_cash_tbl\n" +
                "     on total_revenue_tbl.fuel_id = total_cash_tbl.fuel_id\n" +
                "         and total_revenue_tbl.station_id = total_cash_tbl.station_id\n" +
                "         left join\n" +
                "     (select ft.id                    as fuel_id,\n" +
                "             st.id                    as station_id,\n" +
                "             sum(dt.accounts_payable) as total_debt\n" +
                "      from debt_tbl dt\n" +
                "               inner join transaction_tbl tt on tt.id = dt.transaction_id\n" +
                "               inner join pump_shift_tbl pst on pst.id = tt.pump_shift_id\n" +
                "               inner join pump_tbl pt on pt.id = pst.pump_id\n" +
                "               inner join tank_tbl t on t.id = pt.tank_id\n" +
                "               inner join station_tbl st on st.id = t.station_id\n" +
                "               inner join fuel_tbl ft on ft.id = t.fuel_id\n" +
                "      group by ft.id, st.id) as total_debt_tbl\n" +
                "     on total_revenue_tbl.fuel_id = total_debt_tbl.fuel_id\n" +
                "         and total_revenue_tbl.station_id = total_debt_tbl.station_id\n";
        if (filter.getStationIds() != null && filter.getStationIds().length > 0) {
            str += "  where total_revenue_tbl.station_id in (:stationIds)";
        }
        Query nativeQuery = em.createNativeQuery(str);
        nativeQuery.setParameter("startTime", filter.getStartTime());
        nativeQuery.setParameter("endTime", filter.getEndTime());
        if (filter.getStationIds() != null && filter.getStationIds().length > 0) {
            nativeQuery.setParameter("stationIds", Arrays.asList(filter.getStationIds()));
        }

        List<Object[]> listResult = nativeQuery.getResultList();
        List<FuelStatistic> fuelStatistics = new ArrayList<>();
        listResult.forEach(objects -> {
            FuelStatistic fuelStatistic = FuelStatistic.builder()
                    .fuelId((Integer) objects[0])
                    .fuelName((String) objects[1])
                    .stationId((Integer) objects[2])
                    .stationName((String) objects[3])
                    .totalVolume((Double) objects[4])
                    .totalRevenue((Double) objects[5])
                    .totalDebt((Double) objects[6])
                    .totalCash((Double) objects[7])
                    .build();
            fuelStatistics.add(fuelStatistic);
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", fuelStatistics);
        return map;
    }

    public HashMap<String, Object> tankStatistic(TankStatisticDTOFilter filter) {
        final int TANK_ID = 0;
        final int TANK_NAME = 1;
        final int TANK_VOLUME = 2;
        final int TANK_REMAIN = 3;
        final int TANK_CURRENT_PRICE = 4;
        final int FUEL_ID = 5;
        final int FUEL_NAME = 6;
        final int STATION_ID = 7;
        final int STATION_NAME = 8;
        final int TOTAL_IMPORT = 9;
        final int TOTAL_EXPORT = 10;
        String str = "select tank_tbl.*,\n" +
                "       tank_statistic.total_import,\n" +
                "       tank_statistic.total_export\n" +
                "from (select tank.id            as tank_id,\n" +
                "             tank.name          as tank_name,\n" +
                "             tank.volume        as tank_volume,\n" +
                "             tank.remain        as tank_remain,\n" +
                "             tank.current_price as tank_current_price,\n" +
                "             fuel_tbl.id        as fuel_id,\n" +
                "             fuel_tbl.name      as fuel_name,\n" +
                "             st.id              as station_id,\n" +
                "             st.name            as station_name\n" +
                "      from tank_tbl tank\n" +
                "               inner join fuel_tbl on tank.fuel_id = fuel_tbl.id\n" +
                "               inner join station_tbl st on tank.station_id = st.id) as tank_tbl\n" +
                "         left join\n" +
                "\n" +
                "     (select tn.tank_id as tank_id,\n" +
                "             tn.total_import,\n" +
                "             tx.total_export\n" +
                "      from (select tt.id                        as tank_id,\n" +
                "                   coalesce(sum(fit.volume), 0) as total_import\n" +
                "            from fuel_import_tbl fit\n" +
                "                     right join tank_tbl tt on tt.id = fit.tank_id\n" +
                "            where fit.created_date between 0 and 9000000000000\n" +
                "               or fit.created_date is null\n" +
                "            group by tt.id, tt.station_id) as tn\n" +
                "               inner join\n" +
                "           (select tt.id                         as tank_id,\n" +
                "                   coalesce(sum(tran.volume), 0) as total_export\n" +
                "            from transaction_tbl tran\n" +
                "                     right join pump_shift_tbl pst on pst.id = tran.pump_shift_id\n" +
                "                     right join pump_tbl pt on pt.id = pst.pump_id\n" +
                "                     right join tank_tbl tt on tt.id = pt.tank_id\n" +
                "                     right join fuel_tbl ft on ft.id = tt.fuel_id\n" +
                "            where tran.time between 0 and 900000000000000\n" +
                "               or tran.time is null\n" +
                "            group by tt.id\n" +
                "            having tt.id is not null) as tx\n" +
                "           on tx.tank_id = tn.tank_id\n" +
                "     ) as tank_statistic\n" +
                "     on tank_tbl.tank_id = tank_statistic.tank_id\n";

        if (filter.getStationIds() != null && filter.getStationIds().length > 0) {
            str += " where tank_tbl.station_id in (:stationIds)";
        }
        Query nativeQuery = em.createNativeQuery(str);
        nativeQuery.setParameter("startTime", filter.getStartTime());
        nativeQuery.setParameter("endTime", filter.getEndTime());
        if (filter.getStationIds() != null && filter.getStationIds().length > 0) {
            nativeQuery.setParameter("stationIds", Arrays.asList(filter.getStationIds()));
        }

        List<Object[]> listResult = nativeQuery.getResultList();
        List<TankStatistic> tankStatistics = new ArrayList<>();
        listResult.forEach(objects -> {
            TankStatistic tankStatistic = TankStatistic.builder()
                    .tank(Tank.builder()
                            .id((Integer) objects[TANK_ID])
                            .name((String) objects[TANK_NAME])
                            .volume((Double) objects[TANK_VOLUME])
                            .currentPrice((Double) objects[TANK_CURRENT_PRICE])
                            .fuel(Fuel.builder()
                                    .id((Integer) objects[FUEL_ID])
                                    .name((String) objects[FUEL_NAME]).build())
                            .station(Station.builder()
                                    .id((Integer) objects[STATION_ID])
                                    .name((String) objects[STATION_NAME]).build()).build())
                    .totalImport((Double) objects[TOTAL_IMPORT])
                    .totalExport((Double) objects[TOTAL_EXPORT])
                    .build();
            tankStatistics.add(tankStatistic);
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", tankStatistics);
        return map;
    }
}
